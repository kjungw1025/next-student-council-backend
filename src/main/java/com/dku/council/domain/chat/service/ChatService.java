package com.dku.council.domain.chat.service;

import com.dku.council.domain.chat.exception.ChatRoomNotFoundException;
import com.dku.council.domain.chat.model.dto.response.ResponseChatRoomDto;
import com.dku.council.domain.chat.model.entity.ChatRoom;
import com.dku.council.domain.chat.model.entity.ChatRoomUser;
import com.dku.council.domain.chat.repository.ChatRoomRepository;
import com.dku.council.domain.chat.repository.ChatRoomUserRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserRepository userRepository;

    /**
     * 전체 채팅방 조회
     *
     * @return      각 채팅방 정보 list
     */
    public List<ResponseChatRoomDto> findAllRoom() {
        List<ChatRoom> roomList = chatRoomRepository.findAllChatRoom();

        List<ResponseChatRoomDto> responseChatRoomDtos = new ArrayList<>();
        if (!roomList.isEmpty()) {
            for (ChatRoom chatRoom : roomList) {
                ResponseChatRoomDto responseChatRoomDto = new ResponseChatRoomDto(chatRoom.getRoomId(),
                        chatRoom.getRoomName(),
                        chatRoom.getUserCount(),
                        chatRoom.getMaxUserCount(),
                        chatRoom.getRoomPwd(),
                        chatRoom.isSecretCheck());
                responseChatRoomDtos.add(responseChatRoomDto);
            }
        }

        return responseChatRoomDtos;
    }

    /**
     * roomID를 기준으로 채팅방 찾기
     *
     * @param roomId    찾고자 하는 채팅방 id
     * @return          채팅방 정보
     */
    public ResponseChatRoomDto findRoomById(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        return new ResponseChatRoomDto(chatRoom.getRoomId(),
                chatRoom.getRoomName(),
                chatRoom.getUserCount(),
                chatRoom.getMaxUserCount(),
                chatRoom.getRoomPwd(),
                chatRoom.isSecretCheck());
    }

    /**
     * 채팅방 생성
     *
     * @param roomName          생성할 채팅방의 이름
     * @param roomPwd           생성항 채팅방의 비밀번호
     * @param secretCheck       생성할 채팅방의 채팅방 잠금 여부
     * @param maxUserCount      생성할 채팅방의 최대 인원수 제한
     * @param userId            채팅방을 생성하고자 하는 사용자 id
     * @return                  채팅방 정보
     */
    public ResponseChatRoomDto createChatRoom(String roomName, String roomPwd, boolean secretCheck, int maxUserCount, Long userId){
        // roomName 와 roomPwd 로 chatRoom 빌드 후 return

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .roomManager(user) // 채팅방 방장
                .userCount(0) // 채팅방 참여 인원수
                .maxUserCount(maxUserCount) // 최대 인원수 제한
                .roomPwd(roomPwd)  // 채팅방 패스워드
                .secretCheck(secretCheck) // 채팅방 잠금 여부
                .build();
        chatRoomRepository.save(chatRoom);

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoomUserRepository.save(chatRoomUser);

        return new ResponseChatRoomDto(chatRoom.getRoomId(),
                chatRoom.getRoomName(),
                chatRoom.getUserCount(),
                chatRoom.getMaxUserCount(),
                chatRoom.getRoomPwd(),
                chatRoom.isSecretCheck());
    }

    /**
     * 채팅방 인원 +1
     */
    @Transactional
    public void plusUserCnt(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.addUserCount();
    }

    /**
     * 채팅방 인원 -1
     */
    @Transactional
    public void minusUserCnt(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.subUserCount();
    }

    /**
     * maxUserCount 에 따른 채팅방 입장 여부
     */
    public boolean chkRoomUserCnt(String roomId){
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        log.info("참여인원 확인 [{}, {}]", chatRoom.getUserCount(), chatRoom.getMaxUserCount());

        return chatRoom.getUserCount() + 1 <= chatRoom.getMaxUserCount();
    }

    /**
     * 채팅방 유저 리스트에 유저 추가
     */
    @Transactional
    public String addUser(String roomId, String userName){
        User user = userRepository.findByNickname(userName).orElseThrow(UserNotFoundException::new);

        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoomUserRepository.save(chatRoomUser);

        return user.getNickname();
    }

    /**
     * 채팅방 유저 리스트 삭제
     */
    @Transactional
    public void delUser(String roomId, String username){
        User user = userRepository.findByNickname(username).orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        chatRoomUserRepository.deleteChatRoomUserByRoomIdAndUserId(chatRoom.getId(), user.getId());
    }

    /**
     * 채팅방 전체 userList 조회
     *
     * @param roomId    채팅방 id
     */
    public List<String> getUserList(String roomId){
        return chatRoomUserRepository.findChatRoomUserByRoomId(roomId);
    }

    /**
     * 채팅방 비밀번호 확인
     *
     * @param roomId    비밀번호 확인할 채팅방 id
     * @param roomPwd   사용자가 입력한 해당 채팅방의 비밀번호
     *
     * @return          확인이 완료되면 true, 아니면 false
     */
    public boolean confirmPwd(String roomId, String roomPwd) {
        return chatRoomRepository.checkChatRoomByRoomPwd(roomId, roomPwd).isPresent();
    }

    /**
     * 채팅방 삭제 (DB 논리적 삭제)
     *
     * @param userId    삭제 요청한 사용자 id
     * @param roomId    삭제할 채팅방 id
     * @param isAdmin   삭제 요청한 사용자가 관리자인지
     */
    @Transactional
    public void delChatRoom(Long userId, String roomId, boolean isAdmin) {

        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new);
        if (isAdmin) {
            chatRoom.markAsDeleted(true);
        } else if (chatRoom.getRoomManager().getId().equals(userId)) {
            chatRoom.markAsDeleted(false);
        } else {
            throw new NotGrantedException();
        }
        log.info("삭제 완료 roomId : {}", roomId);
    }

    public boolean alreadyInRoom(String roomId, Long userId) {
        long chatRoomId = chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(ChatRoomNotFoundException::new).getId();
        return chatRoomUserRepository.existsUserByRoomIdAndUserId(chatRoomId, userId).isPresent();
    }
}
