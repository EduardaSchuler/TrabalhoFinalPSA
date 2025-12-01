
package com.progsoftaplic.TrabalhoFinal.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Pagamento {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @Column(nullable = false)
    private String ticketCodigo;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;


    public Pagamento() {
    }

    public Pagamento(String ticketCodigo, BigDecimal valor) {
        this.ticketCodigo = ticketCodigo;
        this.valor = valor;
        this.dataPagamento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTicketCodigo() {
        return ticketCodigo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

}
