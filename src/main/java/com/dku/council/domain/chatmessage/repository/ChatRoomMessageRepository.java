package com.dku.council.domain.chatmessage.repository;

import com.dku.council.domain.chatmessage.model.ChatRoomMessageId;
import com.dku.council.domain.chatmessage.model.entity.ChatRoomMessage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@EnableScan
@Repository
public interface ChatRoomMessageRepository extends CrudRepository<ChatRoomMessage, ChatRoomMessageId> {
    List<ChatRoomMessage> findAllByRoomIdOrderByCreatedAtAsc(String roomId);

    void deleteAllByRoomId(String roomId);
}
