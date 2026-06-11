package com.example.academia.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class RegistroTreino {
    private String dataHora;
    private Long idFicha;
    private String nomeFicha;
    private double tempoTotal;
    private double caloriasTotal;

    public RegistroTreino(Ficha ficha) {
        // Formata a data atual
        this.dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.idFicha = ficha.getId();
        this.nomeFicha = ficha.getNome();
        this.tempoTotal = ficha.getTempoTotal();
        this.caloriasTotal = ficha.getCaloriasTotais();
    }

    public RegistroTreino(String dataHora, Long idFicha, String nomeFicha, double tempoTotal, double caloriasTotal) {
        this.dataHora = dataHora;
        this.idFicha = idFicha;
        this.nomeFicha = nomeFicha;
        this.tempoTotal = tempoTotal;
        this.caloriasTotal = caloriasTotal;
    }
}