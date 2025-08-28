package com.neoapp.desafio.api_clientes.repositories;

import com.neoapp.desafio.api_clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
