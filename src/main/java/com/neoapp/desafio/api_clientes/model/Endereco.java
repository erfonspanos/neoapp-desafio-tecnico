package com.neoapp.desafio.api_clientes.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Embeddable
@Getter
@Setter
public class Endereco {

    @NotBlank(message = "O CEP é obrigatório")
    @Length(min = 8, max = 9, message = "O CEP deve conter 8 dígitos")
    private String cep;

    @NotBlank(message = "O logradouro é obrigatório")
    private String logradouro;

    @NotNull(message = "O número é obrigatório")
    private int numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    private String estado;
}
