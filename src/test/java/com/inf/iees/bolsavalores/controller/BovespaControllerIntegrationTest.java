package com.inf.iees.bolsavalores.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BovespaControllerIntegrationTest {

    private static final String ENDPOINT = "/api/integracoes/bovespa/eventos";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveNotificarTodosClientesQuandoVariacaoForAlta() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variacao\":\"ALTA\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bolsa").value("BOVESPA"))
                .andExpect(jsonPath("$.variacao").value("ALTA"))
                .andExpect(jsonPath("$.clientesNotificados").isArray())
                .andExpect(jsonPath("$.clientesNotificados.length()").value(2))
                .andExpect(jsonPath("$.clientesNotificados[0].nome").value("João"))
                .andExpect(jsonPath("$.clientesNotificados[0].tipo").value("COMUM"))
                .andExpect(jsonPath("$.clientesNotificados[1].nome").value("Maria"))
                .andExpect(jsonPath("$.clientesNotificados[1].tipo").value("PREMIUM"));
    }

    @Test
    void deveNotificarSomenteClientePremiumQuandoVariacaoForBaixa() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variacao\":\"BAIXA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bolsa").value("BOVESPA"))
                .andExpect(jsonPath("$.variacao").value("BAIXA"))
                .andExpect(jsonPath("$.clientesNotificados.length()").value(1))
                .andExpect(jsonPath("$.clientesNotificados[0].nome").value("Maria"))
                .andExpect(jsonPath("$.clientesNotificados[0].tipo").value("PREMIUM"));
    }

    @Test
    void naoDeveNotificarClientesQuandoVariacaoForSemAlteracao() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variacao\":\"SEM_ALTERACAO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bolsa").value("BOVESPA"))
                .andExpect(jsonPath("$.variacao").value("SEM_ALTERACAO"))
                .andExpect(jsonPath("$.clientesNotificados").isEmpty());
    }

    @Test
    void deveRetornarBadRequestQuandoPayloadEstiverVazio() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoCampoVariacaoEstiverAusente() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoVariacaoForInvalida() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"variacao\":\"SUBIU\"}"))
                .andExpect(status().isBadRequest());
    }
}
