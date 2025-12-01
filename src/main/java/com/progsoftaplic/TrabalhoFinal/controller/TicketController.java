package com.progsoftaplic.TrabalhoFinal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.estac.domain.Ticket;
import com.progsoftaplic.TrabalhoFinal.repository.TicketRepository;
import com.progsoftaplic.TrabalhoFinal.service.EstacionamentoService;
import com.progsoftaplic.TrabalhoFinal.domain.model.Pagamento;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/gerar")
    public Ticket gerarTicket(@RequestParam String placa) {
        Ticket t = new Ticket();
        t.setCodigo(UUID.randomUUID().toString());
        t.setPlaca(placa);
        t.setEntrada(LocalDateTime.now());
        t.setLiberado(false);
        return ticketRepository.save(t);
    }

    @GetMapping("/{codigo}")
    public Ticket getByCodigo(@PathVariable String codigo) {
        return ticketRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
    }
}