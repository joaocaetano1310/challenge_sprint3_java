package com.futurevet.web.forms;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

public class VacinaForm {
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
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String value) {
        this.nome = value;
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

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private java.time.LocalDate proximaDose;

    public java.time.LocalDate getProximaDose() {
        return proximaDose;
    }

    public void setProximaDose(java.time.LocalDate value) {
        this.proximaDose = value;
    }

    @Size(max = 100)
    private String local;

    public String getLocal() {
        return local;
    }

    public void setLocal(String value) {
        this.local = value;
    }
}
