import java.util.List;

public class Knn {
    private int k;
    private List<Jogador> dadosTreinamento;

    public Knn(int k, List<Jogador> dadosTreinamento) {
        this.k = k;
        this.dadosTreinamento = dadosTreinamento;
    }

    //talvez private? nao sei
    public double calcularDistanciaEuclidiana(double[] p, double[] q) {
        double[] temporario = new double[p.length];

        for (int i = 0; i < p.length; i++) {
            temporario[i] = Math.pow(p[i] - q[i], 2);
        }

        double soma = 0;

        for (double valor : temporario) {
            soma += valor;
        }
        
        return Math.sqrt(soma);
    }
}
