
package com.progsoftaplic.TrabalhoFinal.service;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Camada de Serviço - Lógica de Negócio do Sistema de Estacionamento
 * 
 * Esta classe implementa as regras de negócio do sistema, incluindo:
 * - Emissão de tickets
 * - Cálculo de valores
 * - Validação de saída
 * - Processamento de pagamentos
 * - Geração de relatórios
 */
@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    // Constantes para regras de negócio
    private static final int MINUTOS_CORTESIA = 15;
    private static final BigDecimal VALOR_PRIMEIRA_HORA = new BigDecimal("5.00");
    private static final BigDecimal VALOR_HORA_ADICIONAL = new BigDecimal("4.50");
    private static final int MINUTOS_POR_HORA = 60;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Cria um novo ticket de estacionamento para um veículo
     */
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

    /**
     * Valida se um ticket pode ser usado para saída
     */
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

    /**
     * Calcula o valor a ser pago por um ticket
     */
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
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar pagamento: " + e.getMessage(), e);
        }
    }

    /**
     * Calcula o total recebido em um período
     */
    @Transactional(readOnly = true)
    public BigDecimal totalRecebido(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }

        try {
            List<Ticket> pagos = ticketRepository.findByPagoTrueAndSaidaBetween(inicio, fim);
            return pagos.stream()
                    .map(Ticket::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao calcular total recebido: " + e.getMessage(), e);
        }
    }

    /**
     * Conta a quantidade de tickets pagos em um período
     */
    @Transactional(readOnly = true)
    public long quantidadeTicketsPagos(LocalDateTime inicio, LocalDateTime fim) {
        if (inicio == null || fim == null) {
            throw new IllegalArgumentException("Datas de início e fim não podem ser nulas");
        }
        
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
        }

        try {
            return ticketRepository.findByPagoTrueAndSaidaBetween(inicio, fim).size();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao contar tickets pagos: " + e.getMessage(), e);
        }
    }

    /**
     * Busca ticket por código
     */
    @Transactional(readOnly = true)
    public Optional<Ticket> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return ticketRepository.findById(codigo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar ticket: " + e.getMessage(), e);
        }
    }
}
