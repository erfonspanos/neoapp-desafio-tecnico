package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    Page<ClienteResponseDTO> listarTodos(Pageable pageable);

    Page<ClienteResponseDTO> buscarPorFiltros(String nome, String cpf, String email, Pageable pageable);

    ClienteResponseDTO buscarPorId(Long id);

    ClienteResponseDTO adicionarCliente(ClienteRequestDTO clienteDTO);

    ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteDTO);

    void removerCliente(Long id);
}
