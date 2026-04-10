package com.jonas.tasklybackend.model;

import com.jonas.tasklybackend.enums.StatusTarefa;
import com.jonas.tasklybackend.exceptions.RegraDeNegocioException;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_tarefa")
public class Tarefa{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Construtor do JPA
    protected Tarefa() {}

    // 🔥 CONSTRUTOR PRINCIPAL (REGRA DE NEGÓCIO)
    public Tarefa(String descricao, LocalDate data, Cliente cliente) {
        if (descricao == null || descricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição não pode ser vazia");
        }
        if (data == null) {
            throw new RegraDeNegocioException("Data não pode ser nula");
        }
        if (cliente == null) {
            throw new RegraDeNegocioException("Cliente não pode ser nulo");
        }

        this.descricao = descricao;
        this.data = data;
        this.cliente = cliente;
        this.status = StatusTarefa.PENDENTE;
    }

    // ========================
    // GETTERS
    // ========================

    public Long getId() { return id; }

    public String getDescricao() { return descricao; }

    public LocalDate getData() { return data; }

    public StatusTarefa getStatus() { return status; }

    public Cliente getCliente() { return cliente; }

    // ========================
    // REGRAS DE NEGÓCIO
    // ========================

    public void iniciar() {

        if (this.status == StatusTarefa.EM_ANDAMENTO) {
            throw new RegraDeNegocioException(
                    "Não é possível iniciar esta tarefa, pois ela já está em andamento."
            );
        }

        if (this.status == StatusTarefa.CONCLUIDA) {
            throw new RegraDeNegocioException(
                    "Não é possível iniciar esta tarefa, pois ela já foi concluída."
            );
        }

        this.status = StatusTarefa.EM_ANDAMENTO;
    }

    public void concluir() {

        if (this.status == StatusTarefa.PENDENTE) {
            throw new RegraDeNegocioException(
                    "Não é possível concluir uma tarefa pendente. Inicie a tarefa primeiro."
            );
        }

        if (this.status == StatusTarefa.CONCLUIDA) {
            throw new RegraDeNegocioException(
                    "Não é possível concluir esta tarefa, pois ela já foi concluída."
            );
        }

        this.status = StatusTarefa.CONCLUIDA;
    }

    public void alterarDescricao(String novaDescricao) {
        if (novaDescricao == null || novaDescricao.isBlank()) {
            throw new RegraDeNegocioException("Descrição inválida");
        }
        this.descricao = novaDescricao;
    }

    public void alterarData(LocalDate novaData) {
        if (novaData == null) {
            throw new RegraDeNegocioException("Data inválida");
        }
        this.data = novaData;
    }
}