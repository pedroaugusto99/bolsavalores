package com.inf.iees.bolsavalores.dto;

import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Mensagem no contrato original da BOVESPA.")
public record BovespaEventoRequest(

        @NotNull
        @Schema(description = "Variação informada pela BOVESPA.", example = "ALTA")
        VariacaoMercado variacao) {
}
