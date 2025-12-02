package com.progsoftaplic.TrabalhoFinal.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.PagamentoRepository;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;

/**
 * Carrega dados de teste na inicialização da aplicação
 */
@Component
public class DadosTesteLoader implements CommandLineRunner {

    private final TicketRepository ticketRepository;
    private final PagamentoRepository pagamentoRepository;

    public DadosTesteLoader(TicketRepository ticketRepository, PagamentoRepository pagamentoRepository) {
        this.ticketRepository = ticketRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem dados
        if (ticketRepository.count() > 0) {
            return; // Dados já existem, não criar novamente
        }

        criarDadosTeste();
    }

    private void criarDadosTeste() {
        try {
            // Tickets de hoje (01/12/2025)
            criarTicketComPagamento("TK001", "ABC1234", 
                    LocalDateTime.of(2025, 12, 1, 8, 0), 
                    LocalDateTime.of(2025, 12, 1, 10, 30), 
                    LocalDateTime.of(2025, 12, 1, 10, 25),
                    new BigDecimal("5.00"));

            criarTicketComPagamento("TK002", "DEF5678", 
                    LocalDateTime.of(2025, 12, 1, 9, 15), 
                    LocalDateTime.of(2025, 12, 1, 12, 45), 
                    LocalDateTime.of(2025, 12, 1, 12, 40),
                    new BigDecimal("14.50"));

            criarTicketSemPagamento("TK003", "GHI9012", 
                    LocalDateTime.of(2025, 12, 1, 14, 20));

            criarTicketComPagamento("TK004", "JKL3456", 
                    LocalDateTime.of(2025, 12, 1, 7, 30), 
                    LocalDateTime.of(2025, 12, 1, 18, 0), 
                    LocalDateTime.of(2025, 12, 1, 17, 55),
                    new BigDecimal("52.50"));

            criarTicketSemPagamento("TK005", "MNO7890", 
                    LocalDateTime.of(2025, 12, 1, 16, 45));

            // Tickets de ontem (30/11/2025)
            criarTicketComPagamento("TK006", "PQR1122", 
                    LocalDateTime.of(2025, 11, 30, 10, 0), 
                    LocalDateTime.of(2025, 11, 30, 11, 15), 
                    LocalDateTime.of(2025, 11, 30, 11, 10),
                    new BigDecimal("5.00"));

            criarTicketComPagamento("TK007", "STU3344", 
                    LocalDateTime.of(2025, 11, 30, 13, 30), 
                    LocalDateTime.of(2025, 11, 30, 16, 20), 
                    LocalDateTime.of(2025, 11, 30, 16, 15),
                    new BigDecimal("9.50"));

            // Tickets de dias anteriores
            criarTicketComPagamento("TK008", "VWX5566", 
                    LocalDateTime.of(2025, 11, 29, 8, 15), 
                    LocalDateTime.of(2025, 11, 29, 20, 30), 
                    LocalDateTime.of(2025, 11, 29, 20, 25),
                    new BigDecimal("60.00"));

            criarTicketComPagamento("TK009", "YZA7788", 
                    LocalDateTime.of(2025, 11, 29, 11, 45), 
                    LocalDateTime.of(2025, 11, 29, 13, 0), 
                    LocalDateTime.of(2025, 11, 29, 12, 55),
                    new BigDecimal("5.00"));

            criarTicketComPagamento("TK010", "BCD9900", 
                    LocalDateTime.of(2025, 11, 28, 15, 20), 
                    LocalDateTime.of(2025, 11, 28, 17, 55), 
                    LocalDateTime.of(2025, 11, 28, 17, 50),
                    new BigDecimal("9.50"));

            System.out.println("=== DADOS DE TESTE CARREGADOS ===");
            System.out.println("Total de tickets: " + ticketRepository.count());
            System.out.println("Total de pagamentos: " + pagamentoRepository.count());
            System.out.println("================================");

        } catch (Exception e) {
            System.err.println("Erro ao carregar dados de teste: " + e.getMessage());
        }
    }

    private void criarTicketComPagamento(String codigo, String placa, LocalDateTime entrada, 
                                        LocalDateTime saida, LocalDateTime dataPagamento, BigDecimal valor) {
        // Criar ticket
        Ticket ticket = new Ticket(codigo, placa, entrada);
        ticket.setSaida(saida);
        ticket.setPago(true);
        ticket.setValor(valor);
        ticketRepository.save(ticket);

        // Criar pagamento
        Pagamento pagamento = new Pagamento(codigo, valor);
        // Usar reflection para definir a data do pagamento
        try {
            java.lang.reflect.Field field = Pagamento.class.getDeclaredField("dataPagamento");
            field.setAccessible(true);
            field.set(pagamento, dataPagamento);
        } catch (Exception e) {
            // Se não conseguir, usar a data atual (melhor que nenhum pagamento)
        }
        pagamentoRepository.save(pagamento);
    }

    private void criarTicketSemPagamento(String codigo, String placa, LocalDateTime entrada) {
        Ticket ticket = new Ticket(codigo, placa, entrada);
        ticket.setPago(false);
        ticket.setValor(BigDecimal.ZERO);
        ticketRepository.save(ticket);
    }
}