package com.example.academia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Perfil {
    private int idade;
    private double altura; // em cm
    private String genero; // "M" ou "F"
}