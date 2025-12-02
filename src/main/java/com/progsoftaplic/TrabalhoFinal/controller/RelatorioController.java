package com.progsoftaplic.TrabalhoFinal.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.progsoftaplic.TrabalhoFinal.service.TicketService;
import com.progsoftaplic.TrabalhoFinal.service.dto.ReportResponseDTO;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final TicketService ticketService;

    public RelatorioController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/periodo")
    public ResponseEntity<ReportResponseDTO> receitaPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        var total = ticketService.totalRecebido(inicio, fim);
        var quantidade = ticketService.contarTicketsPagos(inicio, fim);
        
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setTotalRecebido(total);
        dto.setQuantidadeTicketsPagos(quantidade);
        dto.setInicio(inicio);
        dto.setFim(fim);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dia")
    public ResponseEntity<ReportResponseDTO> receitaPorDia(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return receitaPorPeriodo(inicio, fim);
    }

    @GetMapping("/mes")
    public ResponseEntity<ReportResponseDTO> receitaPorMes(
            @RequestParam int ano, 
            @RequestParam int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDateTime inicio = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime fim = yearMonth.atEndOfMonth().plusDays(1).atStartOfDay();
        return receitaPorPeriodo(inicio, fim);
    }
}