package com.inf.iees.bolsavalores.service;

import com.inf.iees.bolsavalores.integracao.IntegracaoBolsa;
import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.Cliente;
import com.inf.iees.bolsavalores.modelo.TipoCliente;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MercadoService {

    private static final List<Cliente> CLIENTES = List.of(
            new Cliente("João", TipoCliente.COMUM),
            new Cliente("Maria", TipoCliente.PREMIUM));

    private final Map<Bolsa, IntegracaoBolsa> integracoes;

    public MercadoService(List<IntegracaoBolsa> integracoesDisponiveis) {
        this.integracoes = integracoesDisponiveis.stream()
                .collect(Collectors.toMap(IntegracaoBolsa::getBolsa, Function.identity()));
    }

    public void processarEventoMercado(Bolsa bolsa, VariacaoMercado variacao) {
        if (variacao == VariacaoMercado.SEM_ALTERACAO) {
            return;
        }

        String mensagemMercado = integracoes.get(bolsa).obterMensagemMercado(variacao);

        CLIENTES.stream()
                .filter(cliente -> deveNotificar(cliente, variacao))
                .forEach(cliente -> notificar(cliente, mensagemMercado));
    }

    private boolean deveNotificar(Cliente cliente, VariacaoMercado variacao) {
        return variacao == VariacaoMercado.ALTA || cliente.tipo() == TipoCliente.PREMIUM;
    }

    private void notificar(Cliente cliente, String mensagemMercado) {
        System.out.println("Notificando " + cliente.nome() + ": " + mensagemMercado + ".");
    }
}
