package com.inf.iees.bolsavalores.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.Cliente;
import com.inf.iees.bolsavalores.modelo.EventoMercado;
import com.inf.iees.bolsavalores.modelo.TipoCliente;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MercadoServiceTest {

    private static final Cliente JOAO = new Cliente("João", TipoCliente.COMUM);

    private static final Cliente MARIA = new Cliente("Maria", TipoCliente.PREMIUM);

    private final ByteArrayOutputStream notificacoes = new ByteArrayOutputStream();

    private PrintStream saidaOriginal;

    private MercadoService mercadoService;

    @BeforeEach
    void prepararCenario() {
        saidaOriginal = System.out;
        System.setOut(new PrintStream(notificacoes, true, StandardCharsets.UTF_8));
        mercadoService = new MercadoService();
    }

    @AfterEach
    void restaurarSaida() {
        System.setOut(saidaOriginal);
    }

    @Test
    void deveNotificarTodosClientesQuandoNasdaqEstiverEmAlta() {
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(
                new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.ALTA));

        assertThat(clientesNotificados).containsExactlyInAnyOrder(JOAO, MARIA);
        assertThat(notificacoesEnviadas())
                .containsExactlyInAnyOrder(
                        "Notificando João: NASDAQ está em alta.",
                        "Notificando Maria: NASDAQ está em alta.");
    }

    @Test
    void deveNotificarSomenteClientesPremiumQuandoNasdaqEstiverEmBaixa() {
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(
                new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.BAIXA));

        assertThat(clientesNotificados).containsExactly(MARIA);
        assertThat(notificacoesEnviadas())
                .containsExactly("Notificando Maria: NASDAQ está em baixa.");
    }

    @Test
    void deveNotificarTodosClientesQuandoBovespaEstiverEmAlta() {
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(
                new EventoMercado(Bolsa.BOVESPA, VariacaoMercado.ALTA));

        assertThat(clientesNotificados).containsExactlyInAnyOrder(JOAO, MARIA);
        assertThat(notificacoesEnviadas())
                .containsExactlyInAnyOrder(
                        "Notificando João: BOVESPA está em alta.",
                        "Notificando Maria: BOVESPA está em alta.");
    }

    @Test
    void deveNotificarSomenteClientesPremiumQuandoBovespaEstiverEmBaixa() {
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(
                new EventoMercado(Bolsa.BOVESPA, VariacaoMercado.BAIXA));

        assertThat(clientesNotificados).containsExactly(MARIA);
        assertThat(notificacoesEnviadas())
                .containsExactly("Notificando Maria: BOVESPA está em baixa.");
    }

    @Test
    void naoDeveNotificarNenhumClienteQuandoNaoHouverAlteracao() {
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(
                new EventoMercado(Bolsa.NASDAQ, VariacaoMercado.SEM_ALTERACAO));

        assertThat(clientesNotificados).isEmpty();
        assertThat(notificacoesEnviadas()).isEmpty();
    }

    private List<String> notificacoesEnviadas() {
        String saida = notificacoes.toString(StandardCharsets.UTF_8).trim();
        return saida.isEmpty() ? List.of() : List.of(saida.split(System.lineSeparator()));
    }
}
