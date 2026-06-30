package com.example.academia.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ProgressoCorporal {
    private Long id;
    private LocalDate dataRegistro;

    // Dados Básicos
    private int idade;
    private double altura; // em cm (ex: 175)
    private double peso;   // em kg
    private String genero; // "M" ou "F"

    // Medidas
    private double busto;
    private double ombro;
    private double cintura;
    private double quadril;
    private double biceps;
    private double coxa;
    private double panturrilha;
    private double pescoco;

    // Atributos Calculados automaticamente pelo Java
    private double percentualGordura;
    private double massaMagra;
    private double massaGorda;

    public ProgressoCorporal(Long id, int idade, double altura, double peso, String genero, double pescoco,
                             double cintura, double quadril, double biceps, double coxa, double panturrilha, double ombro, double busto) {
        this.id = id;
        this.dataRegistro = LocalDate.now();
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
        this.genero = genero.toUpperCase();
        this.cintura = cintura;
        this.quadril = quadril;
        this.biceps = biceps;
        this.pescoco = pescoco;
        this.coxa = coxa;
        this.panturrilha = panturrilha;
        this.ombro = ombro;
        this.busto = busto;
        calcularComposicaoCorporal();
    }

    public void calcularComposicaoCorporal() {
        if (this.altura <= 0 || (this.cintura - this.pescoco) <= 0) {
            this.percentualGordura = 0;
            this.massaGorda = 0;
            this.massaMagra = this.peso;
            return;
        }

        if ("M".equals(this.genero)) {
            // Fórmula U.S. Navy para Homens
            this.percentualGordura = 86.010 * Math.log10(this.cintura - this.pescoco)
                    - 70.041 * Math.log10(this.altura) + 36.76;
        } else if ("F".equals(this.genero)) {
            // Fórmula U.S. Navy para Mulheres
            double somaMedidas = this.cintura + this.quadril - this.pescoco;
            if (somaMedidas <= 0) somaMedidas = 1;
            this.percentualGordura = 163.205 * Math.log10(somaMedidas)
                    - 97.684 * Math.log10(this.altura) - 78.387;
        }

        // Evitar valores bizarros/negativos em medições erradas
        if (this.percentualGordura < 2) this.percentualGordura = 2;
        if (this.percentualGordura > 60) this.percentualGordura = 60;

        this.massaGorda = this.peso * (this.percentualGordura / 100.0);
        this.massaMagra = this.peso - this.massaGorda;
    }
}