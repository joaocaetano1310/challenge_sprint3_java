package com.futurevet.web;

import com.futurevet.service.*;
import com.futurevet.repository.UsuarioRepository;
import com.futurevet.security.Acesso;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.time.*;

@Controller
public class PainelController {
    private final AnimalService animais;
    private final AgendaService agenda;
    private final CarteiraService carteira;
    private final UsuarioRepository users;
    private final Acesso acesso;
    private final Clock clock;

    public PainelController(AnimalService animais, AgendaService agenda, CarteiraService carteira,
            UsuarioRepository users, Acesso acesso, Clock clock) {
        this.animais = animais;
        this.agenda = agenda;
        this.carteira = carteira;
        this.users = users;
        this.acesso = acesso;
        this.clock = clock;
    }

    @GetMapping("/")
    public String painel(Model m) {
        var pets = animais.listar();
        var doses = carteira.listar();
        var consultas = agenda.listar().stream()
                .filter(c -> c.ativa() && !c.data().isBefore(LocalDate.now(clock)))
                .sorted(java.util.Comparator.comparing(com.futurevet.model.Consulta::data)
                        .thenComparing(c -> c.hora() == null ? "99:99" : c.hora()))
                .toList();
        m.addAttribute("totalAnimais", pets.size());
        m.addAttribute("pendencias", doses.stream()
                .filter(i -> java.util.Set.of("ATRASADA", "PROXIMA").contains(i.situacao())).count());
        m.addAttribute("totalConsultas", consultas.size());
        m.addAttribute("consultas", consultas.stream().limit(5).toList());
        m.addAttribute("animais", pets.stream().limit(4).toList());
        return "painel";
    }

    @GetMapping("/admin/usuarios")
    public String usuarios(Model m) {
        acesso.admin();
        m.addAttribute("usuarios", users.todos());
        return "usuarios";
    }
}
