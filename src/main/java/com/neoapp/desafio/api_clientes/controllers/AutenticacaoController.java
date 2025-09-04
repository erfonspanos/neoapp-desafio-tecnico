package com.neoapp.desafio.api_clientes.controllers;

import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.dto.LoginDTO;
import com.neoapp.desafio.api_clientes.dto.RegistroUsuarioDTO;
import com.neoapp.desafio.api_clientes.dto.TokenResponseDTO;
import com.neoapp.desafio.api_clientes.model.Usuario;
import com.neoapp.desafio.api_clientes.services.AutenticacaoService;
import com.neoapp.desafio.api_clientes.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        var token = new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.senha());
        Authentication authentication = manager.authenticate(token);
        String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenResponseDTO(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity<ClienteResponseDTO> register(@RequestBody @Valid RegistroUsuarioDTO registroDTO) {
        ClienteResponseDTO novoCliente = autenticacaoService.registrarUsuarioCliente(registroDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }
}