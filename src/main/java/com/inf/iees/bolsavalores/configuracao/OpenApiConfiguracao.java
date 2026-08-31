package com.inf.iees.bolsavalores.configuracao;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguracao {

    @Bean
    public OpenAPI apiBolsaValores() {
        return new OpenAPI().info(new Info()
                .title("API Bolsa de Valores")
                .description("API para processamento de eventos das bolsas NASDAQ e BOVESPA "
                        + "e notificação de clientes conforme a variação do mercado.")
                .version("1.0.0"));
    }
}
