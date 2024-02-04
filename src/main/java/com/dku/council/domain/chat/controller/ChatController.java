package com.dku.council.domain.chat.controller;

import com.dku.council.domain.chat.model.FileType;
import com.dku.council.domain.chat.model.MessageType;
import com.dku.council.domain.chat.model.dto.Message;
import com.dku.council.domain.chat.model.dto.request.RequestChatDto;
import com.dku.council.domain.chat.model.dto.response.ResponseChatDto;
import com.dku.council.domain.chat.service.ChatService;
import com.dku.council.domain.chat.service.MessageSender;
import com.dku.council.domain.chatmessage.model.entity.ChatRoomMessage;
import com.dku.council.domain.chatmessage.service.ChatRoomMessageService;
import com.dku.council.domain.user.service.UserService;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.infra.fcm.service.FirebaseCloudMessageService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "채팅", description = "채팅 송/수신 관련 api")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    /**
     * 아래에서 사용되는 convertAndSend 를 사용하기 위한 선언
     *  convertAndSend 는 객체를 인자로 넘겨주면 자동으로 Message 객체로 변환 후 도착지로 전송한다.
     */
    private final SimpMessageSendingOperations template;

    private final UserService userService;
    private final ChatService chatService;
    private final ChatRoomMessageService chatRoomMessageService;
    private final MessageSender sender;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    /**
     * 채팅방 별, 입장 이벤트 발생시 처리되는 기능
     *
     * @param chat  채팅으로 보낼 메시지 정보 관련 param
     *
     * MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
     * 이때 클라이언트에서는 /pub/chat/sendMessage 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
     * 처리가 완료되면 /sub/chatRoom/enter/roomId 로 메시지가 전송된다.
     */
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload RequestChatDto chat,
                          SimpMessageHeaderAccessor headerAccessor) {

        if(chatService.alreadyInRoom(chat.getRoomId(), chat.getUserId())) {
            // 반환 결과를 socket session 에 userUUID 로 저장
            headerAccessor.getSessionAttributes().put("username", chat.getSender());
            headerAccessor.getSessionAttributes().put("userId", chat.getUserId());
            headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());
        } else {
            // 채팅방 유저+1
            chatService.plusUserCnt(chat.getRoomId());

            // 채팅방에 유저 추가 및 UserUUID 반환
            String username = chatService.addUser(chat.getRoomId(), chat.getSender());

            // 반환 결과를 socket session 에 userUUID 로 저장
            headerAccessor.getSessionAttributes().put("username", username);
            headerAccessor.getSessionAttributes().put("userId", chat.getUserId());
            headerAccessor.getSessionAttributes().put("roomId", chat.getRoomId());

            String enterMessage = chat.getSender() + " 님 입장!!";
            LocalDateTime messageTime = LocalDateTime.now();

            // 입장 메시지 저장
            chatRoomMessageService.create(chat.getRoomId(), chat.getType().toString(), chat.getUserId(), chat.getSender(), enterMessage, messageTime, "", "", chat.getFileType().toString());

            Message message = Message.builder()
                    .type(chat.getType())
                    .roomId(chat.getRoomId())
                    .sender(chat.getSender())
                    .message(enterMessage)
                    .messageTime(messageTime)
                    .fileType(chat.getFileType())
                    .build();

            sender.send(topic, message);
        }
    }

    /**
     * 채팅방 별, 채팅 메시지 전송 기능
     *
     * @param chat  채팅으로 보낼 메시지 정보 관련 param
     */
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload RequestChatDto chat) {
        LocalDateTime messageTime = LocalDateTime.now();

        chatRoomMessageService.create(chat.getRoomId(), chat.getType().toString(), chat.getUserId(), chat.getSender(), chat.getMessage(), messageTime, chat.getFileName(), chat.getFileUrl(), chat.getFileType().toString());

        Message message = Message.builder()
                .type(chat.getType())
                .roomId(chat.getRoomId())
                .sender(chat.getSender())
                .message(chat.getMessage())
                .messageTime(messageTime)
                .fileName(chat.getFileName())
                .fileUrl(chat.getFileUrl())
                .fileType(chat.getFileType())
                .build();

        sender.send(topic, message);
    }

    /**
     * 채팅방 별, 퇴장 이벤트 발생시 처리되는 기능
     *
     * 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
     */
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // stomp 세션에 있던 username과 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//        log.info("퇴장 controller에서 uuid " + username);
//        log.info("퇴장 controller에서 roomId " + roomId);
//
//        log.info("headAccessor {}", headerAccessor);
//
//        // 채팅방 유저 -1
//        chatService.minusUserCnt(roomId);
//
//        // 채팅방 유저 리스트에서 유저 삭제
//        chatService.delUser(roomId, username);
//
//        if (username != null) {
//            log.info("User Disconnected : ", username);
//
//            String exitMessage = username + " 님 퇴장!!";
//            LocalDateTime messageTime = LocalDateTime.now();
//
//            // 퇴장 메시지 저장
//            chatRoomMessageService.create(roomId, MessageType.LEAVE.toString(), userId, username, exitMessage, messageTime);
//            // builder 어노테이션 활용
//            Message message = Message.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .roomId(roomId)
//                    .message(exitMessage)
//                    .messageTime(messageTime)
//                    .build();
//
//            sender.send(topic, message);
//        }
//    }

    /**
     * 채팅방 별, 채팅에 참여한 유저 리스트 반환
     *
     * @param roomId    채팅방 id
     */
    @GetMapping("/chat/userlist")
    @UserAuth
    @ResponseBody
    public List<String> userList(String roomId) {
        return chatService.getUserList(roomId);
    }

    /**
     * 채팅방 별, 이전에 나눈 채팅 메시지 리스트 반환
     *
     * @param roomId    채팅방 id
     */
    @GetMapping("/chat/message/list")
    @UserAuth
    @ResponseBody
    public List<ChatRoomMessage> list(@RequestParam("roomId") String roomId) {
        return chatRoomMessageService.findAllChatRoomMessages(roomId);
    }
}