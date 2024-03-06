package com.dku.council.infra.dku.service;

import com.dku.council.domain.cafeteria.model.entity.Cafeteria;
import com.dku.council.domain.cafeteria.model.entity.CafeteriaInfo;
import com.dku.council.domain.cafeteria.repository.CafeteriaInfoRepository;
import com.dku.council.domain.cafeteria.repository.CafeteriaRepository;
import com.dku.council.domain.cafeteria.service.StringFinder;
import com.dku.council.infra.dku.exception.DkuFailedCrawlingException;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Component
@RequiredArgsConstructor
@EnableScheduling
public class DkuCafeteriaService {

    @Value("${dku.cafeteria.api-path}")
    private String url;

    private final CafeteriaRepository cafeteriaRepository;
    private final CafeteriaInfoRepository cafeteriaInfoRepository;
    private String breakfastResult;
    private String lunchResult;
    private String dinnerResult;
    private String otherResult;
    private String originResult;
    private String allergyResult;
    private Map<String, String> notOperated = new HashMap<>() {
        {
            put("BRE", "조식 [미운영]");
            put("LUN", "중식 [미운영]");
            put("DIN", "석식 [미운영]");
            put("OTH", "기타 [미운영]");
        }
    };

    @Transactional
    public void crawlCafeteria() {

        try {
            LocalDate today = LocalDate.now();
            if (today.getDayOfWeek() != DayOfWeek.MONDAY) {
                throw new DkuFailedCrawlingException();
            }

            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("#p_p_id_Food_WAR_foodportlet_ > div > div > div > form > div.food_list > table > tbody");

            /**
             * 요일별 for loop
             */
            int i = 0;
            for (Element el : elements.select("tr")) {
                i++;
                if (i == 7) // 원산지, 알레르기 정보는 for문 밖에서 따로
                    break;

                clearMealResults();

                String result = el.text();

                String foodList = el.select("td:nth-child(2)").text(); // 아예 식단이 없는 경우 (식단 패스)
                if (foodList.isEmpty()) {
                    Cafeteria cafeteria = Cafeteria.builder()
                            .mealDate(today.plusDays(i - 1))
                            .breakfast(notOperated.get("BRE"))
                            .lunch(notOperated.get("LUN"))
                            .dinner(notOperated.get("DIN"))
                            .other(notOperated.get("OTH"))
                            .build();

                    // DB에 해당하는 날짜의 식단이 없다면 insert
                    if (cafeteriaRepository.findByMealDate(today.plusDays(i - 1)).isEmpty()) {
                        cafeteriaRepository.save(cafeteria);
                    }
                    continue;
                }

                List<Integer> breakfastList = StringFinder.kmpSearch(result, "조식");
                List<Integer> lunchList = StringFinder.kmpSearch(result, "중식");
                List<Integer> dinnerList = StringFinder.kmpSearch(result, "석식");
                List<Integer> otherList = StringFinder.kmpSearch(result, "기타");

                String breakfast = result.substring(breakfastList.get(0), lunchList.get(0));
                String lunch = result.substring(lunchList.get(0), dinnerList.get(0));
                String dinner = result.substring(dinnerList.get(0), otherList.get(0));
                String other = result.substring(otherList.get(0));

                if (StringFinder.kmpSearch(result, "조식 [미운영]").isEmpty()) {

                    List<Integer> thousandList = StringFinder.kmpSearch(breakfast, "** 천원의 아침밥 **");
                    List<Integer> breakfastAcourseList = StringFinder.kmpSearch(breakfast, "[A코스]");
                    List<Integer> breakfastBcourseList = StringFinder.kmpSearch(breakfast, "[B코스]");
                    List<Integer> breakfastCcourseList = StringFinder.kmpSearch(breakfast, "[C코스]");

                    // 천원의 아침밥
                    if (!thousandList.isEmpty() && !breakfastAcourseList.isEmpty()) {
                        breakfastResult += (breakfast.substring(thousandList.get(0), breakfastAcourseList.get(0))+"\n");    // 천원의 아침밥
                    }

                    // [A코스] 있는지 탐색
                    Boolean breakfastAFlag = parseCourseA(breakfast, "BRE", breakfastAcourseList, breakfastBcourseList);
                    if (!breakfastAFlag) {

                        // 하나의 식단만 존재할 때, [A코스]라고 명시하지 않는 경우가 존재함
                        StringBuilder sb = new StringBuilder();
                        for (int k = 0; k < breakfast.length(); k++) {
                            char c = breakfast.charAt(k);
                            if (c == '\\') {
                                sb.append('\n');
                            }
                            sb.append(c);
                        }
                        breakfastResult = String.valueOf(sb);
                    }

                    // [B코스] 있는지 탐색
                    Boolean breakfastBFlag = parseCourseB(breakfast, "BRE", breakfastBcourseList, breakfastCcourseList, breakfastAFlag);

                    // [C코스] 있는지 탐색
                    parseCourseC(breakfast, "BRE", breakfastCcourseList, breakfastAFlag, breakfastBFlag);
                }
                else {
                    breakfastResult = notOperated.get("BRE");   // 조식 [미운영]
                }

                if (StringFinder.kmpSearch(result, "중식 [미운영]").isEmpty()) {

                    List<Integer> lunchAcourseList = StringFinder.kmpSearch(lunch, "[A코스]");
                    List<Integer> lunchBcourseList = StringFinder.kmpSearch(lunch, "[B코스]");
                    List<Integer> lunchCcourseList = StringFinder.kmpSearch(lunch, "[C코스]");

                    // [A코스] 있는지 탐색
                    Boolean lunchAFlag = parseCourseA(lunch, "LUN", lunchAcourseList, lunchBcourseList);

                    // [B코스] 있는지 탐색
                    Boolean lunchBFlag = parseCourseB(lunch, "LUN", lunchBcourseList, lunchCcourseList, lunchAFlag);

                    // [C코스] 있는지 탐색
                    parseCourseC(lunch, "LUN", lunchCcourseList, lunchAFlag, lunchBFlag);
                }
                else {
                    lunchResult = notOperated.get("LUN");   // 중식 [미운영]
                }

                if (StringFinder.kmpSearch(result, "석식 [미운영]").isEmpty()) {

                    List<Integer> dinnerAcourseList = StringFinder.kmpSearch(dinner, "[A코스]");
                    List<Integer> dinnerBcourseList = StringFinder.kmpSearch(dinner, "[B코스]");
                    List<Integer> dinnerCcourseList = StringFinder.kmpSearch(dinner, "[C코스]");

                    // [A코스] 있는지 탐색
                    Boolean dinnerAFlag = parseCourseA(dinner, "DIN", dinnerAcourseList, dinnerBcourseList);

                    // [B코스] 있는지 탐색
                    Boolean dinnerBFlag = parseCourseB(dinner, "DIN", dinnerBcourseList, dinnerCcourseList, dinnerAFlag);

                    // [C코스] 있는지 탐색
                    parseCourseC(dinner, "DIN", dinnerCcourseList, dinnerAFlag, dinnerBFlag);
                }
                else {
                    dinnerResult = notOperated.get("DIN");   // 석식 [미운영]
                }

                if (StringFinder.kmpSearch(result, "기타 [미운영]").isEmpty()) {
                    otherResult = other;
                }
                else {
                    otherResult = notOperated.get("OTH");   // 기타 [미운영]
                }

                Cafeteria cafeteria = Cafeteria.builder()
                        .mealDate(today.plusDays(i - 1))
                        .breakfast(breakfastResult)
                        .lunch(lunchResult)
                        .dinner(dinnerResult)
                        .other(otherResult)
                        .build();

                // DB에 해당하는 날짜의 식단이 없다면 insert
                if (cafeteriaRepository.findByMealDate(today.plusDays(i - 1)).isEmpty()) {
                    cafeteriaRepository.save(cafeteria);
                }
            }

            /**
             * 비고 (원산지, 알레르기)
             */
            String etc = doc.select("#p_p_id_Food_WAR_foodportlet_ > div > div > div > form > div.food_list > table > tbody > tr:nth-child(7) > td:nth-child(2)").text();

            List<Integer> originList = StringFinder.kmpSearch(etc, "[원산지]");
            List<Integer> allergyList = StringFinder.kmpSearch(etc, "[알레르기유발물질]");

            originResult = etc.substring(originList.get(0) + 5, originList.get(0) + allergyList.get(0));
            allergyResult = etc.substring(allergyList.get(0) + 10);

            for (int j = 0; j < 6; j++) {
                CafeteriaInfo cafeteriaInfo = CafeteriaInfo.builder()
                        .mealDate(today.plusDays(j))
                        .origin(originResult)
                        .allergy(allergyResult)
                        .build();

                // DB에 해당하는 날짜의 식단이 없다면 insert
                if (cafeteriaInfoRepository.findByMealDate(today.plusDays(i - 1)).isEmpty()) {
                    cafeteriaInfoRepository.save(cafeteriaInfo);
                }
            }

        } catch(Exception e) {
            System.out.println("error: " + e);
        }
    }

    private Boolean parseCourseA(String meal,
                                 String mealType,
                                 List<Integer> AcourseList,
                                 List<Integer> BcourseList) {
        // [A코스] 있는지 탐색
        Boolean flag = true;

        if (!AcourseList.isEmpty()) {
            // [B코스]가 있다면, [B코스]전까지 파싱
            if (!BcourseList.isEmpty()) {
                addMealResult(mealType, meal.substring(AcourseList.get(0), BcourseList.get(0))+"\n");   // A코스
            }
            // 없다면 그 다음 식사전까지 파싱 (ex. 현재 조식 파싱중이라면, 중식 전까지 파싱)
            else {
                addMealResult(mealType, meal.substring(AcourseList.get(0))+"\n");   // A코스
            }
        }
        else { // [A코스]가 없는 경우
            flag = false;
        }
        return flag;
    }

    private Boolean parseCourseB(String meal,
                                 String mealType,
                                 List<Integer> BcourseList,
                                 List<Integer> CcourseList,
                                 Boolean aFlag) {
        // [B코스] 있는지 탐색
        Boolean flag = true;
        if (aFlag && !BcourseList.isEmpty()) {
            // [C코스]가 있다면, [C코스]전까지 파싱
            if (!CcourseList.isEmpty()) {
                addMealResult(mealType, meal.substring(BcourseList.get(0), CcourseList.get(0))+"\n");
            }
            // 없다면 그 다음 식사전까지 파싱
            else {
                addMealResult(mealType, meal.substring(BcourseList.get(0))+"\n");
            }
        }
        else { // [B코스]가 없는 경우
            flag = false;
        }
        return flag;
    }

    private void parseCourseC(String meal,
                              String mealType,
                              List<Integer> CcourseList,
                              Boolean aFlag,
                              Boolean bFlag) {
        if (aFlag && bFlag && !CcourseList.isEmpty()) {
            addMealResult(mealType, meal.substring(CcourseList.get(0))+"\n");
        }
    }

    private void addMealResult(String mealType, String str) {
        if (mealType == "BRE") {
            breakfastResult += str;
        }
        else if (mealType == "LUN") {
            lunchResult += str;
        }
        else if (mealType == "DIN") {
            dinnerResult += str;
        }
    }

    private void clearMealResults() {
        breakfastResult = "";
        lunchResult = "";
        dinnerResult = "";
        otherResult = "";
    }

}
