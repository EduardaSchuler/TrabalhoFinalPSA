package com.progsoftaplic.TrabalhoFinal.controller;

import org.springframework.web.bind.annotation.*;
import com.estac.domain.Ticket;
import com.estac.repository.TicketRepository;


import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/entrada")
public class EstacionamentoController {

private final TicketRepository ticketRepo;

    public EstacionamentoController(TicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
        }

    @PostMapping("/gerar-ticket")
    public Ticket gerarTicket(@RequestParam String placa) {
        Ticket t = new Ticket();
        t.setCodigo(UUID.randomUUID().toString());
        t.setPlaca(placa);
        t.setEntrada(LocalDateTime.now());
        t.setLiberado(false);
        return ticketRepo.save(t);
    }
}