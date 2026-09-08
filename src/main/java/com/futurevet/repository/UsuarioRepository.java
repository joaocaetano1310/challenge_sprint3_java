package com.futurevet.repository;

import com.futurevet.model.Usuario;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class UsuarioRepository {
    private final JdbcTemplate db;
    private final Ids ids;

    public UsuarioRepository(JdbcTemplate db, Ids ids) {
        this.db = db;
        this.ids = ids;
    }

    private final RowMapper<Usuario> mapper = (r, n) -> new Usuario(r.getLong("ID_USUARIO"),
            r.getString("NOME"), r.getString("EMAIL"), r.getString("SENHA"), r.getLong("CPF"),
            r.getObject("TELEFONE", Long.class), r.getString("ROLE"));

    public Optional<Usuario> email(String email) {
        return db.query("SELECT * FROM TB_USUARIO WHERE LOWER(EMAIL)=LOWER(?)", mapper, email).stream()
                .findFirst();
    }

    public boolean duplicado(String email, long cpf) {
        return db.queryForObject("SELECT COUNT(*) FROM TB_USUARIO WHERE LOWER(EMAIL)=LOWER(?) OR CPF=?",
                Integer.class, email, cpf) > 0;
    }

    public long criar(String nome, String email, String senha, long cpf, Long telefone, String role) {
        long id = ids.next("USUARIO");
        db.update(
                "INSERT INTO TB_USUARIO(ID_USUARIO,NOME,EMAIL,SENHA,CPF,TELEFONE,ROLE) VALUES(?,?,?,?,?,?,?)",
                id, nome, email, senha, cpf, telefone, role);
        return id;
    }

    public List<Usuario> todos() {
        return db.query("SELECT * FROM TB_USUARIO ORDER BY NOME,ID_USUARIO", mapper);
    }

    public long admins() {
        return db.queryForObject("SELECT COUNT(*) FROM TB_USUARIO WHERE ROLE='ADMIN'", Long.class);
    }
}
