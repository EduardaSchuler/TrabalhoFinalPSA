
package com.progsoftaplic.TrabalhoFinal.service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketResponseDTO {

    private String codigo;
    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private boolean pago;
    private BigDecimal valor;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public LocalDateTime getEntrada() { return entrada; }
    public void setEntrada(LocalDateTime entrada) { this.entrada = entrada; }

    public LocalDateTime getSaida() { return saida; }
    public void setSaida(LocalDateTime saida) { this.saida = saida; }

    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
