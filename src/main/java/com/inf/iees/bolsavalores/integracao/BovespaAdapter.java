package com.inf.iees.bolsavalores.integracao;

import com.inf.iees.bolsavalores.dto.BovespaEventoRequest;
import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.EventoMercado;
import org.springframework.stereotype.Component;

@Component
public class BovespaAdapter {

    public EventoMercado paraEventoMercado(BovespaEventoRequest request) {
        return new EventoMercado(Bolsa.BOVESPA, request.variacao());
    }
}
