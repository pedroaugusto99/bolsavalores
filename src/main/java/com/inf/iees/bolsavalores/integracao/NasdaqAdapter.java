package com.inf.iees.bolsavalores.integracao;

import com.inf.iees.bolsavalores.dto.MovimentoNasdaq;
import com.inf.iees.bolsavalores.dto.NasdaqEventoRequest;
import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.EventoMercado;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;
import org.springframework.stereotype.Component;

@Component
public class NasdaqAdapter {

    public EventoMercado paraEventoMercado(NasdaqEventoRequest request) {
        return new EventoMercado(Bolsa.NASDAQ, converter(request.movement()));
    }

    private VariacaoMercado converter(MovimentoNasdaq movimento) {
        return switch (movimento) {
            case UP -> VariacaoMercado.ALTA;
            case DOWN -> VariacaoMercado.BAIXA;
            case UNCHANGED -> VariacaoMercado.SEM_ALTERACAO;
        };
    }
}
