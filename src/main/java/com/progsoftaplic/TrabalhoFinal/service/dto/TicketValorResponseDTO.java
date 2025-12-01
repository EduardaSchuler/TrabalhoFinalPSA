package com.progsoftaplic.TrabalhoFinal.service.dto;

import java.math.BigDecimal;

public class TicketValorResponseDTO {

    private String codigo;
    private BigDecimal valor;

    public TicketValorResponseDTO() {}

    public TicketValorResponseDTO(String codigo, BigDecimal valor) {
        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
