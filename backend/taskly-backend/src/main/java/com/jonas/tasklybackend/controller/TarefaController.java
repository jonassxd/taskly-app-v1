package com.jonas.tasklybackend.controller;
import com.jonas.tasklybackend.dto.ApiResponse;
import com.jonas.tasklybackend.dto.TarefaRequest;
import com.jonas.tasklybackend.dto.TarefaResponse;
import com.jonas.tasklybackend.enums.StatusTarefa;
import com.jonas.tasklybackend.exceptions.RegraDeNegocioException;
import com.jonas.tasklybackend.model.Cliente;
import com.jonas.tasklybackend.model.Tarefa;
import com.jonas.tasklybackend.repositories.ClienteRepository;
import com.jonas.tasklybackend.services.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final ClienteRepository clienteRepository;

    public TarefaController(TarefaService tarefaService, ClienteRepository clienteRepository) {
        this.tarefaService = tarefaService;
        this.clienteRepository = clienteRepository;
    }

    // CRIAR TAREFA
    @PostMapping
    public ResponseEntity<ApiResponse<TarefaResponse>> criar(@RequestBody @Valid TarefaRequest request) {

        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RegraDeNegocioException("Cliente não encontrado"));

        Tarefa tarefa = new Tarefa(
                request.getDescricao(),
                request.getData(),
                cliente
        );

        Tarefa criada = tarefaService.criar(tarefa);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(201, new TarefaResponse(criada)));
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TarefaResponse>> buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, new TarefaResponse(tarefa)));
    }

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> listarTodas() {
        List<TarefaResponse> lista = tarefaService.listarTodas();
        return ResponseEntity.ok(ApiResponse.sucesso(200, lista));
    }


    // BUSCAR POR CLIENTE
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> buscarPorCliente(@PathVariable Long clienteId) {
        List<TarefaResponse> lista = tarefaService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(ApiResponse.sucesso(200, lista));
    }

    // BUSCAR POR STATUS
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> buscarPorStatus(@PathVariable StatusTarefa status) {
        List<TarefaResponse> lista = tarefaService.buscarPorStatus(status);
        return ResponseEntity.ok(ApiResponse.sucesso(200, lista));
    }


    // BUSCAR POR CLIENTE E STATUS
    @GetMapping("/cliente/{clienteId}/status/{status}")
    public ResponseEntity<ApiResponse<List<TarefaResponse>>> buscarPorClienteEStatus(
            @PathVariable Long clienteId,
            @PathVariable StatusTarefa status
    ) {
        List<TarefaResponse> lista = tarefaService.buscarPorClienteEStatus(clienteId, status);
        return ResponseEntity.ok(ApiResponse.sucesso(200, lista));
    }

    // INICIAR TAREFA
    @PutMapping("/{id}/iniciar")
    public ResponseEntity<ApiResponse<TarefaResponse>> iniciar(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.iniciar(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, new TarefaResponse(tarefa)));
    }

    // CONCLUIR TAREFA
    @PutMapping("/{id}/concluir")
    public ResponseEntity<ApiResponse<TarefaResponse>> concluir(@PathVariable Long id) {
        Tarefa tarefa = tarefaService.concluir(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, new TarefaResponse(tarefa)));
    }

    // DELETAR TAREFA
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, null));
    }
}