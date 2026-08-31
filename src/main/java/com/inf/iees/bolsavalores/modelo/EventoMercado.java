package com.inf.iees.bolsavalores.modelo;

import java.util.Objects;

public class EventoMercado {

    private final Bolsa bolsa;

    private final VariacaoMercado variacao;

    public EventoMercado(Bolsa bolsa, VariacaoMercado variacao) {
        this.bolsa = bolsa;
        this.variacao = variacao;
    }

    public Bolsa getBolsa() {
        return bolsa;
    }

    public VariacaoMercado getVariacao() {
        return variacao;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof EventoMercado outro)) {
            return false;
        }
        return bolsa == outro.bolsa && variacao == outro.variacao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bolsa, variacao);
    }

    @Override
    public String toString() {
        return "EventoMercado{bolsa=" + bolsa + ", variacao=" + variacao + "}";
    }
}
