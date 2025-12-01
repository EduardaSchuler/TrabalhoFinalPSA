package com.progsoftaplic.TrabalhoFinal.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.service.TicketService;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketRequestDTO;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Ponto de acesso para cancela de entrada - Emissão de ticket
    @PostMapping("/entrada")
    public ResponseEntity<Ticket> emitirTicket(@RequestBody TicketRequestDTO request) {
        try {
            Ticket ticket = ticketService.criarTicket(request.getPlaca());
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Ponto de acesso para cancela de saída - Validação de ticket
    @PostMapping("/saida/{codigo}")
    public ResponseEntity<Map<String, Object>> validarSaida(@PathVariable String codigo) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean liberado = ticketService.validarSaida(codigo);
            response.put("liberado", liberado);
            response.put("codigo", codigo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("liberado", false);
            response.put("erro", "Ticket não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Ponto de acesso para cálculo de valor - Interface do operador
    @GetMapping("/calcular/{codigo}")
    public ResponseEntity<Map<String, Object>> calcularValor(@PathVariable String codigo) {
        Map<String, Object> response = new HashMap<>();
        try {
            BigDecimal valor = ticketService.calcularValor(codigo);
            response.put("codigo", codigo);
            response.put("valor", valor);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", "Ticket não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Ponto de acesso para pagamento - Interface do operador
    @PostMapping("/pagar/{codigo}")
    public ResponseEntity<Map<String, Object>> pagarTicket(@PathVariable String codigo) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean sucesso = ticketService.pagarTicket(codigo);
            response.put("sucesso", sucesso);
            response.put("codigo", codigo);
            if (sucesso) {
                response.put("mensagem", "Ticket pago com sucesso");
            } else {
                response.put("mensagem", "Erro ao processar pagamento");
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("sucesso", false);
            response.put("erro", "Ticket não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Ponto de acesso para relatório gerencial - Total por dia
    @GetMapping("/relatorio/dia")
    public ResponseEntity<Map<String, Object>> relatorioDia(@RequestParam String data) {
        Map<String, Object> response = new HashMap<>();
        try {
            LocalDate date = LocalDate.parse(data);
            LocalDateTime inicio = date.atStartOfDay();
            LocalDateTime fim = date.plusDays(1).atStartOfDay();
            
            BigDecimal total = ticketService.totalRecebido(inicio, fim);
            long quantidade = ticketService.quantidadeTicketsPagos(inicio, fim);
            
            response.put("data", data);
            response.put("totalRecebido", total);
            response.put("quantidadeTickets", quantidade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", "Data inválida");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Ponto de acesso para relatório gerencial - Total por mês
    @GetMapping("/relatorio/mes")
    public ResponseEntity<Map<String, Object>> relatorioMes(@RequestParam int ano, @RequestParam int mes) {
        Map<String, Object> response = new HashMap<>();
        try {
            YearMonth yearMonth = YearMonth.of(ano, mes);
            LocalDateTime inicio = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime fim = yearMonth.plusMonths(1).atDay(1).atStartOfDay();
            
            BigDecimal total = ticketService.totalRecebido(inicio, fim);
            long quantidade = ticketService.quantidadeTicketsPagos(inicio, fim);
            
            response.put("ano", ano);
            response.put("mes", mes);
            response.put("totalRecebido", total);
            response.put("quantidadeTickets", quantidade);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("erro", "Data inválida");
            return ResponseEntity.badRequest().body(response);
        }
    }
}