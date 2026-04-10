package com.jonas.tasklybackend.controller;



import com.jonas.tasklybackend.dto.ApiResponse;
import com.jonas.tasklybackend.dto.ClienteResponse;
import com.jonas.tasklybackend.model.Cliente;
import com.jonas.tasklybackend.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // CRIAR CLIENTE
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> criar(@RequestBody @Valid Cliente cliente) {
        Cliente criado = clienteService.criar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.sucesso(201, new ClienteResponse(criado)));
    }


    // LISTAR TODOS OS CLIENTES
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> listarTodos() {
        List<ClienteResponse> clientes = clienteService.listarTodos()
                .stream()
                .map(ClienteResponse::new)
                .toList();
        return ResponseEntity.ok(ApiResponse.sucesso(200, clientes));
    }


    // BUSCAR CLIENTE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, new ClienteResponse(cliente)));
    }

    // ATUALIZAR CLIENTE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid Cliente clienteAtualizado
    ) {
        Cliente atualizado = clienteService.atualizar(id, clienteAtualizado);
        return ResponseEntity.ok(ApiResponse.sucesso(200, new ClienteResponse(atualizado)));
    }

    // DELETAR CLIENTE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.ok(ApiResponse.sucesso(200, null));
    }
}