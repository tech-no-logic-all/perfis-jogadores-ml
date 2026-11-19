package knn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modelo.Jogador;
import modelo.Perfil;

public class ExperimentoKnn {
    private final List<Jogador> treino;
    private final List<Jogador> teste;
    private final NormalizadorMinMax normalizador;

  public ExperimentoKnn(List<Jogador> jogadores) {
        List<Jogador> embaralhados = new ArrayList<>(jogadores);
        Collections.shuffle(embaralhados, new Random(40)); // semente fixa
        int corte = (int) (embaralhados.size() * 0.8);
        treino = new ArrayList<>(embaralhados.subList(0, corte));
        teste = new ArrayList<>(embaralhados.subList(corte, embaralhados.size()));
        normalizador = new NormalizadorMinMax(treino);
  }

    public void executar(int kEscolhido, double[] jogadorNovo) {
        MedidaDeDistancia euclidiana = new DistanciaEuclidiana();
        MedidaDeDistancia manhattan = new DistanciaDeManhattan();

        System.out.println("\nTreino: " + treino.size() + " | Teste: " + teste.size());

        // Item 3
        System.out.println("\nAcurácia por K (euclidiana, normalizado):");
        for (int k : new int[]{1, 3, 5, 7, 9, 11}) {
            Knn knn = new Knn(k, treino, euclidiana, normalizador);
            System.out.printf("K=%d -> %.2f%%%n", k, AvaliadorKnn.acuracia(knn, teste) * 100);
        }

        // Item 6: curva de aprendizagem para o K escolhido
        imprimirCurvaAprendizagem(kEscolhido);

        // Item 4
        System.out.println("\nComparação com K=" + kEscolhido + ":");
        comparar("Euclidiana", euclidiana, kEscolhido);
        comparar("Manhattan", manhattan, kEscolhido);

        // Item 5
        Knn escolhido = new Knn(kEscolhido, treino, euclidiana, normalizador);

        System.out.println("\nMatriz de confusão (K=" + kEscolhido + ", euclidiana, normalizado):");
        AvaliadorKnn.imprimirMatriz(AvaliadorKnn.matrizConfusao(escolhido, teste));

        Perfil previsto = escolhido.prever(jogadorNovo);
        System.out.println("\nJogador novo " + Arrays.toString(jogadorNovo) + " -> " + previsto);
    }

    private void comparar(String nome, MedidaDeDistancia medida, int k) {
        Knn com = new Knn(k, treino, medida, normalizador);
        Knn sem = new Knn(k, treino, medida, null); // null = sem normalização
        System.out.printf("%-11s com norm: %.2f%% | sem norm: %.2f%%%n", nome,
                AvaliadorKnn.acuracia(com, teste) * 100,
                AvaliadorKnn.acuracia(sem, teste) * 100);
    }

    private void imprimirCurvaAprendizagem(int k) {
        System.out.println("\nCurva de aprendizagem (K=" + k + ", euclidiana, normalizado):");
        System.out.printf("%-18s%s%n", "Jogadores treino", "Acurácia no teste");

        for (int quantidade = 15; quantidade <= treino.size(); quantidade += 15) {
            List<Jogador> amostra = new ArrayList<>(treino.subList(0, quantidade));
            NormalizadorMinMax normalizadorAmostra = new NormalizadorMinMax(amostra);
            Knn knn = new Knn(k, amostra, new DistanciaEuclidiana(), normalizadorAmostra);
            System.out.printf("%-18d%.2f%%%n", quantidade, AvaliadorKnn.acuracia(knn, teste) * 100);
        }
    }
}
