package com.neoapp.desafio.api_clientes.controllers;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService service;

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listarTodos(Pageable pageable) {
        Page<ClienteResponseDTO> dtoPage = service.listarTodos(pageable);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscarPorFiltros(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "email", required = false) String email,
            Pageable pageable) {

        Page<ClienteResponseDTO> dtoPage = service.buscarPorFiltros(nome, cpf, email, pageable);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        ClienteResponseDTO dto = service.buscarPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> adicionarCliente(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO newDto = service.adicionarCliente(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(newDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO updatedDto = service.atualizarCliente(id, dto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCliente(@PathVariable Long id) {
        service.removerCliente(id);

        return ResponseEntity.noContent().build();
    }
}
