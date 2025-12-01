
package com.progsoftaplic.TrabalhoFinal.controller;

import com.progsoftaplic.TrabalhoFinal.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.domain.Pagamento;
import com.progsoftaplic.TrabalhoFinal.dto.*;
import com.progsoftaplic.TrabalhoFinal.service.TicketService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> criarTicket(@Valid @RequestBody TicketCreateRequestDTO req) {
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
    public ResponseEntity<PagamentoResponseDTO> pagar(@PathVariable String codigo) {
        Pagamento pagamento = ticketService.pagarETrazerPagamento(codigo);
        if (pagamento == null) {
            return ResponseEntity.notFound().build();
        }
        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(pagamento.getId());
        dto.setTicketCodigo(pagamento.getTicketCodigo());
        dto.setValor(pagamento.getValor());
        dto.setDataPagamento(pagamento.getDataPagamento());
        return ResponseEntity.ok(dto);
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
