package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repositorio;

    @Transactional(readOnly = true)
    @Override
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        return null;
    }

    @Transactional
    @Override
    public ClienteResponseDTO adicionarCliente(ClienteRequestDTO clienteDTO) {
        return null;
    }

    @Transactional
    @Override
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteDTO) {
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void removerCliente(Long id) {

    }
}
