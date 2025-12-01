
package com.progsoftaplic.TrabalhoFinal.repository;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    List<Ticket> findByPagoTrueAndSaidaBetween(LocalDateTime inicio, LocalDateTime fim);
    List<Ticket> findByPagoTrueAndSaidaBetweenOrderBySaidaAsc(LocalDateTime inicio, LocalDateTime fim);
    List<Ticket> findByPlaca(String placa);
}
