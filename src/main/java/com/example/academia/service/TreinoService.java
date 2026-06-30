package com.example.academia.service;

import com.example.academia.model.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreinoService {

    private final List<Exercicio> exercicios = new ArrayList<>();
    private final List<Ficha> fichas = new ArrayList<>();
    private final List<RegistroTreino> historico = new ArrayList<>();
    private final List<ProgressoCorporal> historicoProgresso = new ArrayList<>();

    private Long proximoExercicioId = 1L;
    private Long proximoFichaId = 1L;
    private Long proximoProgressoId = 1L;

    private final String ARQUIVO_EXERCICIOS = "exercicios.txt";
    private final String ARQUIVO_FICHAS = "fichas.txt";
    private final String ARQUIVO_HISTORICO = "historico.txt";
    private final String ARQUIVO_PROGRESSO = "progresso.txt";

    @PostConstruct
    public void inicializar() { //carregar listas a partir dos arquivos quando inicializar
        carregarHistoricoDeArquivo();
        carregarProgressoDeArquivo();
        carregarDadosDoDisco();
    }

    public Exercicio salvarExercicio(Exercicio exercicio) {
        exercicio.setId(proximoExercicioId++);
        exercicios.add(exercicio);
        salvarDadosNoDisco();
        return exercicio;
    }

    public Ficha criarFicha(String nome) {
        Ficha ficha = new Ficha(proximoFichaId++, nome);
        fichas.add(ficha);
        salvarDadosNoDisco();
        return ficha;
    }

    public Ficha adicionarExercicioAFicha(Long fichaId, Long exercicioId) {
        Ficha ficha = fichas.stream().filter(f -> f.getId().equals(fichaId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Ficha não encontrada"));
        Exercicio exercicio = exercicios.stream().filter(e -> e.getId().equals(exercicioId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));

        ficha.adicionarExercicio(exercicio);
        salvarDadosNoDisco();
        return ficha;
    }

    public RegistroTreino concluirTreino(Long fichaId) {
        Ficha ficha = fichas.stream().filter(f -> f.getId().equals(fichaId)).findFirst()
                .orElseThrow(() -> new RuntimeException("Ficha não encontrada"));

        RegistroTreino registro = new RegistroTreino(ficha);
        historico.add(registro);
        salvarHistoricoEmArquivo();
        return registro;
    }

    public ProgressoCorporal salvarProgresso(ProgressoCorporal progresso) {
        progresso.setId(proximoProgressoId++);
        historicoProgresso.add(progresso);
        salvarProgressoEmArquivo();
        return progresso;
    }

    public List<RegistroTreino> obterHistorico() {
        return historico;
    }

    public List<Exercicio> obterExercicios() {
        return exercicios;
    }

    public List<Ficha> obterFichas() {
        return fichas;
    }

    public List<ProgressoCorporal> obterHistoricoProgresso() {
        return historicoProgresso;
    }

    public boolean deletarExercicio(long id) {
        boolean removido = exercicios.removeIf(e -> e.getId() == id);
        if (removido) salvarDadosNoDisco();
        return removido;
    }

    public boolean deletarFicha(long id) {
        boolean removido = fichas.removeIf(f -> f.getId() == id);
        if (removido) salvarDadosNoDisco();
        return removido;
    }

    public boolean deletarProgresso(long id) {
        boolean removido = historicoProgresso.removeIf(p -> p.getId() == id);
        if (removido) salvarProgressoEmArquivo();
        return removido;
    }

    // =================================================================================
    // MÉTODOS DE SALVAMENTO DE ARQUIVO
    // =================================================================================

    private void salvarHistoricoEmArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_HISTORICO))) {
            for (RegistroTreino r : historico) {
                writer.println(r.getDataHora() + ";" + r.getIdFicha() + ";" + r.getNomeFicha() + ";" + r.getTempoTotal() + ";" + r.getCaloriasTotal());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar historico: " + e.getMessage());
        }
    }

    private void salvarProgressoEmArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PROGRESSO))) {
            for (ProgressoCorporal p : historicoProgresso) {
                // Salva: id;dataRegistro;idade;altura;peso;genero;pescoco;cintura;quadril;biceps;coxa;panturrilha;ombro;busto
                writer.println(p.getId() + ";" + p.getDataRegistro() + ";" + p.getIdade() + ";" +
                        p.getAltura() + ";" + p.getPeso() + ";" + p.getGenero() + ";" +
                        p.getPescoco() + ";" + p.getCintura() + ";" + p.getQuadril() + ";" +
                        p.getBiceps() + ";" + p.getCoxa() + ";" + p.getPanturrilha() + ";" +
                        p.getOmbro() + ";" + p.getBusto());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar progresso: " + e.getMessage());
        }
    }

    private void salvarDadosNoDisco() {
        try {
            try (PrintWriter writerEx = new PrintWriter(new FileWriter(ARQUIVO_EXERCICIOS))) {
                for (Exercicio e : exercicios) {
                    if (e instanceof Cardio) {
                        Cardio c = (Cardio) e;
                        writerEx.println(c.getId() + ";" + c.getNome() + ";" + c.getTipo() + ";" + c.getDuracao() + ";" + c.getCaloriasPorMinuto());
                    } else if (e instanceof Forca) {
                        Forca f = (Forca) e;
                        writerEx.println(f.getId() + ";" + f.getNome() + ";" + f.getTipo() + ";" + f.getCarga() + ";" + f.getSeries() + ";" + f.getRepeticoes() + ";" + f.getTempoDescanso());
                    }
                }
            }

            try (PrintWriter writerFi = new PrintWriter(new FileWriter(ARQUIVO_FICHAS))) {
                for (Ficha f : fichas) {
                    StringBuilder idsExercicios = new StringBuilder();
                    for (Exercicio ex : f.getExercicios()) {
                        if (idsExercicios.length() > 0) idsExercicios.append(",");
                        idsExercicios.append(ex.getId());
                    }
                    writerFi.println(f.getId() + ";" + f.getNome() + ";" + (idsExercicios.length() > 0 ? idsExercicios.toString() : "NENHUM"));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no disco: " + e.getMessage());
        }
    }

    // =================================================================================
    // MÉTODOS DE CARREGAMENTO DE ARQUIVO
    // =================================================================================

    private void carregarHistoricoDeArquivo() {
        File arquivo = new File(ARQUIVO_HISTORICO);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] tokens = linha.split(";");
                if (tokens.length >= 5) {
                    historico.add(new RegistroTreino(tokens[0], Long.parseLong(tokens[1]), tokens[2], Double.parseDouble(tokens[3]), Double.parseDouble(tokens[4])));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler historico: " + e.getMessage());
        }
    }

    private void carregarProgressoDeArquivo() {
        File arquivo = new File(ARQUIVO_PROGRESSO);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] tokens = linha.split(";");
                if (tokens.length >= 14) {
                    Long id = Long.parseLong(tokens[0]);
                    LocalDate data = LocalDate.parse(tokens[1]);

                    ProgressoCorporal p = new ProgressoCorporal(id, Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]),
                            Double.parseDouble(tokens[4]), tokens[5], Double.parseDouble(tokens[6]),
                            Double.parseDouble(tokens[7]), Double.parseDouble(tokens[8]),
                            Double.parseDouble(tokens[9]), Double.parseDouble(tokens[10]),
                            Double.parseDouble(tokens[11]), Double.parseDouble(tokens[12]),
                            Double.parseDouble(tokens[13]));
                    // Sobrescreve a data de registro para manter a do arquivo original, pois o construtor coloca LocalDate.now()
                    p.setDataRegistro(data);

                    historicoProgresso.add(p);
                    if (id >= proximoProgressoId) {
                        proximoProgressoId = id + 1;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler progresso: " + e.getMessage());
        }
    }

    private void carregarDadosDoDisco() {
        File arquivoEx = new File(ARQUIVO_EXERCICIOS);
        File arquivoFi = new File(ARQUIVO_FICHAS);

        try {
            if (arquivoEx.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivoEx))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        if (linha.trim().isEmpty()) continue;
                        String[] tokens = linha.split(";");
                        Long id = Long.parseLong(tokens[0]);
                        Exercicio ex;
                        if (Integer.parseInt(tokens[2]) == 1) {
                            ex = new Cardio(tokens[1], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]));
                        } else {
                            ex = new Forca(tokens[1], Double.parseDouble(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]));
                        }
                        ex.setId(id);
                        exercicios.add(ex);
                        if (id >= proximoExercicioId) proximoExercicioId = id + 1;
                    }
                }
            }

            if (arquivoFi.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivoFi))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        if (linha.trim().isEmpty()) continue;
                        String[] tokens = linha.split(";");
                        Long id = Long.parseLong(tokens[0]);
                        Ficha ficha = new Ficha(id, tokens[1]);

                        if (!tokens[2].equals("NENHUM")) {
                            for (String idStr : tokens[2].split(",")) {
                                Long idEx = Long.parseLong(idStr);
                                exercicios.stream().filter(e -> e.getId().equals(idEx)).findFirst().ifPresent(ficha::adicionarExercicio);
                            }
                        }
                        fichas.add(ficha);
                        if (id >= proximoFichaId) proximoFichaId = id + 1;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do disco: " + e.getMessage());
        }
    }
}