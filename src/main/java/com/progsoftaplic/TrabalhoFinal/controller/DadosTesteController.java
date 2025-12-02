package com.progsoftaplic.TrabalhoFinal.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.PagamentoRepository;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;

/**
 * Controller para carregar dados de teste no sistema
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class DadosTesteController {

    private final TicketRepository ticketRepository;
    private final PagamentoRepository pagamentoRepository;

    public DadosTesteController(TicketRepository ticketRepository, PagamentoRepository pagamentoRepository) {
        this.ticketRepository = ticketRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @PostMapping("/carregar-dados-teste")
    public ResponseEntity<String> carregarDadosTeste() {
        try {
            // Limpar dados existentes
            pagamentoRepository.deleteAll();
            ticketRepository.deleteAll();

            // Criar tickets de teste
            criarTickets();
            criarPagamentos();

            return ResponseEntity.ok("Dados de teste carregados com sucesso! " +
                    "Total de tickets: " + ticketRepository.count() + 
                    ", Total de pagamentos: " + pagamentoRepository.count());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao carregar dados de teste: " + e.getMessage());
        }
    }

    private void criarTickets() {
        // Tickets de hoje (01/12/2025)
        criarTicket("TK001", "ABC1234", LocalDateTime.of(2025, 12, 1, 8, 0), 
                   LocalDateTime.of(2025, 12, 1, 10, 30), true, new BigDecimal("5.00"));
        
        criarTicket("TK002", "DEF5678", LocalDateTime.of(2025, 12, 1, 9, 15), 
                   LocalDateTime.of(2025, 12, 1, 12, 45), true, new BigDecimal("14.50"));
        
        criarTicket("TK003", "GHI9012", LocalDateTime.of(2025, 12, 1, 14, 20), 
                   null, false, BigDecimal.ZERO);
        
        criarTicket("TK004", "JKL3456", LocalDateTime.of(2025, 12, 1, 7, 30), 
                   LocalDateTime.of(2025, 12, 1, 18, 0), true, new BigDecimal("52.50"));
        
        criarTicket("TK005", "MNO7890", LocalDateTime.of(2025, 12, 1, 16, 45), 
                   null, false, BigDecimal.ZERO);

        // Tickets de ontem (30/11/2025)
        criarTicket("TK006", "PQR1122", LocalDateTime.of(2025, 11, 30, 10, 0), 
                   LocalDateTime.of(2025, 11, 30, 11, 15), true, new BigDecimal("5.00"));
        
        criarTicket("TK007", "STU3344", LocalDateTime.of(2025, 11, 30, 13, 30), 
                   LocalDateTime.of(2025, 11, 30, 16, 20), true, new BigDecimal("9.50"));

        // Tickets de dias anteriores
        criarTicket("TK008", "VWX5566", LocalDateTime.of(2025, 11, 29, 8, 15), 
                   LocalDateTime.of(2025, 11, 29, 20, 30), true, new BigDecimal("60.00"));
        
        criarTicket("TK009", "YZA7788", LocalDateTime.of(2025, 11, 29, 11, 45), 
                   LocalDateTime.of(2025, 11, 29, 13, 0), true, new BigDecimal("5.00"));
        
        criarTicket("TK010", "BCD9900", LocalDateTime.of(2025, 11, 28, 15, 20), 
                   LocalDateTime.of(2025, 11, 28, 17, 55), true, new BigDecimal("9.50"));
    }

    private void criarTicket(String codigo, String placa, LocalDateTime entrada, 
                            LocalDateTime saida, boolean pago, BigDecimal valor) {
        Ticket ticket = new Ticket(codigo, placa, entrada);
        ticket.setSaida(saida);
        ticket.setPago(pago);
        ticket.setValor(valor);
        ticketRepository.save(ticket);
    }

    private void criarPagamentos() {
        // Pagamentos correspondentes aos tickets pagos
        criarPagamento("TK001", LocalDateTime.of(2025, 12, 1, 10, 25), new BigDecimal("5.00"));
        criarPagamento("TK002", LocalDateTime.of(2025, 12, 1, 12, 40), new BigDecimal("14.50"));
        criarPagamento("TK004", LocalDateTime.of(2025, 12, 1, 17, 55), new BigDecimal("52.50"));
        criarPagamento("TK006", LocalDateTime.of(2025, 11, 30, 11, 10), new BigDecimal("5.00"));
        criarPagamento("TK007", LocalDateTime.of(2025, 11, 30, 16, 15), new BigDecimal("9.50"));
        criarPagamento("TK008", LocalDateTime.of(2025, 11, 29, 20, 25), new BigDecimal("60.00"));
        criarPagamento("TK009", LocalDateTime.of(2025, 11, 29, 12, 55), new BigDecimal("5.00"));
        criarPagamento("TK010", LocalDateTime.of(2025, 11, 28, 17, 50), new BigDecimal("9.50"));
    }

    private void criarPagamento(String ticketCodigo, LocalDateTime dataPagamento, BigDecimal valor) {
        Pagamento pagamento = new Pagamento(ticketCodigo, valor);
        // Definir a data específica usando reflection ou método setter se existir
        try {
            java.lang.reflect.Field field = Pagamento.class.getDeclaredField("dataPagamento");
            field.setAccessible(true);
            field.set(pagamento, dataPagamento);
        } catch (Exception e) {
            // Se não conseguir, usar a data atual
        }
        pagamentoRepository.save(pagamento);
    }
}