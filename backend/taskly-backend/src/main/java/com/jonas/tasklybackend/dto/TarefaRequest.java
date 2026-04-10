package com.jonas.tasklybackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TarefaRequest{

    @NotBlank(message = "Descrição não pode ser vazia")
    private String descricao;

    @NotNull(message = "Data não pode ser nula")
    private LocalDate data;

    @NotNull(message = "ID do cliente não pode ser nulo")
    private Long clienteId;


    // GETTERS
    public String getDescricao() {
        return descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public Long getClienteId() {
        return clienteId;
    }


    // SETTERS
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}