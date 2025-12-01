
package com.progsoftaplic.TrabalhoFinal.service;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;
import com.progsoftaplic.TrabalhoFinal.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            if (minutos <= MINUTOS_CORTESIA) {
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


    public BigDecimal calcularValor(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do ticket não pode ser nulo ou vazio");
        }

        try {
            Optional<Ticket> optTicket = ticketRepository.findById(codigo);
            if (optTicket.isEmpty()) {
                throw new IllegalArgumentException("Ticket não encontrado");
            }

            Ticket ticket = optTicket.get();
            LocalDateTime agora = LocalDateTime.now();
            long minutos = Duration.between(ticket.getEntrada(), agora).toMinutes();

            return calcularValorPorMinutos(minutos);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular valor: " + e.getMessage(), e);
        }
    }

    /**
     * Calcula valor baseado no tempo em minutos
     */
    private BigDecimal calcularValorPorMinutos(long minutos) {
        if (minutos <= MINUTOS_CORTESIA) {
            return BigDecimal.ZERO; // Período de cortesia
        } else if (minutos <= MINUTOS_POR_HORA) {
            return VALOR_PRIMEIRA_HORA; // Primeira hora
        } else {
            // Calcula horas adicionais (cada fração conta como hora completa)
            long horasExtras = (minutos - MINUTOS_POR_HORA + MINUTOS_POR_HORA - 1) / MINUTOS_POR_HORA;
            BigDecimal valorExtra = VALOR_HORA_ADICIONAL.multiply(BigDecimal.valueOf(horasExtras));
            return VALOR_PRIMEIRA_HORA.add(valorExtra).setScale(2, RoundingMode.HALF_UP);
        }
    }

    /**
     * Processa o pagamento de um ticket
     */
    public boolean pagarTicket(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do ticket não pode ser nulo ou vazio");
        }

        try {
            Optional<Ticket> optTicket = ticketRepository.findById(codigo);
            if (optTicket.isEmpty()) {
                throw new IllegalArgumentException("Ticket não encontrado");
            }

            Ticket ticket = optTicket.get();
            
            if (ticket.isPago()) {
                return true; // Já foi pago
            }

            BigDecimal valor = calcularValor(codigo);
            ticket.marcarComoPago(valor);
            
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

    /**
     * Conta a quantidade de tickets pagos em um período
     */
    @Transactional(readOnly = true)
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
