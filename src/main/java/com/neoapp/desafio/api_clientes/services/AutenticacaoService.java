package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.AdminCreationDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.dto.RegistroUsuarioDTO;
import com.neoapp.desafio.api_clientes.exceptions.BusinessRuleException;
import com.neoapp.desafio.api_clientes.model.Cliente;
import com.neoapp.desafio.api_clientes.model.Role;
import com.neoapp.desafio.api_clientes.model.Usuario;
import com.neoapp.desafio.api_clientes.repositories.ClienteRepository;
import com.neoapp.desafio.api_clientes.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: "+ username));
    }

    @Transactional
    public ClienteResponseDTO registrarUsuarioCliente(RegistroUsuarioDTO dto) {
        Cliente clienteExistente = clienteRepository.findByEmail(dto.email())
                .orElseThrow(() -> new BusinessRuleException("Nenhum cliente cadastrado com este e-mail. Contate um administrador."));

        if (usuarioRepository.existsByClienteId(clienteExistente.getId())) {
            throw new BusinessRuleException("Este cliente já possui uma conta de usuário registrada.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail(clienteExistente.getEmail());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha()));
        novoUsuario.setRole(Role.CLIENTE);
        novoUsuario.setCliente(clienteExistente);

        usuarioRepository.save(novoUsuario);

        return toClienteResponseDTO(clienteExistente);
    }

    @Transactional
    public void criarAdmin(AdminCreationDTO dto){
        if(usuarioRepository.findByEmail(dto.email()).isPresent()){
            throw new BusinessRuleException("E-mail já cadastrado no sistema.");
        }

        Usuario novoAdmin = new Usuario();
        novoAdmin.setEmail(dto.email());
        novoAdmin.setSenha(passwordEncoder.encode(dto.senha()));
        novoAdmin.setRole(Role.ADMIN);
        novoAdmin.setCliente(null);
        usuarioRepository.save(novoAdmin);
    }

    private ClienteResponseDTO toClienteResponseDTO(Cliente entity) {
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

}
