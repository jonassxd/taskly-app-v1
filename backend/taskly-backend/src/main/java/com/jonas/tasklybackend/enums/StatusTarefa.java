package com.jonas.tasklybackend.enums;


public enum StatusTarefa {
    PENDENTE,
    EM_ANDAMENTO,
    CONCLUIDA;

    public boolean podeIniciar() {
        return this == PENDENTE;
    }

    public boolean podeConcluir() {
        return this == EM_ANDAMENTO;
    }
}