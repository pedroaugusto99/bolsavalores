package com.inf.iees.bolsavalores.integracao;

import org.springframework.stereotype.Component;

@Component
public class NasdaqClient {

    public String enviarMensagemNasdaq(String status) {
        return "NASDAQ está em " + ("RISE".equals(status) ? "alta" : "baixa");
    }
}
