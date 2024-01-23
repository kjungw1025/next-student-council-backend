package com.dku.council.domain.chat.repository;

import com.dku.council.domain.chat.model.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    @Query("select u.participant.nickname from ChatRoomUser u " +
            "join ChatRoom r " +
            "on u.chatRoom.id = r.id " +
            "where r.roomId = :roomId")
    List<String> findChatRoomUserByRoomId(@Param("roomId") String roomId);

    @Modifying
    @Query(value = "delete from chat_room_user c " +
            "where c.chat_room_id = :roomId and c.participant_id = :userId", nativeQuery = true)
    void deleteChatRoomUserByRoomIdAndUserId(@Param("roomId") Long roomId,
                                             @Param("userId") Long userId);
}
