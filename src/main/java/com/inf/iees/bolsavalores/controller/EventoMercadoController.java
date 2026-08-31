package com.inf.iees.bolsavalores.controller;

import com.inf.iees.bolsavalores.dto.EventoMercadoRequest;
import com.inf.iees.bolsavalores.service.MercadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Eventos de Mercado", description = "Recebimento dos eventos das bolsas de valores.")
@RestController
@RequestMapping("/api/eventos-mercado")
public class EventoMercadoController {

    private final MercadoService mercadoService;

    public EventoMercadoController(MercadoService mercadoService) {
        this.mercadoService = mercadoService;
    }

    @Operation(
            summary = "Processar evento de mercado",
            description = "Recebe uma atualização de uma bolsa de valores e processa as notificações "
                    + "dos clientes de acordo com a variação informada. Em ALTA todos os clientes são "
                    + "notificados, em BAIXA somente os clientes PREMIUM são notificados e em "
                    + "SEM_ALTERACAO nenhuma notificação é realizada.")
    @ApiResponse(responseCode = "200", description = "Evento de mercado processado com sucesso.")
    @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida: bolsa ou variação ausente ou com valor não reconhecido.",
            content = @Content)
    @PostMapping
    public Map<String, String> receberEventoMercado(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EventoMercadoRequest.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Bolsa em alta",
                                            value = "{\"bolsa\": \"NASDAQ\", \"variacao\": \"ALTA\"}"),
                                    @ExampleObject(
                                            name = "Bolsa em baixa",
                                            value = "{\"bolsa\": \"BOVESPA\", \"variacao\": \"BAIXA\"}")
                            }))
            @Valid @RequestBody EventoMercadoRequest request) {
        mercadoService.processarEventoMercado(request.bolsa(), request.variacao());
        return Map.of("mensagem", "Evento de mercado processado com sucesso");
    }
}
