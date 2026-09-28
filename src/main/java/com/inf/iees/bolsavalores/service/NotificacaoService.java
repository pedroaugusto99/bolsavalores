package com.inf.iees.bolsavalores.service;

import com.inf.iees.bolsavalores.model.Cliente;
import com.inf.iees.bolsavalores.model.EventoMercado;
import com.inf.iees.bolsavalores.model.Notificacao;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final List<Notificacao> notificacoes = new CopyOnWriteArrayList<>();

    public void notificar(Cliente cliente, EventoMercado evento) {
        notificacoes.add(0, new Notificacao(cliente, evento));
        System.out.println("Notificando " + cliente.getNome() + ": " + descrever(evento) + ".");
    }

    public List<Notificacao> listar() {
        return List.copyOf(notificacoes);
    }

    public void limpar() {
        notificacoes.clear();
    }

    private String descrever(EventoMercado evento) {
        return evento.getBolsa() + " está em "
                + (evento.getVariacao() == VariacaoMercado.ALTA ? "alta" : "baixa");
    }
}
