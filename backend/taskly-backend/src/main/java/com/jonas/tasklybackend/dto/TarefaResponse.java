package com.jonas.tasklybackend.dto;

import com.jonas.tasklybackend.enums.StatusTarefa;
import com.jonas.tasklybackend.model.Tarefa;

import java.time.LocalDate;

public class TarefaResponse {

    private Long id;
    private String descricao;
    private LocalDate data;
    private StatusTarefa status;

    // Dados do cliente
    private Long clienteId;
    private String clienteNome;
    private String clienteEmail;
    private String clienteTelefone;

    public TarefaResponse(Tarefa tarefa) {
        this.id = tarefa.getId();
        this.descricao = tarefa.getDescricao();
        this.data = tarefa.getData();
        this.status = tarefa.getStatus();

        if (tarefa.getCliente() != null) {
            this.clienteId = tarefa.getCliente().getId();
            this.clienteNome = tarefa.getCliente().getNome();
            this.clienteEmail = tarefa.getCliente().getEmail();
            this.clienteTelefone = tarefa.getCliente().getTelefone();
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public LocalDate getData() { return data; }
    public StatusTarefa getStatus() { return status; }

    public Long getClienteId() { return clienteId; }
    public String getClienteNome() { return clienteNome; }
    public String getClienteEmail() { return clienteEmail; }
    public String getClienteTelefone() { return clienteTelefone; }
}
