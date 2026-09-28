package com.inf.iees.bolsavalores.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.inf.iees.bolsavalores.model.Bolsa;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import com.inf.iees.bolsavalores.service.NotificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PainelControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificacaoService notificacaoService;

    @BeforeEach
    void limparHistorico() {
        notificacaoService.limpar();
    }

    @Test
    void deveExibirPainel() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("painel"))
                .andExpect(content().string(containsString("Painel de Bolsa de Valores")))
                .andExpect(content().string(containsString("Abrir Swagger")));
    }

    @Test
    void deveProcessarEventoDaNasdaqERegistrarNotificacoes() throws Exception {
        mockMvc.perform(post("/painel/nasdaq")
                        .param("movement", "UP"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute(
                        "mensagem",
                        "NASDAQ - ALTA: 2 cliente(s) notificado(s)."));

        assertThat(notificacaoService.listar())
                .hasSize(2)
                .allSatisfy(notificacao -> {
                    assertThat(notificacao.getBolsa()).isEqualTo(Bolsa.NASDAQ);
                    assertThat(notificacao.getVariacao()).isEqualTo(VariacaoMercado.ALTA);
                });
        assertThat(notificacaoService.listar())
                .extracting(notificacao -> notificacao.getCliente().getNome())
                .containsExactlyInAnyOrder("João", "Maria");
    }

    @Test
    void deveProcessarEventoDaBovespaERegistrarSomenteClientePremium() throws Exception {
        mockMvc.perform(post("/painel/bovespa")
                        .param("variacao", "BAIXA"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute(
                        "mensagem",
                        "BOVESPA - BAIXA: 1 cliente(s) notificado(s)."));

        assertThat(notificacaoService.listar())
                .singleElement()
                .satisfies(notificacao -> {
                    assertThat(notificacao.getCliente().getNome()).isEqualTo("Maria");
                    assertThat(notificacao.getVariacao()).isEqualTo(VariacaoMercado.BAIXA);
                });
    }

    @Test
    void deveLimparHistoricoDeNotificacoes() throws Exception {
        mockMvc.perform(post("/painel/nasdaq")
                        .param("movement", "DOWN"))
                .andExpect(status().is3xxRedirection());

        assertThat(notificacaoService.listar()).isNotEmpty();

        mockMvc.perform(post("/painel/notificacoes/limpar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute(
                        "mensagem",
                        "Histórico de notificações limpo."));

        assertThat(notificacaoService.listar()).isEmpty();
    }
}
