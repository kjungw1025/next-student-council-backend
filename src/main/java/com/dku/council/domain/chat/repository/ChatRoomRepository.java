package com.dku.council.domain.chat.repository;

import com.dku.council.domain.chat.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "select * " +
                    "from chat_room c " +
                    "where c.chat_room_status = 'ACTIVE' " +
                    "order by c.created_at DESC", nativeQuery = true)
    List<ChatRoom> findAllChatRoom();

    @Query("select c from ChatRoom c " +
            "where c.roomId = :roomId and c.chatRoomStatus = 'ACTIVE' ")
    Optional<ChatRoom> findChatRoomByRoomId(@Param("roomId") String roomId);

    @Query("select c from ChatRoom c " +
            "where c.roomId = :roomId and c.roomManager.id = :userId ")
    Optional<ChatRoom> checkChatRoomManagerByUserId(@Param("roomId") String roomId,
                                                    @Param("userId") Long userId);

    /**
     * 게시글과 채팅방이 1 : 1로 생성 되는 단터디, 베어이츠, 단혼밥에서 사용하는 메소드
     */
    @Query("select c from ChatRoom c " +
            "where c.withDankook.id = :withDankookId and c.chatRoomStatus = 'ACTIVE' ")
    Optional<ChatRoom> findChatRoomByWithDankookId(@Param("withDankookId") Long withDankookId);
}
