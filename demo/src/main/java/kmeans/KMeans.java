package kmeans;

import java.util.Arrays;
import java.util.Random;

/** K-Means com inicialização por linhas sorteadas, sem usar os perfis. */
public final class KMeans {
    public ResultadoKMeans executar(double[][] dados, int k, int maxIteracoes, long semente) {
        int colunas = NormalizadorMinMax.validar(dados);
        if (k < 1 || k > dados.length || maxIteracoes < 1)
            throw new IllegalArgumentException("Exija 1 <= K <= quantidade de pontos e limite positivo.");
        Random sorteio = new Random(semente);
        int[] indices = new int[dados.length];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        double[][] centros = new double[k][];
        for (int g = 0; g < k; g++) {
            int escolhido = g + sorteio.nextInt(indices.length - g);
            int troca = indices[g]; indices[g] = indices[escolhido]; indices[escolhido] = troca;
            centros[g] = dados[indices[g]].clone();
        }
        int[] grupos = new int[dados.length];
        Arrays.fill(grupos, -1);
        int iteracoes = 0;
        while (iteracoes < maxIteracoes) {
            int[] anteriores = grupos.clone();
            int[] tamanhos = new int[k];
            for (int i = 0; i < dados.length; i++) {
                // Em empate, preserve o grupo anterior; na primeira passagem, vence o menor índice.
                int melhor = grupos[i] < 0 ? 0 : grupos[i];
                double menor = distanciaQuadrada(dados[i], centros[melhor]);
                for (int g = 0; g < k; g++) {
                    double distancia = distanciaQuadrada(dados[i], centros[g]);
                    if (distancia < menor) { menor = distancia; melhor = g; }
                }
                grupos[i] = melhor;
                tamanhos[melhor]++;
            }
            // Transfira o ponto mais distante de um grupo com pelo menos dois membros.
            for (int vazio = 0; vazio < k; vazio++) if (tamanhos[vazio] == 0) {
                int escolhido = -1;
                double maior = -1;
                for (int i = 0; i < dados.length; i++) if (tamanhos[grupos[i]] > 1) {
                    double distancia = distanciaQuadrada(dados[i], centros[grupos[i]]);
                    if (distancia > maior) { maior = distancia; escolhido = i; }
                }
                tamanhos[grupos[escolhido]]--;
                grupos[escolhido] = vazio;
                tamanhos[vazio]++;
            }
            centros = new double[k][colunas];
            for (int i = 0; i < dados.length; i++) for (int j = 0; j < colunas; j++) {
                centros[grupos[i]][j] += dados[i][j] / tamanhos[grupos[i]];
                exigirFinito(centros[grupos[i]][j]);
            }
            iteracoes++;
            if (Arrays.equals(anteriores, grupos)) break;
        }
        double inercia = 0;
        for (int i = 0; i < dados.length; i++) {
            inercia += distanciaQuadrada(dados[i], centros[grupos[i]]);
            exigirFinito(inercia);
        }
        return new Resultado(grupos, centros, inercia, iteracoes, semente);
    }

    private static double distanciaQuadrada(double[] a, double[] b) {
        double soma = 0;
        for (int j = 0; j < a.length; j++) { double d = a[j] - b[j]; soma += d * d; }
        exigirFinito(soma);
        return soma;
    }

    private static void exigirFinito(double valor) {
        if (!Double.isFinite(valor)) throw new IllegalArgumentException("Amplitude numérica excessiva para K-Means.");
    }

    private static final class Resultado implements ResultadoKMeans {
        private final int[] grupos;
        private final double[][] centros;
        private final double inercia;
        private final int iteracoes;
        private final long semente;
        Resultado(int[] grupos, double[][] centros, double inercia, int iteracoes, long semente) {
            this.grupos = grupos.clone(); this.centros = copiar(centros);
            this.inercia = inercia; this.iteracoes = iteracoes; this.semente = semente;
        }
        private static double[][] copiar(double[][] matriz) {
            double[][] copia = new double[matriz.length][];
            for (int i = 0; i < matriz.length; i++) copia[i] = matriz[i].clone();
            return copia;
        }
        public int[] getGrupos() { return grupos.clone(); }
        public double[][] getCentroides() { return copiar(centros); }
        public double getInercia() { return inercia; }
        public int getIteracoes() { return iteracoes; }
        public long getSemente() { return semente; }
        public ResultadoKMeans executar(double[][] dados, int k, int maxIteracoes, long semente) {
            return new KMeans().executar(dados, k, maxIteracoes, semente);
        }
    }
}
