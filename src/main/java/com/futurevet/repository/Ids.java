package com.futurevet.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Ids {
    private final JdbcTemplate db;

    public Ids(JdbcTemplate db) {
        this.db = db;
    }

    public long next(String tabela) {
        if (!java.util.Set.of("USUARIO", "ANIMAL", "CONSULTA", "VACINA").contains(tabela))
            throw new IllegalArgumentException();
        return db.queryForObject("SELECT FV_SEQ_" + tabela + ".NEXTVAL FROM DUAL", Long.class);
    }
}
