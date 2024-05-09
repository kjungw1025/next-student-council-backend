package com.dku.council.domain.ticket.repository;

import com.dku.council.domain.ticket.model.entity.TicketEvent;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TicketEventRepository extends JpaRepository<TicketEvent, Long> {

    @Query("select tc from TicketEvent tc where tc.name =:name")
    Optional<TicketEvent> findByName(@Param("name") String name);
}
