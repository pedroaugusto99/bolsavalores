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
class BovespaAdapterTest {

    @Mock
    private BovespaClient bovespaClient;

    @InjectMocks
    private BovespaAdapter bovespaAdapter;

    @Test
    void deveEnviarCodigoPositivoParaBovespaQuandoMercadoEstiverEmAlta() {
        when(bovespaClient.publicarEventoBovespa(1)).thenReturn("BOVESPA está em alta");

        String mensagemMercado = bovespaAdapter.obterMensagemMercado(VariacaoMercado.ALTA);

        verify(bovespaClient).publicarEventoBovespa(1);
        assertThat(mensagemMercado).isEqualTo("BOVESPA está em alta");
    }

    @Test
    void deveEnviarCodigoNegativoParaBovespaQuandoMercadoEstiverEmBaixa() {
        when(bovespaClient.publicarEventoBovespa(-1)).thenReturn("BOVESPA está em baixa");

        String mensagemMercado = bovespaAdapter.obterMensagemMercado(VariacaoMercado.BAIXA);

        verify(bovespaClient).publicarEventoBovespa(-1);
        assertThat(mensagemMercado).isEqualTo("BOVESPA está em baixa");
    }
}
