package knn;

import java.util.List;

import modelo.Jogador;

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
}
