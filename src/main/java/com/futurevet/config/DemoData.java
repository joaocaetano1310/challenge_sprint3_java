package com.futurevet.config;

import com.futurevet.repository.*;
import com.futurevet.web.forms.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.*;

@Component
@Profile("demo")
public class DemoData implements CommandLineRunner {
    private final UsuarioRepository users;
    private final AnimalRepository animais;
    private final ConsultaRepository consultas;
    private final VacinaRepository vacinas;
    private final PasswordEncoder encoder;
    private final Clock clock;

    public DemoData(UsuarioRepository users, AnimalRepository animais, ConsultaRepository consultas,
            VacinaRepository vacinas, PasswordEncoder encoder, Clock clock) {
        this.users = users;
        this.animais = animais;
        this.consultas = consultas;
        this.vacinas = vacinas;
        this.encoder = encoder;
        this.clock = clock;
    }

    public void run(String... args) {
        if (!users.todos().isEmpty())
            return;
        users.criar("Clínica FutureVet", "clinica@futurevet.local", encoder.encode("Futurevet123!"),
                90000000001L, null, "ADMIN");
        long tutor = users.criar("Marina Costa", "tutor@futurevet.local", encoder.encode("Futurevet123!"),
                90000000002L, 11988887777L, "TUTOR");
        users.criar("Outro Tutor", "outro@futurevet.local", encoder.encode("Futurevet123!"), 90000000003L,
                null, "TUTOR");
        AnimalForm a = new AnimalForm();
        a.setNome("Bento");
        a.setEspecie("CAO");
        a.setIdade("3 anos");
        a.setPorte("MEDIO");
        a.setRaca("Sem raça definida");
        a.setPeso(new java.math.BigDecimal("12.50"));
        long id = animais.criar(a, tutor);
        a.setNome("Luna");
        a.setEspecie("GATO");
        a.setIdade("6 meses");
        a.setPorte("PEQUENO");
        a.setPeso(new java.math.BigDecimal("3.20"));
        long luna = animais.criar(a, tutor);
        for (int i = 0; i < 3; i++) {
            VacinaForm v = new VacinaForm();
            v.setAnimalId(i == 2 ? luna : id);
            v.setNome(new String[] { "V10", "Antirrábica", "V4 Felina" }[i]);
            v.setData(LocalDate.now(clock).minusMonths(12));
            v.setProximaDose(i == 2 ? null : LocalDate.now(clock).plusDays(i == 0 ? -10 : 20));
            v.setLocal("Clínica FutureVet");
            vacinas.criar(v, null);
        }
        AgendaForm c = new AgendaForm();
        c.setAnimalId(id);
        c.setTipo("Consulta preventiva");
        c.setData(LocalDate.now(clock).plusDays(2));
        c.setHora(LocalTime.of(10, 0));
        c.setLocal("Consultório 1");
        consultas.criar(c);
    }
}
