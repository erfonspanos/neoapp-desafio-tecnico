package com.neoapp.desafio.api_clientes.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String msg) {
        super(msg);
    }
}
