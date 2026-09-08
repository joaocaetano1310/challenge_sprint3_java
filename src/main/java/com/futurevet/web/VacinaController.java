package com.futurevet.web;

import com.futurevet.service.*;
import com.futurevet.web.forms.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/vacinas")
public class VacinaController {
    private final CarteiraService service;
    private final AnimalService animais;

    public VacinaController(CarteiraService service, AnimalService animais) {
        this.service = service;
        this.animais = animais;
    }

    @GetMapping
    public String listar(Model m) {
        var itens = service.listar();
        m.addAttribute("itens", itens);
        m.addAttribute("atrasadas", itens.stream().filter(i -> i.situacao().equals("ATRASADA")).count());
        m.addAttribute("proximas", itens.stream().filter(i -> i.situacao().equals("PROXIMA")).count());
        return "vacinas";
    }

    @GetMapping("/nova")
    public String nova(Model m) {
        m.addAttribute("form", new VacinaForm());
        m.addAttribute("animais", animais.listar());
        return "vacina-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable long id, Model m) {
        var v = service.buscar(id);
        VacinaForm f = new VacinaForm();
        f.setAnimalId(v.animalId());
        f.setNome(v.nome());
        f.setData(v.data());
        f.setProximaDose(v.proximaDose());
        f.setLocal(v.local());
        m.addAttribute("form", f);
        m.addAttribute("id", id);
        m.addAttribute("animais", animais.listar());
        return "vacina-form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id, @Valid @ModelAttribute("form") VacinaForm f,
            BindingResult b, Model m, RedirectAttributes r) {
        if (id != null)
            service.buscar(id);
        m.addAttribute("id", id);
        m.addAttribute("animais", animais.listar());
        if (b.hasErrors())
            return "vacina-form";
        try {
            service.salvar(id, f);
        } catch (RegraException e) {
            b.reject("regra", e.getMessage());
            return "vacina-form";
        }
        r.addFlashAttribute("sucesso", "Vacina salva na carteira.");
        return "redirect:/vacinas";
    }

    @GetMapping("/{id}/reforco")
    public String reforco(@PathVariable long id, Model m) {
        m.addAttribute("dose", service.buscar(id));
        m.addAttribute("form", new ReforcoForm());
        return "reforco-form";
    }

    @PostMapping("/{id}/reforco")
    public String reforcar(@PathVariable long id, @Valid @ModelAttribute("form") ReforcoForm f,
            BindingResult b, Model m, RedirectAttributes r) {
        m.addAttribute("dose", service.buscar(id));
        if (b.hasErrors())
            return "reforco-form";
        try {
            service.reforcar(id, f);
        } catch (RegraException e) {
            b.reject("regra", e.getMessage());
            return "reforco-form";
        }
        r.addFlashAttribute("sucesso",
                "Reforço registrado. Próxima dose calculada e dose anterior preservada no histórico.");
        return "redirect:/vacinas";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable long id, RedirectAttributes r) {
        try {
            service.excluir(id);
            r.addFlashAttribute("sucesso", "Vacina excluída.");
        } catch (RegraException e) {
            r.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/vacinas";
    }
}
