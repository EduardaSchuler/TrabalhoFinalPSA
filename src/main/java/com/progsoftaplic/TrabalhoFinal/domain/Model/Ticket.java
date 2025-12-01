package com.progsoftaplic.TrabalhoFinal.domain.Model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private String placa;
    private LocalDateTime entrada;
    private LocalDateTime pagamento;
    private boolean liberado;
}
