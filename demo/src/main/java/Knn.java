import java.util.ArrayList;
import java.util.List;

public class Knn {
    private int k;
    private List<Jogador> dadosTreinamento;

    public Knn(int k, List<Jogador> dadosTreinamento) {
        this.k = k;
        this.dadosTreinamento = dadosTreinamento;
    }

    //talvez private? nao sei
    public double calcularDistanciaEuclidiana(double[] p, double[] q) throws IllegalArgumentException {

        if (p.length != q.length) {
            throw new IllegalArgumentException("Os vetores devem ter o mesmo tamanho.");
        }

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

    public String prever(double[] caracteristicas) {

        List<Double> distancias = new ArrayList<>();

        for(Jogador jogador : dadosTreinamento) {
            distancias.add(calcularDistanciaEuclidiana(caracteristicas, jogador.getCaracteristicas()));
            //testezinho
            System.out.println("distancia: " + distancias.get(distancias.size() - 1) + " para o jogador: " + jogador.getApelido());
        }

        return "classe prevista: ?";
    }
}
