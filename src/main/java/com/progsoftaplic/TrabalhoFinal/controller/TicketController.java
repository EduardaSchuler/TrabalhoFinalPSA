
package com.progsoftaplic.TrabalhoFinal.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.service.TicketService;
import com.progsoftaplic.TrabalhoFinal.service.dto.PagamentoResponseDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketCreateRequestDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketResponseDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.TicketValorResponseDTO;
import com.progsoftaplic.TrabalhoFinal.service.dto.ValidarSaidaResponseDTO;



@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> criarTicket(@Validated @RequestBody TicketCreateRequestDTO req) {
        Ticket ticket = ticketService.criarTicket(req.getPlaca());
        return ResponseEntity.ok(toDTO(ticket));
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<TicketResponseDTO> buscarTicket(@PathVariable String codigo) {
        Optional<Ticket> opt = ticketService.buscarPorCodigo(codigo);
        return opt.map(ticket -> ResponseEntity.ok(toDTO(ticket)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{codigo}/valor")
    public ResponseEntity<TicketValorResponseDTO> calcularValor(@PathVariable String codigo) {
        BigDecimal valor = ticketService.calcularValor(codigo);
        return ResponseEntity.ok(new TicketValorResponseDTO(codigo, valor));
    }

    @PostMapping("/{codigo}/pay")
    public ResponseEntity<PagamentoResponseDTO> liberarTicketComPagamento(@PathVariable String codigo) {
        try {
            boolean sucesso = ticketService.pagarTicket(codigo);
            if (sucesso) {
                Optional<Ticket> ticket = ticketService.buscarPorCodigo(codigo);
                if (ticket.isPresent()) {
                    PagamentoResponseDTO dto = new PagamentoResponseDTO();
                    dto.setTicketCodigo(codigo);
                    dto.setValor(ticket.get().getValor());
                    dto.setDataPagamento(LocalDateTime.now());
                    return ResponseEntity.ok(dto);
                }
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{codigo}/validate")
    public ResponseEntity<ValidarSaidaResponseDTO> validarSaida(@PathVariable String codigo) {
        boolean liberado = ticketService.validarSaida(codigo);
        String motivo = liberado ? "Cancelas liberadas." : "Pagamento necessário ou ticket inválido.";
        return ResponseEntity.ok(new ValidarSaidaResponseDTO(codigo, liberado, motivo));
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
