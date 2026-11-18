package knn;

import modelo.Jogador;
import modelo.Perfil;
import java.util.*;

public class ExperimentoKnn {
    private final List<Jogador> treino;
    private final List<Jogador> teste;

  public ExperimentoKnn(List<Jogador> jogadores) {
        List<Jogador> embaralhados = new ArrayList<>(jogadores);
    Collections.shuffle(embaralhados, new Random(42)); // semente fixa
        int corte = (int) (embaralhados.size() * 0.8);
        treino = new ArrayList<>(embaralhados.subList(0, corte));
        teste = new ArrayList<>(embaralhados.subList(corte, embaralhados.size()));
        normalizador = new NormalizadorMinMax(treino);
  }

  public void executar(int kEscolhido, double[] jogadorNovo){
    System.out.println("\nTreino: " + treino.size() + " | Teste: " + teste.size());

        System.out.println("\nAcurácia por K (euclidiana, normalizado):");
        for (int k : new int[]{1, 3, 5, 7, 9, 11}) {
            Knn knn = new Knn(k, treino, Metrica.EUCLIDIANA, normalizador);
            System.out.printf("K=%d -> %.2f%%%n", k, Avaliador.acuracia(knn, teste) * 100);  }
}

