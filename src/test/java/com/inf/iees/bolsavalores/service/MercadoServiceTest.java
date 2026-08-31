package com.inf.iees.bolsavalores.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.inf.iees.bolsavalores.integracao.BovespaAdapter;
import com.inf.iees.bolsavalores.integracao.BovespaClient;
import com.inf.iees.bolsavalores.integracao.NasdaqAdapter;
import com.inf.iees.bolsavalores.integracao.NasdaqClient;
import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MercadoServiceTest {

    private final ByteArrayOutputStream notificacoes = new ByteArrayOutputStream();

    private PrintStream saidaOriginal;

    private MercadoService mercadoService;

    @BeforeEach
    void prepararCenario() {
        saidaOriginal = System.out;
        System.setOut(new PrintStream(notificacoes, true, StandardCharsets.UTF_8));
        mercadoService = new MercadoService(List.of(
                new NasdaqAdapter(new NasdaqClient()),
                new BovespaAdapter(new BovespaClient())));
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    @Test
    void deveNotificarTodosClientesQuandoNasdaqEstiverEmAlta() {
        mercadoService.processarEventoMercado(Bolsa.NASDAQ, VariacaoMercado.ALTA);

        assertThat(notificacoesEnviadas())
                .containsExactlyInAnyOrder(
                        "Notificando João: NASDAQ está em alta.",
                        "Notificando Maria: NASDAQ está em alta.");
    }

    @Test
    void deveNotificarSomenteClientesPremiumQuandoNasdaqEstiverEmBaixa() {
        mercadoService.processarEventoMercado(Bolsa.NASDAQ, VariacaoMercado.BAIXA);

        assertThat(notificacoesEnviadas())
                .containsExactly("Notificando Maria: NASDAQ está em baixa.");
    }

    @Test
    void deveNotificarTodosClientesQuandoBovespaEstiverEmAlta() {
        mercadoService.processarEventoMercado(Bolsa.BOVESPA, VariacaoMercado.ALTA);

        assertThat(notificacoesEnviadas())
                .containsExactlyInAnyOrder(
                        "Notificando João: BOVESPA está em alta.",
                        "Notificando Maria: BOVESPA está em alta.");
    }

    @Test
    void deveNotificarSomenteClientesPremiumQuandoBovespaEstiverEmBaixa() {
        mercadoService.processarEventoMercado(Bolsa.BOVESPA, VariacaoMercado.BAIXA);

        assertThat(notificacoesEnviadas())
                .containsExactly("Notificando Maria: BOVESPA está em baixa.");
    }

    @Test
    void naoDeveNotificarNenhumClienteQuandoNaoHouverAlteracao() {
        mercadoService.processarEventoMercado(Bolsa.NASDAQ, VariacaoMercado.SEM_ALTERACAO);

        assertThat(notificacoesEnviadas()).isEmpty();
    }

    private List<String> notificacoesEnviadas() {
        String saida = notificacoes.toString(StandardCharsets.UTF_8).trim();
        return saida.isEmpty() ? List.of() : List.of(saida.split(System.lineSeparator()));
    }
}
