package com.inf.iees.bolsavalores.integration;

import com.inf.iees.bolsavalores.dto.BovespaEventoRequest;
import com.inf.iees.bolsavalores.model.Bolsa;
import com.inf.iees.bolsavalores.model.EventoMercado;
import org.springframework.stereotype.Component;

@Component
public class BovespaAdapter {

    public EventoMercado paraEventoMercado(BovespaEventoRequest request) {
        return new EventoMercado(Bolsa.BOVESPA, request.variacao());
    }
}
