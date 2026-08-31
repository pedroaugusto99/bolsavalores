# API Bolsa de Valores

API REST que recebe eventos das bolsas NASDAQ e BOVESPA e notifica os clientes de acordo com a variação do mercado.

Regra de negócio:

- ALTA: todos os clientes são notificados
- BAIXA: somente os clientes PREMIUM são notificados
- SEM_ALTERACAO: nenhum cliente é notificado

Os clientes ficam em memória (João como COMUM e Maria como PREMIUM). Não há banco de dados, autenticação nem frontend.

## Como rodar

Pré-requisitos: Java 21 e Maven.

Para iniciar a aplicação:

    mvn spring-boot:run

A aplicação sobe em http://localhost:8080

Ao abrir a raiz no navegador, você é redirecionado automaticamente para a documentação interativa (Swagger UI). É possível testar os endpoints por lá, pelo botão Try it out.

Para rodar os testes:

    mvn test

## Endpoints

Cada bolsa tem o seu próprio endpoint, porque cada uma envia mensagens em um formato diferente.

NASDAQ:

    POST /api/integracoes/nasdaq/eventos

    { "movement": "UP" }

Valores aceitos: UP, DOWN, UNCHANGED

BOVESPA:

    POST /api/integracoes/bovespa/eventos

    { "variacao": "ALTA" }

Valores aceitos: ALTA, BAIXA, SEM_ALTERACAO

A resposta informa quais clientes foram notificados:

    {
      "bolsa": "NASDAQ",
      "variacao": "ALTA",
      "clientesNotificados": [
        { "nome": "João", "tipo": "COMUM" },
        { "nome": "Maria", "tipo": "PREMIUM" }
      ]
    }

As notificações também aparecem no console da aplicação:

    Notificando João: NASDAQ está em alta.
    Notificando Maria: NASDAQ está em alta.

## Explicação arquitetural

O fluxo das duas bolsas é o mesmo:

    NASDAQ  -> NasdaqController  -> NasdaqAdapter  -> EventoMercado -> MercadoService -> notificação
    BOVESPA -> BovespaController -> BovespaAdapter -> EventoMercado -> MercadoService -> notificação

Os dois caminhos convergem para o mesmo serviço, então a regra de negócio existe em um lugar só e não fica duplicada.

Camadas:

controller: pontos de entrada HTTP. Cada bolsa tem o seu, porque cada uma tem um contrato próprio. O controller não tem regra de negócio, apenas recebe a requisição, chama o adapter e devolve o resultado.

dto: os contratos externos, no formato em que cada bolsa envia. A NASDAQ manda o campo movement com valores em inglês, a BOVESPA manda o campo variacao com valores em português. Aqui também fica o formato da resposta.

integration: os adapters, que traduzem cada contrato externo para o modelo interno.

model: o modelo interno da aplicação. EventoMercado é a representação padronizada, com bolsa e variacao.

service: o MercadoService, onde está a regra de notificação. Ele conhece apenas o EventoMercado e nunca os formatos da NASDAQ ou da BOVESPA.

## Padrão de projeto: Adapter

A NASDAQ e a BOVESPA são sistemas externos independentes e enviam mensagens em formatos incompatíveis entre si. Enviar o formato de uma no endpoint da outra retorna erro 400.

O NasdaqAdapter e o BovespaAdapter resolvem essa incompatibilidade: cada um converte o formato da sua bolsa para EventoMercado. Com isso o restante da aplicação trabalha com um formato único e não precisa saber de onde o evento veio.

Se no futuro entrar uma terceira bolsa, basta criar um controller e um adapter novos. O serviço e a regra de notificação continuam intactos.
