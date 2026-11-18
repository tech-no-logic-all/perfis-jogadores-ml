package knn;

import modelo.Jogador;
import modelo.Perfil;
import java.util.*;

public class ExperimentoKnn {
    private final List<Jogador> treino;
    private final List<Jogador> teste;
    private final NormalizadorMinMax normalizador;

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
            System.out.printf("K=%d -> %.2f%%%n", k, AvaliadorKnn.acuracia(knn, teste) * 100);  
        }

        // com/sem normalização x Euclidiana/Manhattan
System.out.println("\nComparação com K=" + kEscolhido + ":");
for (Metrica m : Metrica.values()) {
    Knn com = new Knn(kEscolhido, treino, m, normalizador);
    Knn sem = new Knn(kEscolhido, treino, m, null); // null = sem normalização
    System.out.printf("%-11s com norm: %.2f%% | sem norm: %.2f%%%n", m,
            AvaliadorKnn.acuracia(com, teste) * 100,
            AvaliadorKnn.acuracia(sem, teste) * 100);
}

//matriz de confusão e jogador novo
Knn escolhido = new Knn(kEscolhido, treino, Metrica.EUCLIDIANA, normalizador);

System.out.println("\nMatriz de confusão (K=" + kEscolhido + ", euclidiana, normalizado):");
AvaliadorKnn.imprimirMatriz(AvaliadorKnn.matrizConfusao(escolhido, teste));

Perfil previsto = escolhido.prever(jogadorNovo);
System.out.println("\nJogador novo " + Arrays.toString(jogadorNovo) + " -> " + previsto);
    }
}
