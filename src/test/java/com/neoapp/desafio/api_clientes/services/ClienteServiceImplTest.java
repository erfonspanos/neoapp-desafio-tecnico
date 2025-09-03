package com.neoapp.desafio.api_clientes.services;

import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.exceptions.BusinessRuleException;
import com.neoapp.desafio.api_clientes.exceptions.ResourceNotFoundException;
import com.neoapp.desafio.api_clientes.model.Cliente;
import com.neoapp.desafio.api_clientes.model.Endereco;
import com.neoapp.desafio.api_clientes.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AutenticacaoService autenticacaoService; // Usado no método de removerCliente

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        Endereco endereco = new Endereco();
        endereco.setCep("60111222");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero(123);
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("CE");

        cliente = new Cliente(
                1L,
                "João Silva",
                "11122233344",
                "joao.silva@email.com",
                "85988887777",
                LocalDate.now().minusYears(30), // Cliente com 30 anos
                LocalDateTime.now(),
                endereco
        );

        requestDTO = new ClienteRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setCpf("111.222.333-44");
        requestDTO.setEmail("joao.silva@email.com");
        requestDTO.setTelefone("85988887777");
        requestDTO.setDataNascimento(LocalDate.now().minusYears(30));
        requestDTO.setEndereco(endereco);
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    void buscarPorId_deveRetornarCliente_quandoIdExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.existsById(1L)).thenReturn(true);

        ClienteResponseDTO resultado = clienteService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(cliente.getNome(), resultado.getNome());
        assertEquals(30, resultado.getIdade()); // Valida o cálculo da idade
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
    void buscarPorId_deveLancarExcecao_quandoIdNaoExiste() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.buscarPorId(99L);
        });
    }

    @Test
    @DisplayName("Deve adicionar cliente com sucesso")
    void adicionarCliente_deveRetornarClienteCriado_comDadosValidos() {
        when(clienteRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(clienteRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        ClienteResponseDTO resultado = clienteService.adicionarCliente(requestDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());

        assertEquals("11122233344", resultado.getCpf());
    }

    @Test
    @DisplayName("Não deve adicionar cliente com CPF duplicado")
    void adicionarCliente_deveLancarExcecao_quandoCpfJaExiste() {
        when(clienteRepository.findByCpf(anyString())).thenReturn(Optional.of(cliente));

        var exception = assertThrows(BusinessRuleException.class, () -> {
            clienteService.adicionarCliente(requestDTO);
        });

        assertEquals("CPF já cadastrado no sistema.", exception.getMessage());
        verify(clienteRepository, never()).save(any()); // Verifica que o save nunca foi chamado
    }

    @Test
    @DisplayName("Deve remover cliente com sucesso")
    void removerCliente_deveChamarMetodosCorretos_quandoIdExiste() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        // Usamos doNothing() para métodos void
        doNothing().when(autenticacaoService).removerUsuarioPorClienteId(1L);
        doNothing().when(clienteRepository).deleteById(1L);

        // Act
        clienteService.removerCliente(1L);

        // Assert
        verify(autenticacaoService, times(1)).removerUsuarioPorClienteId(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }
}