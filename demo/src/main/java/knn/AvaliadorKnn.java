package knn;

import java.util.List;

import modelo.Jogador;
import modelo.Perfil;

public class AvaliadorKnn {
    
    public static double acuracia(Knn knn, List<Jogador> teste) {
        if (knn == null || teste == null) throw new IllegalArgumentException("Knn e conjunto de teste obrigatorios");

        int acertos = 0;

        for(Jogador jogador : teste) {
            if(knn.prever(jogador.getCaracteristicas()).equals(jogador.getPerfil())) {
                acertos++;
            }
        }

        double acuracia = (double) acertos / teste.size();

        return acuracia;
    }
    public static int[][] matrizConfusao(Knn knn, List<Jogador> teste) {
        int n = Perfil.values().length;
        int[][] m = new int[n][n];
        for (Jogador j : teste) {
            Perfil previsto = knn.prever(j.getCaracteristicas());
            m[j.getPerfil().ordinal()][previsto.ordinal()]++;
        }
        return m;
    }

    public static void imprimirMatriz(int[][] m) {
    Perfil[] ps = Perfil.values();
    System.out.printf("%-12s", "real\\prev");
    for (Perfil p : ps) System.out.printf("%-10s", p);
    System.out.println();
    for (int i = 0; i < m.length; i++) {
        System.out.printf("%-12s", ps[i]);
        for (int j = 0; j < m.length; j++) System.out.printf("%-10d", m[i][j]);
        System.out.println();
    }
}
}
