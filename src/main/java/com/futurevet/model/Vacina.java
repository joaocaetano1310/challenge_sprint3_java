package com.futurevet.model;

public record Vacina(long id, long animalId, String animal, long usuarioId, String nome,
        java.time.LocalDate data, java.time.LocalDate proximaDose, String local, Long origemId,
        boolean substituida) {
}
