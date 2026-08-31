package com.inf.iees.bolsavalores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Mensagem no contrato original da NASDAQ.")
public record NasdaqEventoRequest(

        @NotNull
        @Schema(description = "Movimento informado pela NASDAQ.", example = "UP")
        MovimentoNasdaq movement) {
}
