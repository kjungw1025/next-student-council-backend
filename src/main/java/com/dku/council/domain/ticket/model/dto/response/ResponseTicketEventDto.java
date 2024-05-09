package com.dku.council.domain.ticket.model.dto.response;

import com.dku.council.domain.ticket.model.entity.TicketEvent;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseTicketEventDto {

    private final String name;

    private final LocalDateTime startAt;

    private final LocalDateTime endAt;

    private final int totalTickets;

    public ResponseTicketEventDto(TicketEvent ticketEvent) {
        this.name = ticketEvent.getName();
        this.startAt = ticketEvent.getStartAt();
        this.endAt = ticketEvent.getEndAt();
        this.totalTickets = ticketEvent.getTotalTickets();
    }
}
