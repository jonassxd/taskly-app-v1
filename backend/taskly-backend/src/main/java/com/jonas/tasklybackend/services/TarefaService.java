package com.jonas.tasklybackend.services;

import com.jonas.tasklybackend.dto.TarefaResponse;
import com.jonas.tasklybackend.enums.StatusTarefa;
import com.jonas.tasklybackend.exceptions.RegraDeNegocioException;
import com.jonas.tasklybackend.model.Tarefa;
import com.jonas.tasklybackend.repositories.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;

    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    public Tarefa criar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Tarefa não encontrada"));
    }

    public List<TarefaResponse> listarTodas() {
        return tarefaRepository.findAll()
                .stream()
                .map(TarefaResponse::new)
                .collect(Collectors.toList());
    }

    public Tarefa iniciar(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.iniciar();
        return tarefaRepository.save(tarefa);
    }

    public Tarefa concluir(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.concluir();
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id) {
        Tarefa tarefa = buscarPorId(id);
        tarefaRepository.delete(tarefa);
    }

    // ========================
    // BUSCAR POR CLIENTE / STATUS
    // ========================
    public List<TarefaResponse> buscarPorCliente(Long clienteId) {
        return tarefaRepository.findByClienteId(clienteId)
                .stream()
                .map(TarefaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TarefaResponse> buscarPorStatus(StatusTarefa status) {
        return tarefaRepository.findByStatus(status)
                .stream()
                .map(TarefaResponse::new)
                .collect(Collectors.toList());
    }

    public List<TarefaResponse> buscarPorClienteEStatus(Long clienteId, StatusTarefa status) {
        return tarefaRepository.findByClienteIdAndStatus(clienteId, status)
                .stream()
                .map(TarefaResponse::new)
                .collect(Collectors.toList());
    }
}