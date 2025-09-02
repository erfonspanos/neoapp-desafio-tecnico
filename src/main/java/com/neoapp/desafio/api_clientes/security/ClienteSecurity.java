package com.neoapp.desafio.api_clientes.security;

import com.neoapp.desafio.api_clientes.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("clienteSecurity")
public class ClienteSecurity {

    public boolean checkClienteId(Authentication authentication, Long id) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        return usuarioLogado.getCliente() != null && usuarioLogado.getCliente().getId().equals(id);
    }
}