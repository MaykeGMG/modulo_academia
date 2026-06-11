package com.example.academia.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Cardio extends Exercicio {
    private int duracao;
    private double caloriasPorMinuto;

    public Cardio(String nome, int duracao, double caloriasPorMinuto) {
        super(nome);
        this.duracao = duracao;
        this.caloriasPorMinuto = caloriasPorMinuto;
    }

    public Cardio(Long id, String nome, boolean ativo, int duracao, double caloriasPorMinuto) {
        super(id, nome, ativo);
        this.duracao = duracao;
        this.caloriasPorMinuto = caloriasPorMinuto;
    }

    @Override
    public double calcularTempoEstimado() { return this.duracao; }

    @Override
    public double calcularCaloriasGastas() { return (this.duracao/60.0) * this.caloriasPorMinuto; }

    @Override
    public int getTipo() { return 1; }
}