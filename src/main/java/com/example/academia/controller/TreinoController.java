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

    // --- ENDPOINTS DE PERFIL (NOVO) ---
    @GetMapping("/perfil")
    public Perfil obterPerfil() {
        return treinoService.obterPerfil();
    }

    @PostMapping("/perfil")
    public Perfil atualizarPerfil(@RequestParam int idade, @RequestParam double altura, @RequestParam String genero) {
        return treinoService.atualizarPerfil(new Perfil(idade, altura, genero.toUpperCase()));
    }

    // --- ENDPOINTS DE EXERCÍCIOS ---
    @PostMapping("/exercicios/cardio")
    public Exercicio criarCardio(@RequestParam String nome, @RequestParam int duracao, @RequestParam double calorias) {
        return treinoService.salvarExercicio(new Cardio(nome, duracao, calorias));
    }

    @PostMapping("/exercicios/forca")
    public Exercicio criarForca(@RequestParam String nome, @RequestParam double carga, @RequestParam int series, @RequestParam int reps, @RequestParam int descanso) {
        return treinoService.salvarExercicio(new Forca(nome, carga, series, reps, descanso));
    }

    @GetMapping("/exercicios")
    public List<Exercicio> listarExercicios() {
        return treinoService.obterExercicios();
    }

    @DeleteMapping("/exercicios/{id}")
    public String deletarExercicio(@PathVariable long id) {
        boolean deletado = treinoService.deletarExercicio(id);
        if (deletado) {
            return "Exercicio deletado com sucesso!";
        } else {
            return "Erro: Exercicio com o ID " + id + " nao encontrado.";
        }
    }

    // --- ENDPOINTS DE FICHAS ---
    @PostMapping("/fichas")
    public Ficha criarFicha(@RequestParam String nome) {
        return treinoService.criarFicha(nome);
    }

    @PostMapping("/fichas/{fichaId}/add/{exercicioId}")
    public Ficha vincularExercicio(@PathVariable Long fichaId, @PathVariable Long exercicioId) {
        return treinoService.adicionarExercicioAFicha(fichaId, exercicioId);
    }

    @PostMapping("/fichas/{fichaId}/concluir")
    public RegistroTreino concluir(@PathVariable Long fichaId) {
        return treinoService.concluirTreino(fichaId);
    }

    @GetMapping("/fichas")
    public List<Ficha> listarFichas() {
        return treinoService.obterFichas();
    }

    @DeleteMapping("/fichas/{id}")
    public String deletarFicha(@PathVariable long id) {
        boolean deletado = treinoService.deletarFicha(id);
        if (deletado) {
            return "Exercicio deletado com sucesso!";
        } else {
            return "Erro: Ficha com o ID " + id + " nao encontrado.";
        }
    }

    // --- ENDPOINTS DE HISTÓRICO DE TREINOS ---
    @GetMapping("/historico")
    public List<RegistroTreino> listarHistorico() {
        return treinoService.obterHistorico();
    }

    // --- ENDPOINTS DE EVOLUÇÃO CORPORAL (MODIFICADO) ---
    @PostMapping("/progresso")
    public ProgressoCorporal registrarProgresso(
            @RequestParam double peso, @RequestParam double pescoco, @RequestParam double cintura,
            @RequestParam double quadril, @RequestParam double biceps, @RequestParam double coxa,
            @RequestParam double panturrilha, @RequestParam double ombro, @RequestParam double busto) {

        // Busca o perfil atualizado para injetar no histórico imutável do snapshot de progresso
        Perfil atual = treinoService.obterPerfil();

        ProgressoCorporal novo = new ProgressoCorporal(
                null,
                atual.getIdade(),
                atual.getAltura(),
                peso,
                atual.getGenero(),
                pescoco,
                cintura,
                quadril,
                biceps,
                coxa,
                panturrilha,
                ombro,
                busto
        );

        return treinoService.salvarProgresso(novo);
    }

    @GetMapping("/progresso")
    public List<ProgressoCorporal> listarEvolucao() {
        return treinoService.obterHistoricoProgresso();
    }

    @DeleteMapping("/progresso/{id}")
    public String deletarProgresso(@PathVariable long id) {
        boolean deletado = treinoService.deletarProgresso(id);
        if (deletado) {
            return "Registro de progresso deletado com sucesso!";
        } else {
            return "Erro: Registro com o ID " + id + " não encontrado.";
        }
    }
}