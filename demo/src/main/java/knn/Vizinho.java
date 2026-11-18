package knn;

import modelo.Jogador;

/** Classe auxiliar que associa um jogador à sua distância da entrada analisada. */
public class Vizinho implements Comparable<Vizinho> {
    private final Jogador jogador;
    private final double distancia;

    public Vizinho(Jogador jogador, double distancia) {
        this.jogador = jogador;
        this. = distancia;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public double getDistancia() {
        return distancia;
    }

    @Override
    public int compareTo(Vizinho outro) {
        return Double.compare(this.distancia, outro.distancia);
    }
}
