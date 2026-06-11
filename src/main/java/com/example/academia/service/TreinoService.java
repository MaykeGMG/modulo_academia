package com.example.academia.service;

import com.example.academia.model.*;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TreinoService {

    private final List<Exercicio> exercicios = new ArrayList<>();
    private final List<Ficha> fichas = new ArrayList<>();
    private final List<RegistroTreino> historico = new ArrayList<>();

    private Long proximoExercicioId = 1L;
    private Long proximoFichaId = 1L;

    private final String ARQUIVO_EXERCICIOS = "exercicios.txt";
    private final String ARQUIVO_FICHAS = "fichas.txt";
    private final String ARQUIVO_HISTORICO = "historico.txt";

    @PostConstruct
    public void inicializar() { //carregar listas a partir dos arquivos quando inicializar
        carregarHistoricoDeArquivo();
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
        Ficha ficha = fichas.stream().filter(f -> f.getId().equals(fichaId)).findFirst()    //buscar ficha por id
                .orElseThrow(() -> new RuntimeException("Ficha não encontrada"));
        Exercicio exercicio = exercicios.stream().filter(e -> e.getId().equals(exercicioId)).findFirst()    //buscar exercicio por id
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

        salvarHistoricoEmArquivo(); // Salva toda vez que conclui um treino
        return registro;
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

    public boolean deletarExercicio(long id) {
        boolean removido = exercicios.removeIf(e -> e.getId() == id);
        if (removido) {
            salvarDadosNoDisco();   //Atualiza o arquivo txt se deletou
        }
        return removido;
    }

    public boolean deletarFicha(long id) {
        boolean removido = fichas.removeIf(f -> f.getId() == id);
        if (removido) {
            salvarDadosNoDisco(); //Atualiza o arquivo txt se deletou
        }
        return removido;
    }

    // Salavamento dos dados nos arquivos

    private void salvarHistoricoEmArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_HISTORICO))) {
            for (RegistroTreino r : historico) {
                writer.println(r.getDataHora() + ";"
                        + r.getIdFicha() + ";"
                        + r.getNomeFicha() + ";"
                        + r.getTempoTotal() + ";"
                        + r.getCaloriasTotal());
            }
            System.out.println("Histórico salvo em arquivo com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao tentar salvar no arquivo: " + e.getMessage());
        }
    }

    private void carregarHistoricoDeArquivo() {
        File arquivo = new File(ARQUIVO_HISTORICO);
        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = reader.readLine()) != null) {

                if (linha.trim().isEmpty()) continue;
                String[] tokens = linha.split(";");

                if (tokens.length >= 5) {

                    String dataHora = tokens[0];
                    Long idFicha = Long.parseLong(tokens[1]);
                    String nomeFicha = tokens[2];
                    double tempoTotal = Double.parseDouble(tokens[3]);
                    double caloriasTotal = Double.parseDouble(tokens[4]);

                    historico.add(new RegistroTreino(dataHora, idFicha, nomeFicha, tempoTotal, caloriasTotal));
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao tentar ler o arquivo: " + e.getMessage());
        }
    }

    private void salvarDadosNoDisco() {
        try {
            //Salva a lista de Exercícios Completa
            try (PrintWriter writerEx = new PrintWriter(new FileWriter(ARQUIVO_EXERCICIOS))) {

                for (Exercicio e : exercicios) {

                    if (e instanceof Cardio) {

                        Cardio c = (Cardio) e;
                        // Salva: ID;Nome;Tipo;Duração;CaloriasPorMinuto
                        writerEx.println(c.getId() + ";"
                                + c.getNome() + ";"
                                + c.getTipo() + ";"
                                + c.getDuracao() + ";"
                                + c.getCaloriasPorMinuto());
                    } else if (e instanceof Forca) {

                        Forca f = (Forca) e;
                        // Salva: ID;Nome;Tipo;Carga;Series;Repeticoes;Descanso
                        writerEx.println(f.getId() + ";"
                                + f.getNome() + ";"
                                + f.getTipo() + ";"
                                + f.getCarga() + ";"
                                + f.getSeries() + ";"
                                + f.getRepeticoes() + ";"
                                + f.getTempoDescanso());
                    }
                }
            }

            //Salva a lista de fichas completa
            try (PrintWriter writerFi = new PrintWriter(new FileWriter(ARQUIVO_FICHAS))) {

                for (Ficha f : fichas) {

                    // Junta todos os IDs dos exercícios da ficha separados por vírgula
                    StringBuilder idsExercicios = new StringBuilder();
                    for (Exercicio ex : f.getExercicios()) {

                        if (idsExercicios.length() > 0) idsExercicios.append(",");
                        idsExercicios.append(ex.getId());
                    }
                    // Salva ID;Nome;IDsDosExercicios
                    writerFi.println(f.getId() + ";"
                            + f.getNome() + ";"
                            + (idsExercicios.length() > 0 ? idsExercicios.toString() : "NENHUM"));
                }
            }
            System.out.println("Exercícios e Fichas salvos com sucesso total!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados no disco: " + e.getMessage());
        }
    }

    private void carregarDadosDoDisco() {
        File arquivoEx = new File(ARQUIVO_EXERCICIOS);
        File arquivoFi = new File(ARQUIVO_FICHAS);

        try {
            //Carrega os exercicios
            if (arquivoEx.exists()) {

                try (BufferedReader reader = new BufferedReader(new FileReader(arquivoEx))) {

                    String linha;
                    while ((linha = reader.readLine()) != null) {

                        if (linha.trim().isEmpty()) continue;
                        String[] tokens = linha.split(";");

                        Long id = Long.parseLong(tokens[0]);
                        String nome = tokens[1];
                        int tipo = Integer.parseInt(tokens[2]);

                        Exercicio ex;
                        if (tipo == 1) {
                            int duracao = Integer.parseInt(tokens[3]);
                            double calPorMin = Double.parseDouble(tokens[4]);
                            ex = new Cardio(nome, duracao, calPorMin);
                        } else {
                            double carga = Double.parseDouble(tokens[3]);
                            int series = Integer.parseInt(tokens[4]);
                            int repeticoes = Integer.parseInt(tokens[5]);
                            int descanso = Integer.parseInt(tokens[6]);
                            ex = new Forca(nome, carga, series, repeticoes, descanso);
                        }
                        ex.setId(id);
                        exercicios.add(ex);

                        if (id >= proximoExercicioId) {
                            proximoExercicioId = id + 1;
                        }
                    }
                }
            }

            //Carrega as fichas
            if (arquivoFi.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(arquivoFi))) {
                    String linha;
                    while ((linha = reader.readLine()) != null) {
                        if (linha.trim().isEmpty()) continue;
                        String[] tokens = linha.split(";");

                        Long id = Long.parseLong(tokens[0]);
                        String nome = tokens[1];
                        String exerciciosVinculados = tokens[2];

                        Ficha ficha = new Ficha(id, nome);

                        // Se a ficha tinha exercícios, busca eles na memória pelo ID e reconecta
                        if (!exerciciosVinculados.equals("NENHUM")) {
                            String[] ids = exerciciosVinculados.split(",");
                            for (String idStr : ids) {
                                Long idEx = Long.parseLong(idStr);
                                exercicios.stream()
                                        .filter(e -> e.getId().equals(idEx))
                                        .findFirst()
                                        .ifPresent(ficha::adicionarExercicio);
                            }
                        }

                        fichas.add(ficha);

                        if (id >= proximoFichaId) {
                            proximoFichaId = id + 1;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar dados do disco: " + e.getMessage());
        }
    }
}