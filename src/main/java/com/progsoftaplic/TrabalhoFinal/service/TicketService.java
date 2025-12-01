
package com.progsoftaplic.TrabalhoFinal.service;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;
import com.progsoftaplic.TrabalhoFinal.repository.PagamentoRepository;
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
    private final PagamentoRepository pagamentoRepository;

    public TicketService(TicketRepository ticketRepository, PagamentoRepository pagamentoRepository) {
        this.ticketRepository = ticketRepository;
        this.pagamentoRepository = pagamentoRepository;
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

        // Se não for cortesia, só libera se já estiver pago
        ticketRepository.save(ticket);
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

        // Registrar pagamento
        Pagamento pagamento = new Pagamento(codigo, valor);
        pagamentoRepository.save(pagamento);

        return true;
    }

    public BigDecimal totalRecebido(LocalDateTime inicio, LocalDateTime fim) {
        List<Pagamento> pagamentos = pagamentoRepository.findByDataPagamentoBetween(inicio, fim);
        return pagamentos.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long quantidadeTicketsPagos(LocalDateTime inicio, LocalDateTime fim) {
        return pagamentoRepository.findByDataPagamentoBetween(inicio, fim).size();
    }
    
        
    public Optional<Ticket> buscarPorCodigo(String codigo) {
        return ticketRepository.findById(codigo);
    }

    public Pagamento pagarETrazerPagamento(String codigo) {
        Optional<Ticket> optTicket = ticketRepository.findById(codigo);
        if (optTicket.isEmpty()) return null;

        Ticket ticket = optTicket.get();
        BigDecimal valor = calcularValor(codigo);
        ticket.setValor(valor);
        ticket.setPago(true);
        ticketRepository.save(ticket);

        Pagamento pagamento = new Pagamento(codigo, valor);
        return pagamentoRepository.save(pagamento);
    }
}
