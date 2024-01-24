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
            "where c.roomId = :roomId and c.roomPwd = :roomPwd ")
    Optional<ChatRoom> checkChatRoomByRoomPwd(@Param("roomId") String roomId,
                                  @Param("roomPwd") String roomPwd);
}
