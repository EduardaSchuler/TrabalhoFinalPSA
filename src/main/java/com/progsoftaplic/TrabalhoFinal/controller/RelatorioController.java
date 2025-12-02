package com.progsoftaplic.TrabalhoFinal.controller;

import com.progsoftaplic.TrabalhoFinal.service.dto.ReportResponseDTO;
import com.progsoftaplic.TrabalhoFinal.service.TicketService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

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
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim) {
        var total = ticketService.totalRecebido(inicio, fim);
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setTotalRecebido(total);
        dto.setInicio(inicio);
        dto.setFim(fim);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dia")
    public ResponseEntity<ReportResponseDTO> receitaPorDia(@RequestParam LocalDate data) {
        LocalDateTime inicio = data.atStartOfDay();
        LocalDateTime fim = data.plusDays(1).atStartOfDay();
        return receitaPorPeriodo(inicio, fim);
    }

    @GetMapping("/mes")
    public ResponseEntity<ReportResponseDTO> receitaPorMes(@RequestParam YearMonth mes) {
        LocalDateTime inicio = mes.atDay(1).atStartOfDay();
        LocalDateTime fim = mes.atEndOfMonth().plusDays(1).atStartOfDay();
        return receitaPorPeriodo(inicio, fim);
    }
}