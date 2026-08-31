package com.inf.iees.bolsavalores.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.inf.iees.bolsavalores.dto.BovespaEventoRequest;
import com.inf.iees.bolsavalores.model.Bolsa;
import com.inf.iees.bolsavalores.model.EventoMercado;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import org.junit.jupiter.api.Test;

class BovespaAdapterTest {

    private final BovespaAdapter bovespaAdapter = new BovespaAdapter();

    @Test
    void deveConverterVariacaoAltaParaEventoDaBovespa() {
        EventoMercado evento = bovespaAdapter.paraEventoMercado(
                new BovespaEventoRequest(VariacaoMercado.ALTA));

        assertThat(evento).isEqualTo(new EventoMercado(Bolsa.BOVESPA, VariacaoMercado.ALTA));
    }

    @Test
    void deveConverterVariacaoBaixaParaEventoDaBovespa() {
        EventoMercado evento = bovespaAdapter.paraEventoMercado(
                new BovespaEventoRequest(VariacaoMercado.BAIXA));

        assertThat(evento).isEqualTo(new EventoMercado(Bolsa.BOVESPA, VariacaoMercado.BAIXA));
    }
}
