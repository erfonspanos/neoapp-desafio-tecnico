package com.neoapp.desafio.api_clientes.controllers;

import com.neoapp.desafio.api_clientes.dto.AdminCreationDTO;
import com.neoapp.desafio.api_clientes.services.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> criarAdmin(@RequestBody @Valid AdminCreationDTO dto){
        autenticacaoService.criarAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
