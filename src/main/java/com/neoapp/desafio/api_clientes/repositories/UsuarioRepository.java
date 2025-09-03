package com.neoapp.desafio.api_clientes.repositories;

import com.neoapp.desafio.api_clientes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByClienteId(Long clienteId);
    boolean existsByClienteId(Long clienteId);
}
