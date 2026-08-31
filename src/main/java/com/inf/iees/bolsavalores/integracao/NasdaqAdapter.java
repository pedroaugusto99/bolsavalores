package com.inf.iees.bolsavalores.integracao;

import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import org.springframework.stereotype.Component;

@Component
public class NasdaqAdapter implements IntegracaoBolsa {

    private final NasdaqClient nasdaqClient;

    public NasdaqAdapter(NasdaqClient nasdaqClient) {
        this.nasdaqClient = nasdaqClient;
    }

    @Override
    public Bolsa getBolsa() {
        return Bolsa.NASDAQ;
    }

    @Override
    public String obterMensagemMercado(VariacaoMercado variacao) {
        return nasdaqClient.enviarMensagemNasdaq(variacao == VariacaoMercado.ALTA ? "RISE" : "FALL");
    }
}
