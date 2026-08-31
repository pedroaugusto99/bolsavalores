package com.inf.iees.bolsavalores.controller;

import com.inf.iees.bolsavalores.dto.NasdaqEventoRequest;
import com.inf.iees.bolsavalores.dto.ProcessamentoEventoResponse;
import com.inf.iees.bolsavalores.integracao.NasdaqAdapter;
import com.inf.iees.bolsavalores.modelo.Cliente;
import com.inf.iees.bolsavalores.modelo.EventoMercado;
import com.inf.iees.bolsavalores.service.MercadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Integração NASDAQ",
        description = "Ponto de entrada das mensagens enviadas pelo subsistema externo da NASDAQ, "
                + "que utiliza contrato próprio.")
@RestController
@RequestMapping("/api/integracoes/nasdaq/eventos")
public class NasdaqController {

    private final NasdaqAdapter nasdaqAdapter;

    private final MercadoService mercadoService;

    public NasdaqController(NasdaqAdapter nasdaqAdapter, MercadoService mercadoService) {
        this.nasdaqAdapter = nasdaqAdapter;
        this.mercadoService = mercadoService;
    }

    @Operation(
            summary = "Receber evento da NASDAQ",
            description = "Recebe a mensagem no contrato original da NASDAQ, converte para o modelo "
                    + "interno através do NasdaqAdapter e processa as notificações. Em ALTA todos os "
                    + "clientes são notificados, em BAIXA somente os clientes PREMIUM e em "
                    + "SEM_ALTERACAO nenhum cliente é notificado.")
    @ApiResponse(
            responseCode = "200",
            description = "Evento processado. A resposta informa os clientes notificados.")
    @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida: movimento ausente ou com valor não reconhecido.",
            content = @Content)
    @PostMapping
    public ProcessamentoEventoResponse receberEvento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NasdaqEventoRequest.class),
                            examples = {
                                    @ExampleObject(name = "Alta", value = "{\"movement\": \"UP\"}"),
                                    @ExampleObject(name = "Baixa", value = "{\"movement\": \"DOWN\"}"),
                                    @ExampleObject(
                                            name = "Sem alteração",
                                            value = "{\"movement\": \"UNCHANGED\"}")
                            }))
            @Valid @RequestBody NasdaqEventoRequest request) {
        EventoMercado evento = nasdaqAdapter.paraEventoMercado(request);
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(evento);
        return new ProcessamentoEventoResponse(evento.bolsa(), evento.variacao(), clientesNotificados);
    }
}
