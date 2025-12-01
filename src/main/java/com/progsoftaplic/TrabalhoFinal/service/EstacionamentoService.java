package com.progsoftaplic.TrabalhoFinal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estac.domain.Ticket;
import com.estac.domain.Pagamento;
import com.estac.repository.TicketRepository;
import com.estac.repository.PagamentoRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class EstacionamentoService {

    private final TicketRepository ticketRepo;
    private final PagamentoRepository pagamentoRepo;

    public EstacionamentoService(TicketRepository ticketRepo, PagamentoRepository pagamentoRepo) {
    this.ticketRepo = ticketRepo;
    this.pagamentoRepo = pagamentoRepo;
    }

    public double calcularValor(Ticket ticket, LocalDateTime referencia) {
        Duration dur = Duration.between(ticket.getEntrada(), referencia);
        long minutos = dur.toMinutes();

        if (minutos <= 15) return 0.0;

        if (minutos <= 60) return 5.0;

        long horasExtras = (long) Math.ceil((minutos - 60) / 60.0);
        return 5.0 + horasExtras * 4.5;
    }


    @Transactional
    public Pagamento registrarPagamento(String codigo, double valorPago) {
        Ticket ticket = ticketRepo.findByCodigo(codigo).orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
        ticket.setPagamento(LocalDateTime.now());
        ticket.setLiberado(true);
        ticketRepo.save(ticket);

        Pagamento p = new Pagamento();
        p.setTicket(ticket);
        p.setValor(valorPago);
        p.setDataPagamento(LocalDateTime.now());
        return pagamentoRepo.save(p);
    }


    public Double totalPorDia(LocalDate dia) { return pagamentoRepo.totalPorDia(dia); }
    public Double totalPorMes(int mes, int ano) { return pagamentoRepo.totalPorMes(mes, ano); }
    public Integer ticketsPorDia(LocalDate dia) { return pagamentoRepo.ticketsPorDia(dia); }
    public Integer ticketsPorMes(int mes, int ano) { return pagamentoRepo.ticketsPorMes(mes, ano); }
}