package knn;

import modelo.Jogador;
import java.util.List;

public class NormalizadorMinMax {
    private final double[] min;
    private final double[] max;

    /** Ajusta min/max usando SOMENTE os jogadores passados (o treino). */
    public NormalizadorMinMax(List<Jogador> treino) {
        int n = treino.get(0).getCaracteristicas().length;
        min = new double[n];
        max = new double[n];
        for (int j = 0; j < n; j++) {
            min[j] = Double.MAX_VALUE;
            max[j] = -Double.MAX_VALUE;
        }
        for (Jogador jogador : treino) {
            double[] v = jogador.getCaracteristicas();
            for (int j = 0; j < n; j++) {
                min[j] = Math.min(min[j], v[j]);
                max[j] = Math.max(max[j], v[j]);
            }
        }
    }

    public double[] transformar(double[] v) {
        double[] r = new double[v.length];
        for (int j = 0; j < v.length; j++) {
            double faixa = max[j] - min[j];
            r[j] = faixa == 0 ? 0 : (v[j] - min[j]) / faixa;
        }
        return r;
    }
}
