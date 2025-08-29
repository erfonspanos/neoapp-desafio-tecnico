package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.exceptions.ResourceNotFoundException;
import com.neoapp.desafio.api_clientes.model.Cliente;
import com.neoapp.desafio.api_clientes.model.Endereco;
import com.neoapp.desafio.api_clientes.repositories.ClienteRepository;
import com.neoapp.desafio.api_clientes.repositories.specifications.ClienteSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        Page<Cliente> clientePage = repository.findAll(pageable);
        return clientePage.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> buscarPorFiltros(String nome, String cpf, String email, Pageable pageable) {
        Specification<Cliente> spec = ClienteSpecification.trueCondition();

        if(nome != null && !nome.isBlank()){
            spec = spec.and(ClienteSpecification.comNome(nome));
        }

        if(cpf != null && !cpf.isBlank()){
            spec = spec.and(ClienteSpecification.comCpf(cpf));
        }

        if(email != null && !email.isBlank()){
            spec = spec.and(ClienteSpecification.comEmail(email));
        }

        Page<Cliente> clientePage = repository.findAll(spec, pageable);
        return clientePage.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado. Id: " + id));
        return toDTO(cliente);
    }

    @Transactional
    @Override
    public ClienteResponseDTO adicionarCliente(ClienteRequestDTO clienteDTO) {
        Cliente entity = new Cliente();
        mapDtoToEntity(clienteDTO, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    @Override
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteDTO) {
        Cliente entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado. Id: " + id));

        mapDtoToEntity(clienteDTO, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void removerCliente(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado. Id: " + id);
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Violação de integridade referencial.");
        }
    }

    private ClienteResponseDTO toDTO(Cliente entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setEndereco(entity.getEndereco());

        if (entity.getDataNascimento() != null) {
            dto.setIdade(Period.between(entity.getDataNascimento(), LocalDate.now()).getYears());
        }

        return dto;
    }

    private void mapDtoToEntity(ClienteRequestDTO dto, Cliente entity) {
        entity.setNome(dto.getNome());
        entity.setCpf(dto.getCpf());
        entity.setEmail(dto.getEmail());
        entity.setTelefone(dto.getTelefone());
        entity.setDataNascimento(dto.getDataNascimento());

        //Normalizar o CEP, removendo o hífen para salvar no banco
        String cepLimpo = dto.getEndereco().getCep().replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new IllegalArgumentException("Formato de CEP inválido.");
        }

        // Garante que o objeto Endereco exista antes de setar os valores
        Endereco endereco = entity.getEndereco() == null ? new Endereco() : entity.getEndereco();

        endereco.setCep(cepLimpo);
        endereco.setLogradouro(dto.getEndereco().getLogradouro());
        endereco.setNumero(dto.getEndereco().getNumero());
        endereco.setComplemento(dto.getEndereco().getComplemento());
        endereco.setBairro(dto.getEndereco().getBairro());
        endereco.setCidade(dto.getEndereco().getCidade());
        endereco.setEstado(dto.getEndereco().getEstado());

        entity.setEndereco(endereco);
    }
}
