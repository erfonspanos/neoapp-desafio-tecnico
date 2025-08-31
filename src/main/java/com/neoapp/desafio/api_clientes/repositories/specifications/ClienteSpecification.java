package com.neoapp.desafio.api_clientes.repositories.specifications;

import com.neoapp.desafio.api_clientes.model.Cliente;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ClienteSpecification {

    public static Specification<Cliente> trueCondition() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

    public static Specification<Cliente> comNome(String nome) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Cliente> comCpf(String cpf) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("cpf"), cpf);
    }

    public static Specification<Cliente> comEmail(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Cliente> comTelefone(String telefone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("telefone"), telefone);
    }

    public static Specification<Cliente> comDataNascimento(LocalDate dataNascimento) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dataNascimento"), dataNascimento);
    }

    public static Specification<Cliente> comCep(String cep) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("endereco").get("cep"), cep);
    }

    public static Specification<Cliente> comLogradouro(String logradouro) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("endereco").get("logradouro")), "%" + logradouro.toLowerCase() + "%");
    }

    public static Specification<Cliente> comCidade(String cidade) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("endereco").get("cidade")), "%" + cidade.toLowerCase() + "%");
    }

    public static Specification<Cliente> comEstado(String estado) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("endereco").get("estado")), estado.toLowerCase());
    }
}
