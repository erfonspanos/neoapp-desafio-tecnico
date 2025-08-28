package com.neoapp.desafio.api_clientes.dto;

import com.neoapp.desafio.api_clientes.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private int idade;
    private LocalDateTime dataCadastro;
    private Endereco endereco;

}
