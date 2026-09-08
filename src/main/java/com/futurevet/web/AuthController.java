package com.futurevet.web;

import com.futurevet.service.*;
import com.futurevet.web.forms.RegistroForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.core.env.Environment;

@Controller
public class AuthController {
    private final UsuarioService users;
    private final Environment env;

    public AuthController(UsuarioService users, Environment env) {
        this.users = users;
        this.env = env;
    }

    @GetMapping("/login")
    public String login(Model m) {
        m.addAttribute("demo", env.acceptsProfiles(org.springframework.core.env.Profiles.of("demo")));
        return "login";
    }

    @GetMapping("/registro")
    public String registro(Model m) {
        m.addAttribute("form", new RegistroForm());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("form") RegistroForm f, BindingResult b) {
        if (b.hasErrors())
            return "registro";
        try {
            users.registrar(f);
        } catch (RegraException e) {
            b.reject("regra", e.getMessage());
            return "registro";
        }
        return "redirect:/login?cadastro";
    }

    @GetMapping("/acesso-negado")
    @ResponseStatus(org.springframework.http.HttpStatus.FORBIDDEN)
    public String negado(Model m) {
        m.addAttribute("mensagem", "Esta área é exclusiva da clínica.");
        return "erro";
    }
}
