package com.progsoftaplic.TrabalhoFinal.service.dto;

import javax.validation.constraints.NotBlank;

public class PagamentoRequestDTO {

    @NotBlank(message = "O código do ticket é obrigatório.")
    private String codigo;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
}
