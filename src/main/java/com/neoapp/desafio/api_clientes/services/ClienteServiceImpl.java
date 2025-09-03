package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.exceptions.BusinessRuleException;
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
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Transactional(readOnly = true)
    @Override
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        Page<Cliente> clientePage = repository.findAll(pageable);
        return clientePage.map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> buscarPorFiltros(String nome, String cpf, String email, String telefone, LocalDate dataNascimento,
                                                     String cep, String logradouro, String cidade, String estado,
                                                     Pageable pageable) {
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

        if(telefone != null && !telefone.isBlank()){
            spec = spec.and(ClienteSpecification.comTelefone(telefone));
        }

        if (dataNascimento != null) {
            spec = spec.and(ClienteSpecification.comDataNascimento(dataNascimento));
        }
        if (cep != null && !cep.isBlank()) {
            spec = spec.and(ClienteSpecification.comCep(cep.replaceAll("[^0-9]", "")));
        }
        if (logradouro != null && !logradouro.isBlank()) {
            spec = spec.and(ClienteSpecification.comLogradouro(logradouro));
        }
        if (cidade != null && !cidade.isBlank()) {
            spec = spec.and(ClienteSpecification.comCidade(cidade));
        }
        if (estado != null && !estado.isBlank()) {
            spec = spec.and(ClienteSpecification.comEstado(estado));
        }

        Page<Cliente> clientePage = repository.findAll(spec, pageable);
        return clientePage.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado. Id: " + id);
        }

        Cliente entity = repository.findById(id).get();
        return toDTO(entity);
    }

    @Transactional
    @Override
    public ClienteResponseDTO adicionarCliente(ClienteRequestDTO clienteDTO) {
        verificarDuplicidade(clienteDTO.getCpf(), clienteDTO.getEmail(), null);
        Cliente entity = new Cliente();
        mapDtoToEntity(clienteDTO, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    @Override
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteDTO) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Recurso não encontrado. Id: " + id);
        }
        verificarDuplicidade(clienteDTO.getCpf(), clienteDTO.getEmail(), id);

        Cliente entity = repository.findById(id).get();
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
            autenticacaoService.removerUsuarioPorClienteId(id);
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

        String cpfLimpo = dto.getCpf().replaceAll("[^0-9]", "");
        if (cpfLimpo.length() != 11) { // Adicione a validação do tamanho
            throw new BusinessRuleException("Formato de CPF inválido. Deve conter 11 dígitos.");
        }
        entity.setCpf(cpfLimpo);

        //Normalizar o CEP, removendo o hífen para salvar no banco
        String cepLimpo = dto.getEndereco().getCep().replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new BusinessRuleException("Formato de CEP inválido. Deve conter 8 dígitos.");
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

    private void verificarDuplicidade(String cpf, String email, Long id) {
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        // Verifica CPF
        Optional<Cliente> clientePorCpf = repository.findByCpf(cpfLimpo);
        if (clientePorCpf.isPresent() && !clientePorCpf.get().getId().equals(id)) {
            throw new BusinessRuleException("CPF já cadastrado no sistema.");
        }

        // Verifica Email
        Optional<Cliente> clientePorEmail = repository.findByEmail(email);
        if (clientePorEmail.isPresent() && !clientePorEmail.get().getId().equals(id)) {
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }
    }
}
