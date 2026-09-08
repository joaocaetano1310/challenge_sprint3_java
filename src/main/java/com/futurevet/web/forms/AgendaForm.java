package com.futurevet.web.forms;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

public class AgendaForm {
    @NotNull
    private Long animalId;

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long value) {
        this.animalId = value;
    }

    @NotBlank
    @Size(max = 30)
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String value) {
        this.tipo = value;
    }

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private java.time.LocalDate data;

    public java.time.LocalDate getData() {
        return data;
    }

    public void setData(java.time.LocalDate value) {
        this.data = value;
    }

    @NotNull
    @DateTimeFormat(pattern = "HH:mm")
    private java.time.LocalTime hora;

    public java.time.LocalTime getHora() {
        return hora;
    }

    public void setHora(java.time.LocalTime value) {
        this.hora = value;
    }

    @NotBlank
    @Size(max = 100)
    private String local;

    public String getLocal() {
        return local;
    }

    public void setLocal(String value) {
        this.local = value;
    }

    @Size(max = 255)
    private String observacao;

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String value) {
        this.observacao = value;
    }
}
