package com.inf.iees.bolsavalores.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Notificacao {

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final Cliente cliente;
    private final Bolsa bolsa;
    private final VariacaoMercado variacao;
    private final LocalDateTime horario;

    public Notificacao(Cliente cliente, EventoMercado evento) {
        this.cliente = cliente;
        this.bolsa = evento.getBolsa();
        this.variacao = evento.getVariacao();
        this.horario = LocalDateTime.now();
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Bolsa getBolsa() {
        return bolsa;
    }

    public VariacaoMercado getVariacao() {
        return variacao;
    }

    public String getHorarioFormatado() {
        return horario.format(FORMATADOR);
    }

    public String getMensagem() {
        return bolsa + " está em " + (variacao == VariacaoMercado.ALTA ? "alta" : "baixa");
    }
}
