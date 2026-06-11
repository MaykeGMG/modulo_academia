package com.example.academia.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Forca extends Exercicio {
    private double carga;
    private int series;
    private int repeticoes;
    private int tempoDescanso;

    public Forca(String nome, double carga, int series, int repeticoes, int tempoDescanso) {
        super(nome);
        this.carga = carga;
        this.series = series;
        this.repeticoes = repeticoes;
        this.tempoDescanso = tempoDescanso;
    }

    public Forca(Long id, String nome, boolean ativo, double carga, int series, int repeticoes, int tempoDescanso) {
        super(id, nome, ativo);
        this.carga = carga;
        this.series = series;
        this.repeticoes = repeticoes;
        this.tempoDescanso = tempoDescanso;
    }

    @Override
    public double calcularTempoEstimado() {
        return ((double)(series * repeticoes * 3) + (series * tempoDescanso)) / 60.0;
    }

    @Override
    public double calcularCaloriasGastas() {
        return series * repeticoes * carga * 0.15;
    }

    @Override
    public int getTipo() { return 2; }
}