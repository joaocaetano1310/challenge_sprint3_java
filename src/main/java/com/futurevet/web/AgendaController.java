package com.futurevet.web;

import com.futurevet.service.*;
import com.futurevet.web.forms.AgendaForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/consultas")
public class AgendaController {
    private final AgendaService service;
    private final AnimalService animais;

    public AgendaController(AgendaService service, AnimalService animais) {
        this.service = service;
        this.animais = animais;
    }

    @GetMapping
    public String listar(Model m) {
        m.addAttribute("consultas", service.listar());
        return "consultas";
    }

    @GetMapping("/nova")
    public String nova(Model m) {
        m.addAttribute("form", new AgendaForm());
        m.addAttribute("animais", animais.listar());
        return "consulta-form";
    }

    @GetMapping("/{id}/reagendar")
    public String editar(@PathVariable long id, Model m) {
        var c = service.buscar(id);
        AgendaForm f = new AgendaForm();
        f.setAnimalId(c.animalId());
        f.setTipo(c.tipo());
        f.setData(c.data());
        f.setHora(c.hora() == null ? null : java.time.LocalTime.parse(c.hora()));
        f.setLocal(c.local());
        f.setObservacao(c.observacao());
        m.addAttribute("form", f);
        m.addAttribute("id", id);
        m.addAttribute("animais", animais.listar());
        return "consulta-form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id, @Valid @ModelAttribute("form") AgendaForm f,
            BindingResult b, Model m, RedirectAttributes r) {
        if (id != null)
            service.buscar(id);
        m.addAttribute("id", id);
        m.addAttribute("animais", animais.listar());
        if (b.hasErrors())
            return "consulta-form";
        try {
            if (id == null)
                service.agendar(f);
            else
                service.reagendar(id, f);
        } catch (RegraException e) {
            b.reject("regra", e.getMessage());
            return "consulta-form";
        }
        r.addFlashAttribute("sucesso", id == null ? "Solicitação enviada. Aguarde a confirmação da clínica."
                : "Consulta reagendada. Aguarde nova confirmação.");
        return "redirect:/consultas";
    }

    @PostMapping("/{id}/confirmar")
    public String confirmar(@PathVariable long id, RedirectAttributes r) {
        return executar(() -> service.confirmar(id), "Consulta confirmada.", r);
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable long id, @RequestParam String motivo, RedirectAttributes r) {
        return executar(() -> service.cancelar(id, motivo), "Consulta cancelada. Horário liberado.", r);
    }

    @PostMapping("/{id}/concluir")
    public String concluir(@PathVariable long id, @RequestParam String observacao, RedirectAttributes r) {
        return executar(() -> service.concluir(id, observacao), "Atendimento concluído e registrado.", r);
    }

    private String executar(Runnable operacao, String mensagem, RedirectAttributes r) {
        try {
            operacao.run();
            r.addFlashAttribute("sucesso", mensagem);
        } catch (RegraException e) {
            r.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/consultas";
    }
}
