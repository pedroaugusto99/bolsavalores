package com.inf.iees.bolsavalores.modelo;

import java.util.Objects;

public class Cliente {

    private final String nome;

    private final TipoCliente tipo;

    public Cliente(String nome, TipoCliente tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Cliente outro)) {
            return false;
        }
        return Objects.equals(nome, outro.nome) && tipo == outro.tipo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tipo);
    }

    @Override
    public String toString() {
        return "Cliente{nome=" + nome + ", tipo=" + tipo + "}";
    }
}
