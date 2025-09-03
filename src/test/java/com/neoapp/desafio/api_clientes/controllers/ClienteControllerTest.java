package com.neoapp.desafio.api_clientes.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neoapp.desafio.api_clientes.dto.ClienteRequestDTO;
import com.neoapp.desafio.api_clientes.dto.ClienteResponseDTO;
import com.neoapp.desafio.api_clientes.exceptions.ResourceNotFoundException;
import com.neoapp.desafio.api_clientes.model.Endereco;
import com.neoapp.desafio.api_clientes.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 1. O CAMPO AGORA É INJETADO COM @Autowired
    // O Spring vai injetar o Mock que definimos na classe de configuração abaixo.
    @Autowired
    private ClienteService clienteService;

    // 2. CLASSE DE CONFIGURAÇÃO DE TESTE ANINHADA E ESTÁTICA
    @TestConfiguration
    static class ClienteControllerTestConfig {

        // 3. ESTE @Bean SUBSTITUI O BEAN REAL de ClienteService por um Mock do Mockito.
        @Bean
        @Primary
        public ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }
    }

    private ClienteRequestDTO requestDTO;
    private ClienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // O ObjectMapper precisa do módulo de data do Java 8 para converter LocalDate
        objectMapper.registerModule(new JavaTimeModule());

        Endereco endereco = new Endereco();
        endereco.setCep("60111-222");
        endereco.setLogradouro("Rua Teste");
        endereco.setNumero(123);
        endereco.setBairro("Bairro Teste");
        endereco.setCidade("Cidade Teste");
        endereco.setEstado("CE");

        requestDTO = new ClienteRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setCpf("111.222.333-44");
        requestDTO.setEmail("joao.silva@email.com");
        requestDTO.setTelefone("85988887777");
        requestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        requestDTO.setEndereco(endereco);

        responseDTO = new ClienteResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("João Silva");
    }

    @Test
    @DisplayName("Deve criar um cliente e retornar 201 Created quando autenticado como ADMIN")
    @WithMockUser(roles = "ADMIN")
    void adicionarCliente_comAdmin_deveRetornar201() throws Exception {
        when(clienteService.adicionarCliente(any(ClienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve retornar 403 Forbidden ao tentar criar cliente como CLIENTE")
    @WithMockUser(roles = "CLIENTE")
    void adicionarCliente_comCliente_deveRetornar403() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar criar cliente com dados inválidos")
    @WithMockUser(roles = "ADMIN")
    void adicionarCliente_comDadosInvalidos_deveRetornar400() throws Exception {
        requestDTO.setNome("J"); // Nome inválido (muito curto)

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve buscar cliente por ID e retornar 200 OK quando autenticado como ADMIN")
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_comAdmin_deveRetornar200() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found ao buscar cliente com ID inexistente")
    @WithMockUser(roles = "ADMIN")
    void buscarPorId_comIdInexistente_deveRetornar404() throws Exception {
        when(clienteService.buscarPorId(99L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());
    }
}