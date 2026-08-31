package com.inf.iees.bolsavalores.service;

import com.inf.iees.bolsavalores.model.Cliente;
import com.inf.iees.bolsavalores.model.EventoMercado;
import com.inf.iees.bolsavalores.model.TipoCliente;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MercadoService {

    private static final List<Cliente> CLIENTES = List.of(
            new Cliente("João", TipoCliente.COMUM),
            new Cliente("Maria", TipoCliente.PREMIUM));

    public List<Cliente> processarEventoMercado(EventoMercado evento) {
        if (evento.getVariacao() == VariacaoMercado.SEM_ALTERACAO) {
            return List.of();
        }

        List<Cliente> clientesNotificados = CLIENTES.stream()
                .filter(cliente -> deveNotificar(cliente, evento.getVariacao()))
                .toList();

        clientesNotificados.forEach(cliente -> notificar(cliente, evento));

        return clientesNotificados;
    }

    private boolean deveNotificar(Cliente cliente, VariacaoMercado variacao) {
        return variacao == VariacaoMercado.ALTA || cliente.getTipo() == TipoCliente.PREMIUM;
    }

    private void notificar(Cliente cliente, EventoMercado evento) {
        System.out.println("Notificando " + cliente.getNome() + ": " + descrever(evento) + ".");
    }

    private String descrever(EventoMercado evento) {
        return evento.getBolsa() + " está em "
                + (evento.getVariacao() == VariacaoMercado.ALTA ? "alta" : "baixa");
    }
}
