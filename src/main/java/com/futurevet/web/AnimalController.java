package com.futurevet.web;

import com.futurevet.service.*;
import com.futurevet.web.forms.AnimalForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/animais")
public class AnimalController {
    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model m) {
        m.addAttribute("animais", service.listar());
        return "animais";
    }

    @GetMapping("/novo")
    public String novo(Model m) {
        m.addAttribute("form", new AnimalForm());
        return "animal-form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable long id, Model m) {
        var a = service.buscar(id);
        AnimalForm f = new AnimalForm();
        f.setNome(a.nome());
        f.setEspecie(a.especie());
        f.setRaca(a.raca());
        f.setIdade(a.idade());
        f.setPorte(a.porte());
        f.setPeso(a.peso());
        m.addAttribute("form", f);
        m.addAttribute("id", id);
        return "animal-form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam(required = false) Long id, @Valid @ModelAttribute("form") AnimalForm f,
            BindingResult b, Model m, RedirectAttributes r) {
        if (id != null)
            service.buscar(id);
        m.addAttribute("id", id);
        if (b.hasErrors())
            return "animal-form";
        service.salvar(id, f);
        r.addFlashAttribute("sucesso", "Animal salvo com sucesso.");
        return "redirect:/animais";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable long id, RedirectAttributes r) {
        try {
            service.excluir(id);
            r.addFlashAttribute("sucesso", "Animal excluído.");
        } catch (RegraException e) {
            r.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/animais";
    }
}
