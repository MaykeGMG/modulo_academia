package com.example.academia.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class Exercicio {
    private Long id;
    private String nome;
    private boolean ativo = true;

    // Construtor para novos cadastros
    public Exercicio(String nome) {
        this.nome = nome;
    }

    // Construtor para leitura de arquivo
    public Exercicio(Long id, String nome, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.ativo = ativo;
    }

    public abstract double calcularTempoEstimado();
    public abstract double calcularCaloriasGastas();
    public abstract int getTipo(); // 1 = Cardio, 2 = Força
}