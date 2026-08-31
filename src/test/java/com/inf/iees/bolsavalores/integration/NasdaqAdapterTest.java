package com.inf.iees.bolsavalores.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.inf.iees.bolsavalores.dto.MovimentoNasdaq;
import com.inf.iees.bolsavalores.dto.NasdaqEventoRequest;
import com.inf.iees.bolsavalores.model.Bolsa;
import com.inf.iees.bolsavalores.model.EventoMercado;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import org.junit.jupiter.api.Test;

class NasdaqAdapterTest {

    private final NasdaqAdapter nasdaqAdapter = new NasdaqAdapter();

    @Test
    void deveConverterMovimentoUpParaAltaDaNasdaq() {
        EventoMercado evento = nasdaqAdapter.paraEventoMercado(
                new NasdaqEventoRequest(MovimentoNasdaq.UP));

        assertThat(evento).isEqualTo(new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.ALTA));
    }

    @Test
    void deveConverterMovimentoDownParaBaixaDaNasdaq() {
        EventoMercado evento = nasdaqAdapter.paraEventoMercado(
                new NasdaqEventoRequest(MovimentoNasdaq.DOWN));

        assertThat(evento).isEqualTo(new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.BAIXA));
    }

    @Test
    void deveConverterMovimentoUnchangedParaSemAlteracaoDaNasdaq() {
        EventoMercado evento = nasdaqAdapter.paraEventoMercado(
                new NasdaqEventoRequest(MovimentoNasdaq.UNCHANGED));

        assertThat(evento).isEqualTo(new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.SEM_ALTERACAO));
    }
}
