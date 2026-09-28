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
class NasdaqControllerIntegrationTest {

    private static final String ENDPOINT = "/api/integracoes/nasdaq/eventos";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deveNotificarTodosClientesQuandoMovimentoForUp() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movement\":\"UP\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bolsa").value("NASDAQ"))
                .andExpect(jsonPath("$.variacao").value("ALTA"))
                .andExpect(jsonPath("$.clientesNotificados").isArray())
                .andExpect(jsonPath("$.clientesNotificados.length()").value(2))
                .andExpect(jsonPath("$.clientesNotificados[0].nome").value("João"))
                .andExpect(jsonPath("$.clientesNotificados[0].tipo").value("COMUM"))
                .andExpect(jsonPath("$.clientesNotificados[1].nome").value("Maria"))
                .andExpect(jsonPath("$.clientesNotificados[1].tipo").value("PREMIUM"));
    }

    @Test
    void deveNotificarSomenteClientePremiumQuandoMovimentoForDown() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movement\":\"DOWN\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bolsa").value("NASDAQ"))
                .andExpect(jsonPath("$.variacao").value("BAIXA"))
                .andExpect(jsonPath("$.clientesNotificados.length()").value(1))
                .andExpect(jsonPath("$.clientesNotificados[0].nome").value("Maria"))
                .andExpect(jsonPath("$.clientesNotificados[0].tipo").value("PREMIUM"));
    }

    @Test
    void naoDeveNotificarClientesQuandoMovimentoForUnchanged() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movement\":\"UNCHANGED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bolsa").value("NASDAQ"))
                .andExpect(jsonPath("$.variacao").value("SEM_ALTERACAO"))
                .andExpect(jsonPath("$.clientesNotificados").isEmpty());
    }

    @Test
    void deveRetornarBadRequestQuandoPayloadEstiverAusente() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoCampoMovementEstiverAusente() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoMovementForInvalido() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movement\":\"SIDEWAYS\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarBadRequestQuandoJsonEstiverMalformado() throws Exception {
        mockMvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movement\":"))
                .andExpect(status().isBadRequest());
    }
}
