package com.example.academia.controller;

import com.example.academia.model.*;
import com.example.academia.service.TreinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TreinoController {

    @Autowired
    private TreinoService treinoService;

    // Endpoint para criar um Exercicio d eCardio
    @PostMapping("/exercicios/cardio")
    public Exercicio criarCardio(@RequestParam String nome, @RequestParam int duracao, @RequestParam double calorias) {
        return treinoService.salvarExercicio(new Cardio(nome, duracao, calorias));
    }

    // Endpoint para criar um Exercício de Força
    @PostMapping("/exercicios/forca")
    public Exercicio criarForca(@RequestParam String nome, @RequestParam double carga, @RequestParam int series, @RequestParam int reps, @RequestParam int descanso) {
        return treinoService.salvarExercicio(new Forca(nome, carga, series, reps, descanso));
    }

    // Criar Ficha
    @PostMapping("/fichas")
    public Ficha criarFicha(@RequestParam String nome) {
        return treinoService.criarFicha(nome);
    }

    // Adicionar Exercício na Ficha
    @PostMapping("/fichas/{fichaId}/add/{exercicioId}")
    public Ficha vincularExercicio(@PathVariable Long fichaId, @PathVariable Long exercicioId) {
        return treinoService.adicionarExercicioAFicha(fichaId, exercicioId);
    }

    // Concluir Treino (Salva no histórico)
    @PostMapping("/fichas/{fichaId}/concluir")
    public RegistroTreino concluir(@PathVariable Long fichaId) {
        return treinoService.concluirTreino(fichaId);
    }

    //ver lista de exercicios
    @GetMapping("/exercicios")
    public List<Exercicio> listarExercicios() {
        return treinoService.obterExercicios();
    }

    //ver lista de fichas
    @GetMapping("/fichas")
    public List<Ficha> listarFichas() {
        return treinoService.obterFichas();
    }

    // Ver histórico
    @GetMapping("/historico")
    public List<RegistroTreino> listarHistorico() {
        return treinoService.obterHistorico();
    }

    // Deletar um exercício
    @DeleteMapping("/exercicios/{id}")
    public String deletarExercicio(@PathVariable long id) {
        boolean deletado = treinoService.deletarExercicio(id);
        if (deletado) {
            return "Exercicio deletado com sucesso!";
        } else {
            return "Erro: Exercicio com o ID " + id + " nao encontrado.";
        }
    }

    //Deletar uma ficha
    @DeleteMapping("/fichas/{id}")
    public String deletarFicha(@PathVariable long id) {
        boolean deletado = treinoService.deletarFicha(id);
        if (deletado) {
            return "Exercicio deletado com sucesso!";
        } else {
            return "Erro: Ficha com o ID " + id + " nao encontrado.";
        }
    }
}