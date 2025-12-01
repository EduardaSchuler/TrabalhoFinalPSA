
package com.progsoftaplic.TrabalhoFinal.service.dto;

import javax.validation.constraints.NotBlank;

public class TicketCreateRequestDTO {

    @NotBlank(message = "A placa do veículo é obrigatória.")
    private String placa;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
