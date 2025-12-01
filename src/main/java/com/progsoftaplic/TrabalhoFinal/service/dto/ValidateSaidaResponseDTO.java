package com.progsoftaplic.TrabalhoFinal.service.dto;

public class ValidarSaidaResponseDTO {

    private String codigo;
    private boolean liberado;
    private String motivo;

    public ValidarSaidaResponseDTO() {}

    public ValidarSaidaResponseDTO(String codigo, boolean liberado, String motivo) {
        this.codigo = codigo;
        this.liberado = liberado;
        this.motivo = motivo;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public boolean isLiberado() { return liberado; }
    public void setLiberado(boolean liberado) { this.liberado = liberado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
