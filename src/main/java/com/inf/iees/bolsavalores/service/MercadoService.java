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

    private final NotificacaoService notificacaoService;

    public MercadoService(NotificacaoService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    public List<Cliente> processarEventoMercado(EventoMercado evento) {
        if (evento.getVariacao() == VariacaoMercado.SEM_ALTERACAO) {
            return List.of();
        }

        List<Cliente> clientesNotificados = CLIENTES.stream()
                .filter(cliente -> deveNotificar(cliente, evento.getVariacao()))
                .toList();

        clientesNotificados.forEach(cliente -> notificacaoService.notificar(cliente, evento));

        return clientesNotificados;
    }

    private boolean deveNotificar(Cliente cliente, VariacaoMercado variacao) {
        return variacao == VariacaoMercado.ALTA || cliente.getTipo() == TipoCliente.PREMIUM;
    }
}
