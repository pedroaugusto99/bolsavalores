package com.inf.iees.bolsavalores.dto;

import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Evento enviado por uma bolsa de valores.")
public record EventoMercadoRequest(

        @NotNull
        @Schema(description = "Bolsa de valores responsável pelo evento.", example = "NASDAQ")
        Bolsa bolsa,

        @NotNull
        @Schema(description = "Variação apresentada pela bolsa de valores.", example = "ALTA")
        VariacaoMercado variacao) {
}
