package com.inf.iees.bolsavalores.dto;

import com.inf.iees.bolsavalores.model.Bolsa;
import com.inf.iees.bolsavalores.model.Cliente;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resultado do processamento do evento de mercado.")
public record ProcessamentoEventoResponse(

        @Schema(description = "Bolsa que originou o evento.")
        Bolsa bolsa,

        @Schema(description = "Variação já convertida para o modelo interno.")
        VariacaoMercado variacao,

        @Schema(description = "Clientes efetivamente notificados neste processamento.")
        List<Cliente> clientesNotificados) {
}
