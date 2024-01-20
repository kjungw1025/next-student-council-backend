package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.user.service.UserService;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedRoommateDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedRoommatePossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateRoommateDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateSurveyDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleRoommateDto;
import com.dku.council.domain.with_dankook.service.RoommateService;
import com.dku.council.domain.with_dankook.service.SurveyService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.global.model.dto.ResponseBooleanDto;
import com.dku.council.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "구해줘! 룸메", description = "구해줘! 룸메 관련 API")
@RequiredArgsConstructor
@RequestMapping("with-dankook/roommate")
public class RoommateController {

    private final RoommateService roommateService;
    private final SurveyService surveyService;
    private final UserService userService;

    /**
     *  구해줘! 룸메 설문조사 작성 여부 확인
     *  <p>구해줘! 룸메 페이지로 로딩될 때 사용되어야 한다.</p>
     *
     *  설문조사를 하지 않았을 때는 설문조사를 먼저 받아야하며, 설문조사가 꼭 있어야만 다른 기능 사용이 가능하다.
     */
    @GetMapping("/survey")
    @UserAuth
    public ResponseBooleanDto isSurveyExist(AppAuthentication auth) {
        userService.isDkuChecked(auth.getUserId());
        boolean result = surveyService.checkSurvey(auth.getUserId());
        return new ResponseBooleanDto(result);
    }

    /**
     * 구해줘! 룸메 설문조사 작성
     * <p>SleepTime: AVG_TEN, AVG_ELEVEN, AVG_TWELVE, AVG_ONE, AVG_TWO, AVG_TWO_AFTER, ETC</p>
     * <p>CleanUpCount : ONCE_UNDER_WEEK, TWICE_WEEK, TWICE_OVER_WEEK, ONCE_MONTH</p>
     * <p>noiseHabit이 true 일 때는, noiseHabitDetail을 필수로 작성해야 한다.</p>
     *
     * @param dto    룸메 설문조사 dto
     * @return       설문조사 id
     */
    @PostMapping("/create/survey")
    @UserAuth
    public ResponseIdDto createSurvey(AppAuthentication auth,
                                      @RequestBody @Valid RequestCreateSurveyDto dto) {
        userService.isDkuChecked(auth.getUserId());
        Long result = surveyService.createSurvey(auth.getUserId(), dto);
        return new ResponseIdDto(result);
    }

    /**
     * 구해줘! 룸메 게시글 작성
     * <p>ResidenceDuration : SEMESTER , HALF_YEAR, YEAR</p>
     */
    @PostMapping
    @UserAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @RequestBody @Valid RequestCreateRoommateDto dto) {
        userService.isDkuChecked(auth.getUserId());
        Long result = roommateService.create(auth.getUserId(), dto);
        return new ResponseIdDto(result);
    }

    /**
     * 구해줘! 룸메 게시글 목록 조회
     *
     * @param bodySize    게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable    페이징 size, sort, page
     * @return            페이징된 게시판 목록
     */
    @GetMapping
    @UserAuth
    public ResponsePage<SummarizedRoommateDto> list(AppAuthentication auth,
                                                    @RequestParam(defaultValue = "50") int bodySize,
                                                    @ParameterObject Pageable pageable) {
        userService.isDkuChecked(auth.getUserId());
        Page<SummarizedRoommateDto> list = roommateService.list(auth.getUserId(), pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 구해줘! 룸메 게시글 상세 조회
     *
     * @param id    게시글 id
     */
    @GetMapping("/{id}")
    @UserAuth
    public ResponseSingleRoommateDto findOne(AppAuthentication auth,
                                                           @PathVariable Long id) {
        userService.isDkuChecked(auth.getUserId());
        return roommateService.findOne(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 구해줘! 룸메 게시글 신청
     * <p>게시글에 신청할 경우 바로 신청되는 것이 아닌 대기중으로 설정됩니다.</p>
     *
     * @param id    게시글 id
     */
    @PostMapping("/apply/{id}")
    @UserAuth
    public void apply(AppAuthentication auth,
                      @Valid @PathVariable Long id) {
        userService.isDkuChecked(auth.getUserId());
        roommateService.apply(auth.getUserId(), id, auth.getUserRole());
    }

    /**
     * 구해줘! 룸메 게시글 승인
     * <p>게시글 작성자가 신청자 id로 승인합니다.</p>
     *
     * @param id              게시글 id
     * @param targetUserId    승인할 사용자 id
     */
    @PatchMapping("/approve/{id}")
    @UserAuth
    public void approve(AppAuthentication auth,
                        @Valid @PathVariable Long id,
                        Long targetUserId) {
        userService.isDkuChecked(auth.getUserId());
        roommateService.approve(auth.getUserId(), id, targetUserId);
    }

    /**
     * 구해줘! 룸메 게시글 삭제
     *
     * @param roommateId    게시글 id
     */
    @DeleteMapping
    @UserAuth
    public void delete(AppAuthentication auth,
                       @Valid @RequestParam Long roommateId) {
        userService.isDkuChecked(auth.getUserId());
        roommateService.delete(auth.getUserId(), auth.isAdmin(), roommateId);
    }

    /**
     * 내가 참여한 구해줘! 룸메 게시글 중, 리뷰 작성이 가능한 게시글 목록 조회
     *
     * @param pageable 페이징 size, sort, page
     * @return         페이징된 리뷰 작성이 가능한 구해줘! 룸메 게시글 목록 조회
     */
    @GetMapping("/my/possible/review")
    @UserAuth
    public ResponsePage<SummarizedRoommatePossibleReviewDto> listPossibleReviewPosts(AppAuthentication auth,
                                                                                     @ParameterObject Pageable pageable) {
        userService.isDkuChecked(auth.getUserId());
        Page<SummarizedRoommatePossibleReviewDto> list = roommateService.listMyPossibleReviewPosts(auth.getUserId(), pageable);
        return new ResponsePage<>(list);
    }
}
