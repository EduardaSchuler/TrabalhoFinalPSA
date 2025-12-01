
package com.progsoftaplic.TrabalhoFinal.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.PagamentoRepository;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PagamentoRepository pagamentoRepository;

    public TicketService(TicketRepository ticketRepository, PagamentoRepository pagamentoRepository) {
        this.ticketRepository = ticketRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    public Ticket criarTicket(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa não pode ser nula ou vazia");
        }

        try {
            String codigo = UUID.randomUUID().toString();
            Ticket ticket = new Ticket(codigo, placa.trim().toUpperCase(), LocalDateTime.now());
            
            return ticketRepository.save(ticket);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar ticket: " + e.getMessage(), e);
        }
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

        public boolean validarSaida(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do ticket não pode ser nulo ou vazio");
        }

        try {
            Optional<Ticket> optTicket = ticketRepository.findById(codigo);
            if (optTicket.isEmpty()) {
                return false;
            }

            Ticket ticket = optTicket.get();
            LocalDateTime agora = LocalDateTime.now();
            
            // Registra horário de saída se ainda não foi registrado
            if (ticket.getSaida() == null) {
                ticket.setSaida(agora);
            }

            // Verifica período de cortesia
            long minutos = Duration.between(ticket.getEntrada(), agora).toMinutes();
            if (minutos <= 15) {
                ticket.setPago(true);
                ticket.setValor(BigDecimal.ZERO);
                ticketRepository.save(ticket);
                return true;
            }

            // Verifica se já foi pago
            boolean liberado = ticket.isPago();
            ticketRepository.save(ticket);
            return liberado;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao validar saída: " + e.getMessage(), e);
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
