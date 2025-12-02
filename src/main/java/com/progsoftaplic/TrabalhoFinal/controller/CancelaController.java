package com.progsoftaplic.TrabalhoFinal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.service.TicketService;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketCreateRequestDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketResponseDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.ValidarSaidaResponseDTO;


@RestController
@RequestMapping("/cancela")
@CrossOrigin(origins = "*")
public class CancelaController {

    private final TicketService ticketService;

    public CancelaController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/entrada")
    public ResponseEntity<TicketResponseDTO> emitirTicket(@Validated @RequestBody TicketCreateRequestDTO req) {
        try {
            Ticket ticket = ticketService.criarTicket(req.getPlaca());
            TicketResponseDTO dto = toDTO(ticket);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/saida/{codigo}")
    public ResponseEntity<ValidarSaidaResponseDTO> validarSaida(@PathVariable String codigo) {
        try {
            boolean liberado = ticketService.validarSaida(codigo);
            String motivo = liberado ? "Cancela liberada" : "Pagamento necessário ou ticket inválido";
            ValidarSaidaResponseDTO dto = new ValidarSaidaResponseDTO(codigo, liberado, motivo);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            ValidarSaidaResponseDTO dto = new ValidarSaidaResponseDTO(codigo, false, "Código inválido");
            return ResponseEntity.badRequest().body(dto);
        } catch (Exception e) {
            ValidarSaidaResponseDTO dto = new ValidarSaidaResponseDTO(codigo, false, "Erro interno do sistema");
            return ResponseEntity.internalServerError().body(dto);
        }
    }

    private TicketResponseDTO toDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setCodigo(ticket.getCodigo());
        dto.setPlaca(ticket.getPlaca());
        dto.setEntrada(ticket.getEntrada());
        dto.setSaida(ticket.getSaida());
        dto.setPago(ticket.isPago());
        dto.setValor(ticket.getValor());
        return dto;
    }
}