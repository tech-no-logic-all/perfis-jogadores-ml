//classe auxiliar pro KNN, a interface eh pra implementar um algoritmo de ordenacao com
//a biblioteca

public class Vizinho implements Comparable<Vizinho> {
    Jogador jogador;
    double distancia;

    public Vizinho(Jogador jogador, double distancia) {
        this.jogador = jogador;
        this.distancia = distancia;
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
