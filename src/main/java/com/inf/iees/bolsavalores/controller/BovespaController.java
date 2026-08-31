package com.inf.iees.bolsavalores.controller;

import com.inf.iees.bolsavalores.dto.BovespaEventoRequest;
import com.inf.iees.bolsavalores.dto.ProcessamentoEventoResponse;
import com.inf.iees.bolsavalores.integration.BovespaAdapter;
import com.inf.iees.bolsavalores.model.Cliente;
import com.inf.iees.bolsavalores.model.EventoMercado;
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
        name = "Integração BOVESPA",
        description = "Ponto de entrada das mensagens enviadas pelo subsistema externo da BOVESPA, "
                + "que utiliza contrato próprio.")
@RestController
@RequestMapping("/api/integracoes/bovespa/eventos")
public class BovespaController {

    private final BovespaAdapter bovespaAdapter;

    private final MercadoService mercadoService;

    public BovespaController(BovespaAdapter bovespaAdapter, MercadoService mercadoService) {
        this.bovespaAdapter = bovespaAdapter;
        this.mercadoService = mercadoService;
    }

    @Operation(
            summary = "Receber evento da BOVESPA",
            description = "Recebe a mensagem no contrato original da BOVESPA, converte para o modelo "
                    + "interno através do BovespaAdapter e processa as notificações. Em ALTA todos os "
                    + "clientes são notificados, em BAIXA somente os clientes PREMIUM e em "
                    + "SEM_ALTERACAO nenhum cliente é notificado.")
    @ApiResponse(
            responseCode = "200",
            description = "Evento processado. A resposta informa os clientes notificados.")
    @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida: variação ausente ou com valor não reconhecido.",
            content = @Content)
    @PostMapping
    public ProcessamentoEventoResponse receberEvento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = BovespaEventoRequest.class),
                            examples = {
                                    @ExampleObject(name = "Alta", value = "{\"variacao\": \"ALTA\"}"),
                                    @ExampleObject(name = "Baixa", value = "{\"variacao\": \"BAIXA\"}"),
                                    @ExampleObject(
                                            name = "Sem alteração",
                                            value = "{\"variacao\": \"SEM_ALTERACAO\"}")
                            }))
            @Valid @RequestBody BovespaEventoRequest request) {
        EventoMercado evento = bovespaAdapter.paraEventoMercado(request);
        List<Cliente> clientesNotificados = mercadoService.processarEventoMercado(evento);
        return new ProcessamentoEventoResponse(evento.getBolsa(), evento.getVariacao(), clientesNotificados);
    }
}
