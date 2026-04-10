package com.jonas.tasklybackend.services;


import com.jonas.tasklybackend.exceptions.RegraDeNegocioException;
import com.jonas.tasklybackend.model.Cliente;
import com.jonas.tasklybackend.model.Tarefa;
import com.jonas.tasklybackend.repositories.ClienteRepository;
import com.jonas.tasklybackend.repositories.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final TarefaRepository tarefaRepository;

    public ClienteService(ClienteRepository clienteRepository, TarefaRepository tarefaRepository) {
        this.clienteRepository = clienteRepository;
        this.tarefaRepository = tarefaRepository;
    }

    // ========================
    // CRIAR CLIENTE
    // ========================
    public Cliente criar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // ========================
    // LISTAR TODOS CLIENTES
    // ========================
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // ========================
    // BUSCAR CLIENTE POR ID
    // ========================
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));
    }

    // ========================
    // ATUALIZAR CLIENTE
    // ========================
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);
        cliente.atualizarContato(clienteAtualizado.getEmail(), clienteAtualizado.getTelefone());
        return clienteRepository.save(cliente);
    }

    // ========================
    // DELETAR CLIENTE
    // ========================
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);

        // Verifica se existem tarefas associadas
        List<Tarefa> tarefas = tarefaRepository.findByClienteId(cliente.getId());
        if (!tarefas.isEmpty()) {
            throw new RegraDeNegocioException(
                    "Não é possível deletar o cliente, pois ele possui " + tarefas.size() + " tarefa(s) associada(s)"
            );
        }

        // Se não houver tarefas, deleta o cliente
        clienteRepository.delete(cliente);
    }
}