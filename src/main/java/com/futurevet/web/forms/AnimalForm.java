package com.futurevet.web.forms;

import jakarta.validation.constraints.*;

public class AnimalForm {
    @NotBlank
    @Size(max = 30)
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String value) {
        this.nome = value;
    }

    @NotBlank
    @Pattern(regexp = "CAO|GATO|COELHO|OUTRO")
    private String especie;

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String value) {
        this.especie = value;
    }

    @Size(max = 35)
    private String raca;

    public String getRaca() {
        return raca;
    }

    public void setRaca(String value) {
        this.raca = value;
    }

    @Size(max = 30)
    private String idade;

    public String getIdade() {
        return idade;
    }

    public void setIdade(String value) {
        this.idade = value;
    }

    @Pattern(regexp = "PEQUENO|MEDIO|GRANDE")
    private String porte;

    public String getPorte() {
        return porte;
    }

    public void setPorte(String value) {
        this.porte = value;
    }

    @DecimalMin("0.01")
    @DecimalMax("999.99")
    @Digits(integer = 3, fraction = 2)
    private java.math.BigDecimal peso;

    public java.math.BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(java.math.BigDecimal value) {
        this.peso = value;
    }
}
