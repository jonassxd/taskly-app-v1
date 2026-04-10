package com.jonas.tasklybackend.dto;

import jakarta.validation.constraints.NotBlank;

public class ClienteRequest {

    @NotBlank(message = "Nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "Email não pode ser vazio")
    private String email;

    @NotBlank(message = "Telefone não pode ser vazio")
    private String telefone;

    // Getters
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
}