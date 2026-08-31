package com.inf.iees.bolsavalores.integracao;

import org.springframework.stereotype.Component;

@Component
public class BovespaClient {

    public String publicarEventoBovespa(int codigoVariacao) {
        return "BOVESPA está em " + (codigoVariacao > 0 ? "alta" : "baixa");
    }
}
