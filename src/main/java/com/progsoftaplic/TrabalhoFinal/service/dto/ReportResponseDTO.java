package com.progsoftaplic.TrabalhoFinal.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReportResponseDTO {

    private BigDecimal totalRecebido;
    private Integer quantidadeTicketsPagos;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public BigDecimal getTotalRecebido() { return totalRecebido; }
    public void setTotalRecebido(BigDecimal totalRecebido) { this.totalRecebido = totalRecebido; }

    public Integer getQuantidadeTicketsPagos() { return quantidadeTicketsPagos; }
    public void setQuantidadeTicketsPagos(Integer quantidadeTicketsPagos) { this.quantidadeTicketsPagos = quantidadeTicketsPagos; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime fim) { this.fim = fim; }
}
