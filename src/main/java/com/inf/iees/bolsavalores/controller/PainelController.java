package com.inf.iees.bolsavalores.controller;

import com.inf.iees.bolsavalores.dto.BovespaEventoRequest;
import com.inf.iees.bolsavalores.dto.MovimentoNasdaq;
import com.inf.iees.bolsavalores.dto.NasdaqEventoRequest;
import com.inf.iees.bolsavalores.integration.BovespaAdapter;
import com.inf.iees.bolsavalores.integration.NasdaqAdapter;
import com.inf.iees.bolsavalores.model.Cliente;
import com.inf.iees.bolsavalores.model.EventoMercado;
import com.inf.iees.bolsavalores.model.VariacaoMercado;
import com.inf.iees.bolsavalores.service.MercadoService;
import com.inf.iees.bolsavalores.service.NotificacaoService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PainelController {

    private final NasdaqAdapter nasdaqAdapter;
    private final BovespaAdapter bovespaAdapter;
    private final MercadoService mercadoService;
    private final NotificacaoService notificacaoService;

    public PainelController(
            NasdaqAdapter nasdaqAdapter,
            BovespaAdapter bovespaAdapter,
            MercadoService mercadoService,
            NotificacaoService notificacaoService) {
        this.nasdaqAdapter = nasdaqAdapter;
        this.bovespaAdapter = bovespaAdapter;
        this.mercadoService = mercadoService;
        this.notificacaoService = notificacaoService;
    }

    @GetMapping("/")
    public String exibirPainel(Model model) {
        model.addAttribute("notificacoes", notificacaoService.listar());
        return "painel";
    }

    @PostMapping("/painel/nasdaq")
    public String processarNasdaq(
            @RequestParam MovimentoNasdaq movement,
            RedirectAttributes redirectAttributes) {
        EventoMercado evento = nasdaqAdapter.paraEventoMercado(new NasdaqEventoRequest(movement));
        List<Cliente> notificados = mercadoService.processarEventoMercado(evento);
        adicionarMensagem(evento, notificados, redirectAttributes);
        return "redirect:/";
    }

    @PostMapping("/painel/bovespa")
    public String processarBovespa(
            @RequestParam VariacaoMercado variacao,
            RedirectAttributes redirectAttributes) {
        EventoMercado evento = bovespaAdapter.paraEventoMercado(new BovespaEventoRequest(variacao));
        List<Cliente> notificados = mercadoService.processarEventoMercado(evento);
        adicionarMensagem(evento, notificados, redirectAttributes);
        return "redirect:/";
    }

    @PostMapping("/painel/notificacoes/limpar")
    public String limparNotificacoes(RedirectAttributes redirectAttributes) {
        notificacaoService.limpar();
        redirectAttributes.addFlashAttribute("mensagem", "Histórico de notificações limpo.");
        return "redirect:/";
    }

    private void adicionarMensagem(
            EventoMercado evento,
            List<Cliente> notificados,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "mensagem",
                evento.getBolsa() + " - " + evento.getVariacao() + ": "
                        + notificados.size() + " cliente(s) notificado(s).");
    }
}
