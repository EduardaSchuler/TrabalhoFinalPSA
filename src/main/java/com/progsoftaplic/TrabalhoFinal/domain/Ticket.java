package com.progsoftaplic.TrabalhoFinal.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Domain Model - Ticket de Estacionamento
 * 
 * Esta classe representa um ticket emitido para controle do estacionamento,
 * seguindo o padrão Domain Model com encapsulamento adequado.
 */
@Entity
@Table(name = "ticket")
public class Ticket {
    
    @Id
    @Column(name = "codigo", length = 50, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private String placa;

    @Column(nullable = false)
    private LocalDateTime entrada;

    private LocalDateTime saida;

    @Column(nullable = false)
    private boolean pago;

    @Column(precision = 10, scale = 2)
    private BigDecimal valor;

    public Ticket(String codigo, String placa, LocalDateTime entrada) {
        this.codigo = codigo;
        this.placa = placa;
        this.entrada = entrada;
    }


    public String getCodigo() {
        return codigo;
    }

    public String getPlaca() {
        return placa;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public boolean isPago() {
        return pago;
    }

    public BigDecimal getValor() {
        return valor;
    }

    // Métodos de modificação com validações (seguindo Domain Model pattern)
    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código do ticket não pode ser nulo ou vazio");
        }
        this.codigo = codigo;
    }

    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("Placa não pode ser nula ou vazia");
        }
        this.placa = placa.toUpperCase().trim();
    }

    public void setEntrada(LocalDateTime entrada) {
        if (entrada == null) {
            throw new IllegalArgumentException("Data/hora de entrada não pode ser nula");
        }
        this.entrada = entrada;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public void setValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor não pode ser nulo");
        }
        if (valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }
        this.valor = valor;
    }

    // Métodos de domínio (Domain Model pattern)
    public boolean isVencido() {
        return saida != null && LocalDateTime.now().isAfter(saida.plusMinutes(15));
    }

    public boolean isPeriodoCortesia() {
        if (entrada == null) return false;
        return LocalDateTime.now().isBefore(entrada.plusMinutes(15));
    }

    public void marcarComoPago(BigDecimal valorPago) {
        if (valorPago == null || valorPago.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor de pagamento inválido");
        }
        this.valor = valorPago;
        this.pago = true;
    }

    public void registrarSaida() {
        this.saida = LocalDateTime.now();
    }

    // Métodos de igualdade e hash baseados no código do ticket
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return Objects.equals(codigo, ticket.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "codigo='" + codigo + '\'' +
                ", placa='" + placa + '\'' +
                ", entrada=" + entrada +
                ", saida=" + saida +
                ", pago=" + pago +
                ", valor=" + valor +
                '}';
    }
}
