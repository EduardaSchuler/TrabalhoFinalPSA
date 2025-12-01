package com.progsoftaplic.TrabalhoFinal.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoResponseDTO {

    private Long id;
    private String ticketCodigo;
    private BigDecimal valor;
    private LocalDateTime dataPagamento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTicketCodigo() { return ticketCodigo; }
    public void setTicketCodigo(String ticketCodigo) { this.ticketCodigo = ticketCodigo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
}
