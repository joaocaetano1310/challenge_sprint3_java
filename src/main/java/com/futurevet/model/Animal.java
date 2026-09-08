package com.futurevet.model;

public record Animal(long id, long usuarioId, String nome, String especie, String raca, String idade,
        String porte, java.math.BigDecimal peso, String tutor) {
}
