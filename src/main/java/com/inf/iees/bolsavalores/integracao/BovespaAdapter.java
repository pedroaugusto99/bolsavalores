package com.inf.iees.bolsavalores.integracao;

import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import org.springframework.stereotype.Component;

@Component
public class BovespaAdapter implements IntegracaoBolsa {

    private final BovespaClient bovespaClient;

    public BovespaAdapter(BovespaClient bovespaClient) {
        this.bovespaClient = bovespaClient;
    }

    @Override
    public Bolsa getBolsa() {
        return Bolsa.BOVESPA;
    }

    @Override
    public String obterMensagemMercado(VariacaoMercado variacao) {
        return bovespaClient.publicarEventoBovespa(variacao == VariacaoMercado.ALTA ? 1 : -1);
    }
}
