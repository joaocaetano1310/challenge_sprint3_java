package com.futurevet.model;

public record Consulta(long id, long animalId, String animal, long usuarioId, String tutor, String tipo,
        java.time.LocalDate data, String hora, String local, String status, String observacao) {
    public String statusDescricao() {
        return switch (status) {
        case "AGENDADA" -> "Aguardando confirmação";
        case "CONFIRMADA" -> "Confirmada";
        case "REALIZADA" -> "Realizada";
        case "CANCELADA" -> "Cancelada";
        default -> "Situação não informada";
        };
    }

    public boolean ativa() {
        return java.util.Set.of("AGENDADA", "CONFIRMADA", "INDEFINIDA").contains(status);
    }
}
