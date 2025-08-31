package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ClienteService {

    Page<ClienteResponseDTO> listarTodos(Pageable pageable);

    Page<ClienteResponseDTO> buscarPorFiltros(String nome, String cpf, String email, String telefone, LocalDate dataNascimento,
                                              String cep, String logradouro, String cidade, String estado,
                                              Pageable pageable);

    ClienteResponseDTO buscarPorId(Long id);

    ClienteResponseDTO adicionarCliente(ClienteRequestDTO clienteDTO);

    ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteDTO);

    void removerCliente(Long id);
}
