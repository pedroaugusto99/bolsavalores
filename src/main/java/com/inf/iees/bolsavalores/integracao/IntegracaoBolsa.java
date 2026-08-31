package com.inf.iees.bolsavalores.integracao;

import com.inf.iees.bolsavalores.modelo.Bolsa;
import com.inf.iees.bolsavalores.modelo.VariacaoMercado;

public interface IntegracaoBolsa {

    Bolsa getBolsa();

    String obterMensagemMercado(VariacaoMercado variacao);
}
