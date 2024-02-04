package com.dku.council.domain.chat.controller;

import com.dku.council.domain.chat.model.dto.response.ResponseChatRoomDto;
import com.dku.council.domain.chat.service.ChatFileService;
import com.dku.council.domain.chat.service.ChatService;
import com.dku.council.domain.chatmessage.service.ChatRoomMessageService;
import com.dku.council.domain.user.model.dto.response.ResponseUserInfoForChattingDto;
import com.dku.council.domain.user.service.UserService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Tag(name = "채팅방", description = "채팅방 관련 api")
@Controller
@RequestMapping("/chatRoom")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final UserService userService;
    private final ChatService chatService;
    private final ChatRoomMessageService chatRoomMessageService;
    private final ChatFileService chatFileService;

    /**
     * 채팅방 리스트 화면
     */
    @GetMapping
    @UserAuth
    public String goChatRoom(Model model, AppAuthentication auth) {
        ResponseUserInfoForChattingDto responseUserInfoForChattingDto = userService.getUserInfoForChatting(auth.getUserId());

        model.addAttribute("list", chatService.findAllRoom());
        model.addAttribute("user", responseUserInfoForChattingDto);

        log.info("SHOW ALL ChatList {}", chatService.findAllRoom());

        return "page/chatting/roomlist";
    }

    /**
     * 채팅방 생성
     *
     * @param name              채팅방 이름
     * @param roomPwd           채팅방 비밀번호
     * @param secretCheck       채팅방 잠금 설정 여부
     * @param maxUserCount      채팅방 최대 인원 수 설정 (default = 10)
     */
    @PostMapping("/create")
    @UserAuth
    public String createRoom(@RequestParam("roomName") String name,
                             @RequestParam("roomPwd") String roomPwd,
                             @RequestParam("secretChk") String secretCheck,
                             @RequestParam(value = "maxUserCount", defaultValue = "10") String maxUserCount,
                             AppAuthentication auth,
                             RedirectAttributes rttr) {

        ResponseChatRoomDto room = chatService.createChatRoom(name,
                roomPwd,
                Boolean.parseBoolean(secretCheck),
                Integer.parseInt(maxUserCount),
                auth.getUserId());

        log.info("CREATE Chat Room [{}]", room);

        rttr.addFlashAttribute("roomName", room);
        return "redirect:/chatRoom";
    }


    // 채팅방 입장 화면
    // 파라미터로 넘어오는 roomId 를 확인후 해당 roomId 를 기준으로
    // 채팅방을 찾아서 클라이언트를 chatroom 으로 보낸다.
    @GetMapping("/enter")
    @UserAuth
    public String roomDetail(Model model, String roomId, AppAuthentication auth){

        log.info("/chatRoom/enter : roomId {}", roomId);

        model.addAttribute("user", userService.getUserInfoForChatting(auth.getUserId()));
        model.addAttribute("room", chatService.findRoomById(roomId));

        return "page/chatting/chatroom";
    }

    /**
     * 채팅방 비밀번호 확인
     *
     * @param roomId    채팅방 id
     * @param roomPwd   사용자가 입력한 비밀번호
     * @return          사용자가 입력한 비밀번호가 일치하면 true, 아니면 false
     */
    @PostMapping("/confirmPwd/{roomId}")
    @UserAuth
    @ResponseBody
    public boolean confirmPwd(@PathVariable String roomId,
                              @RequestParam String roomPwd){

        return chatService.confirmPwd(roomId, roomPwd);
    }

    /**
     * 채팅방 삭제
     *
     * @param roomId        채팅방 id
     */
    @DeleteMapping
    @UserAuth
    public String delChatRoom(@RequestParam String roomId, AppAuthentication auth){

        // 해당 채팅방에 존재하는 파일들 삭제
        chatFileService.deleteAllFilesInChatRoom(roomId);
        
        // 해당 채팅방에 존재하는 채팅 메시지들 삭제
        chatRoomMessageService.deleteChatRoomMessages(roomId);

        // roomId(UUID 값) 기준으로 채팅방 삭제
        chatService.delChatRoom(auth.getUserId(), roomId, auth.isAdmin());

        return "redirect:/chatRoom";
    }

    /**
     * maxUserCount에 따른 채팅방 입장 여부
     *
     * @param roomId        채팅방 Id
     * @return              true/false
     */
    @GetMapping("/chkUserCnt/{roomId}")
    @ResponseBody
    public boolean chkUserCnt(@PathVariable String roomId){

        return chatService.chkRoomUserCnt(roomId);
    }
}