package com.futurevet.config;

import com.futurevet.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
@Profile("oracle")
public class PrimeiroAdmin implements CommandLineRunner {
    private final UsuarioRepository repo;
    private final Environment env;
    private final JdbcTemplate db;

    public PrimeiroAdmin(UsuarioRepository repo, Environment env, JdbcTemplate db) {
        this.repo = repo;
        this.env = env;
        this.db = db;
    }

    public void run(String... args) {
        String email = env.getProperty("FV_PRIMEIRO_ADMIN_EMAIL");
        if (email == null || email.isBlank() || repo.admins() > 0)
            return;
        var u = repo.email(email).orElseThrow(() -> new IllegalStateException(
                "O e-mail indicado para primeiro administrador ainda não está cadastrado."));
        db.update("UPDATE TB_USUARIO SET ROLE='ADMIN' WHERE ID_USUARIO=?", u.id());
    }
}
