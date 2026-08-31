package com.inf.iees.bolsavalores.integracao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NasdaqAdapterTest {

    @Mock
    private NasdaqClient nasdaqClient;

    @InjectMocks
    private NasdaqAdapter nasdaqAdapter;

    @Test
    void deveEnviarStatusDeAltaParaNasdaqQuandoMercadoEstiverEmAlta() {
        when(nasdaqClient.enviarMensagemNasdaq("RISE")).thenReturn("NASDAQ está em alta");

        String mensagemMercado = nasdaqAdapter.obterMensagemMercado(VariacaoMercado.ALTA);

        verify(nasdaqClient).enviarMensagemNasdaq("RISE");
        assertThat(mensagemMercado).isEqualTo("NASDAQ está em alta");
    }

    @Test
    void deveEnviarStatusDeBaixaParaNasdaqQuandoMercadoEstiverEmBaixa() {
        when(nasdaqClient.enviarMensagemNasdaq("FALL")).thenReturn("NASDAQ está em baixa");

        String mensagemMercado = nasdaqAdapter.obterMensagemMercado(VariacaoMercado.BAIXA);

        verify(nasdaqClient).enviarMensagemNasdaq("FALL");
        assertThat(mensagemMercado).isEqualTo("NASDAQ está em baixa");
    }
}
