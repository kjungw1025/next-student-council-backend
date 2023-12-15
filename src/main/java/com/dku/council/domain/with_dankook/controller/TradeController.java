package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateTradeDto;
import com.dku.council.domain.with_dankook.service.TradeService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "단국 거래 게시판", description = "단국 거래 게시판 API")
@RestController
@RequestMapping("with-dankook/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

//    /**
//     * 단국 거래 게시글 작성
//     */
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @UserAuth
//    public ResponseIdDto create(AppAuthentication auth,
//                                @Valid @ModelAttribute RequestCreateTradeDto dto) {
//        Long id = tradeService.create(auth.getUserId(), dto);
//        return new ResponseIdDto(id);
//    }
}
