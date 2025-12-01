
package com.progsoftaplic.TrabalhoFinal.service;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket criarTicket(String placa) {
        Ticket ticket = new Ticket();
        ticket.setCodigo(UUID.randomUUID().toString());
        ticket.setPlaca(placa);
        ticket.setEntrada(LocalDateTime.now());
        ticket.setPago(false);
        ticket.setValor(BigDecimal.ZERO);
        return ticketRepository.save(ticket);
    }

    public boolean validarSaida(String codigo) {
        Optional<Ticket> optTicket = ticketRepository.findById(codigo);
        if (optTicket.isEmpty()) return false;

        Ticket ticket = optTicket.get();
        LocalDateTime agora = LocalDateTime.now();
        ticket.setSaida(agora);

        // Regra dos 15 minutos de cortesia
        long minutos = Duration.between(ticket.getEntrada(), agora).toMinutes();
        if (minutos <= 15) {
            ticket.setPago(true);
            ticketRepository.save(ticket);
            return true;
        }

        return ticket.isPago(); 
    }

    public BigDecimal calcularValor(String codigo) {
        Optional<Ticket> optTicket = ticketRepository.findById(codigo);
        if (optTicket.isEmpty()) return BigDecimal.ZERO;

        Ticket ticket = optTicket.get();
        LocalDateTime agora = LocalDateTime.now();
        long minutos = Duration.between(ticket.getEntrada(), agora).toMinutes();

        if (minutos <= 15) {
            return BigDecimal.ZERO; // cortesia
        } else if (minutos <= 60) {
            return BigDecimal.valueOf(5.00); // até 1 hora
        } else {
            long horasExtras = (minutos - 60) / 60;
            return BigDecimal.valueOf(5.00 + (horasExtras * 4.50));
        }
    }

    public boolean pagarTicket(String codigo) {
        Optional<Ticket> optTicket = ticketRepository.findById(codigo);
        if (optTicket.isEmpty()) return false;

        Ticket ticket = optTicket.get();
        BigDecimal valor = calcularValor(codigo);
        ticket.setValor(valor);
        ticket.setPago(true);
        ticketRepository.save(ticket);
        return true;
    }

    public BigDecimal totalRecebido(LocalDateTime inicio, LocalDateTime fim) {
        List<Ticket> pagos = ticketRepository.findByPagoTrueAndSaidaBetween(inicio, fim);
        return pagos.stream()
                .map(Ticket::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long quantidadeTicketsPagos(LocalDateTime inicio, LocalDateTime fim) {
        return ticketRepository.findByPagoTrueAndSaidaBetween(inicio, fim).size();
    }
}
