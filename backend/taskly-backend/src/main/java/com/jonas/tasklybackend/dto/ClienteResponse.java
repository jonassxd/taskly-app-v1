package com.jonas.tasklybackend.dto;


import com.jonas.tasklybackend.model.Cliente;

public class ClienteResponse {

    private Long id;
    private String nome;
    private String email;
    private String telefone;

    public ClienteResponse(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}