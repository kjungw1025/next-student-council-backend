package com.dku.council.domain.review.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.review.exception.AlreadyWrittenReviewException;
import com.dku.council.domain.review.exception.InvalidCreateReviewToMyselfException;
import com.dku.council.domain.review.model.dto.request.RequestCreateReviewDto;
import com.dku.council.domain.review.model.dto.response.ResponseReviewPositiveCountDto;
import com.dku.council.domain.review.service.ReviewService;
import com.dku.council.domain.user.service.UserService;
import com.dku.council.domain.with_dankook.exception.InvalidStatusException;
import com.dku.council.domain.with_dankook.exception.WithDankookUserNotFoundException;
import com.dku.council.domain.with_dankook.model.entity.WithDankook;
import com.dku.council.domain.with_dankook.service.WithDankookService;
import com.dku.council.domain.with_dankook.service.WithDankookUserService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Tag(name = "리뷰", description = "with-dankook 게시판들에 대한 리뷰 작성/조회 API")
@RestController
@RequestMapping("with-dankook/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;

    /**
     * 모집이 완료된 with-dankook 게시판들에 대한 리뷰 작성
     *
     * @param dto 작성한 리뷰 내용 dto
     *
     * <p>
     *     Request Body에 포함되는 reviewList는 아래 링크를 참고해주세요.</br>
     *     https://www.notion.so/With-dankook-Review-a8c6b5bb322b417098fcfff50406473f
     * </p>
     */
    @PostMapping
    @UserAuth
    public void create(AppAuthentication auth,
                                @Valid @RequestBody RequestCreateReviewDto dto) {
        userService.isDkuChecked(auth.getUserId());
        reviewService.create(auth.getUserId(), dto);
    }

    /**
     * with-dankook 타입에 해당하는 리뷰 Top3 조회
     *
     * @param withDankookType   조회하고자 하는 with-dankook 타입을 영어로 입력
     * <p>
     *     <b>withDankookType Parameter 입력시 참고해주세요.</b></br>
     *     단혼밥 : EatingAlong</br>
     *     단터디 : Study</br>
     *     단국 거래 : Trade</br>
     *     베어이츠 : BearEats</br>
     *     구해줘 룸메 : Dormitory</br>
     * </p>
     *
     * @return                  페이징된 with-dankook 타입에 해당하는 리뷰 Top3 조회
     */
    @GetMapping("/rank")
    @UserAuth
    public ResponsePage<ResponseReviewPositiveCountDto> listTop3Review(AppAuthentication auth,
                                                                       @Valid @RequestParam String withDankookType) {
        userService.isDkuChecked(auth.getUserId());
        Page<ResponseReviewPositiveCountDto> list = reviewService.rank(withDankookType, Pageable.ofSize(3));
        return new ResponsePage<>(list);
    }

    /**
     * With-Dankook 타입 별, 특정 사용자가 받은 모든 '추천해요!' 리뷰 내역 상세 조회
     *
     * @param withDankookType   조회하고자 하는 with-dankook 타입을 영어로 입력
     * <p>
     *     <b>withDankookType Parameter 입력시 참고해주세요.</b></br>
     *     단혼밥 : EatingAlong</br>
     *     단터디 : Study</br>
     *     단국 거래 : Trade</br>
     *     베어이츠 : BearEats</br>
     *     구해줘 룸메 : Dormitory</br>
     * </p>
     *
     * @return                  '추천해요!'를 받은 개수, 리뷰 내역
     */
    @GetMapping("/received/positive")
    @UserAuth
    public Map<String, Integer> AllReceivedPositiveReview(AppAuthentication auth,
                                                          @Valid @RequestParam String withDankookType) {
        userService.isDkuChecked(auth.getUserId());
        return reviewService.findAllUserPositiveReviewDetail(withDankookType, auth.getUserId());
    }

    /**
     * With-Dankook 타입 별, 특정 사용자가 받은 모든 '아쉬워요!' 리뷰 내역 상세 조회
     *
     * @param withDankookType   조회하고자 하는 with-dankook 타입을 영어로 입력
     * <p>
     *     <b>withDankookType Parameter 입력시 참고해주세요.</b></br>
     *     단혼밥 : EatingAlong</br>
     *     단터디 : Study</br>
     *     단국 거래 : Trade</br>
     *     베어이츠 : BearEats</br>
     *     구해줘 룸메 : Dormitory</br>
     * </p>
     *
     * @return                  '아쉬워요!'를 받은 개수, 리뷰 내역
     */
    @GetMapping("/received/negative")
    @UserAuth
    public Map<String, Integer> AllReceivedNegativeReview(AppAuthentication auth,
                                                          @Valid @RequestParam String withDankookType) {
        userService.isDkuChecked(auth.getUserId());
        return reviewService.findAllUserNegativeReviewDetail(withDankookType, auth.getUserId());
    }
}
