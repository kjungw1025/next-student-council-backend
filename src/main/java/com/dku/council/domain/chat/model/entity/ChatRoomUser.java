package com.dku.council.domain.chat.model.entity;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.global.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "chat_room_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomUser extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "chat_room_user_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "participant_id")
    private User participant;

    @Builder
    public ChatRoomUser(ChatRoom chatRoom, User user) {
        this.participant = user;
        this.chatRoom = chatRoom;
    }
}
