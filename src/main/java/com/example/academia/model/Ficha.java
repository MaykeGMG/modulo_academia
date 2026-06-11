package com.example.academia.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Ficha {
    private Long id;
    private String nome;
    private List<Exercicio> exercicios = new ArrayList<>();

    public Ficha(String nome) {
        this.nome = nome;
    }

    public Ficha(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public void adicionarExercicio(Exercicio exercicio) {
        this.exercicios.add(exercicio);
    }

    public double getTempoTotal() {
        return exercicios.stream().mapToDouble(Exercicio::calcularTempoEstimado).sum();
    }

    public double getCaloriasTotais() {
        return exercicios.stream().mapToDouble(Exercicio::calcularCaloriasGastas).sum();
    }
}