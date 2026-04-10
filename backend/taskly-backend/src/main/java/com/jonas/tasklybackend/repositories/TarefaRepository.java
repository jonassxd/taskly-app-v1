package com.jonas.tasklybackend.repositories;

import com.jonas.tasklybackend.enums.StatusTarefa;
import com.jonas.tasklybackend.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Buscar todas as tarefas de um cliente
    List<Tarefa> findByClienteId(Long clienteId);

    // Buscar todas as tarefas por status
    List<Tarefa> findByStatus(StatusTarefa status);

    // Buscar tarefas de um cliente por status
    List<Tarefa> findByClienteIdAndStatus(Long clienteId, StatusTarefa status);
}
