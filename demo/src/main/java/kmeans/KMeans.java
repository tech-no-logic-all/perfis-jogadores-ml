package kmeans;

import java.util.Arrays;
import java.util.Random;


// Agrupa os pontos pela proximidade, sem consultar os perfis dos jogadores
public final class KMeans {
    // Cada linha da matriz e um ponto K diz quantos grupos queremos formar
    // A semente permite repetir o sorteio inicial e o limite evita um laco sem fim
    public ResultadoKMeans executar(double[][] dados, int k, int maxIteracoes, long semente) {
        int colunas = NormalizadorMinMax.validar(dados);
        if (k < 1 || k > dados.length || maxIteracoes < 1)
            throw new IllegalArgumentException("Exija 1 <= K <= quantidade de pontos e limite positivo.");
        // Comecamos com todos os indices disponiveis para o sorteio
        Random sorteio = new Random(semente);
        int[] indices = new int[dados.length];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        double[][] centros = new double[k][];
        // Sorteamos apenas na parte do vetor que ainda nao foi usada
        // A troca coloca o indice escolhido na frente e impede escolher a mesma linha de novo
        // O clone evita que a mudanca de um centro altere os dados originais
        for (int g = 0; g < k; g++) {
            int escolhido = g + sorteio.nextInt(indices.length - g);
            int troca = indices[g]; indices[g] = indices[escolhido]; indices[escolhido] = troca;
            centros[g] = dados[indices[g]].clone();
        }
        int[] grupos = new int[dados.length];
        // Menos um marca os pontos que ainda nao receberam grupo
        Arrays.fill(grupos, -1);
        int iteracoes = 0;
        // Cada rodada atribui os pontos, resolve grupos vazios e recalcula os centros
        // Guardamos os grupos anteriores para saber se o resultado parou de mudar
        while (iteracoes < maxIteracoes) {
            int[] anteriores = grupos.clone();
            int[] tamanhos = new int[k];
            for (int i = 0; i < dados.length; i++) {
                // Em empate, preserve o grupo anterior; na primeira passagem, vence o menor indice
                int melhor = grupos[i] < 0 ? 0 : grupos[i];
                double menor = distanciaQuadrada(dados[i], centros[melhor]);
                for (int g = 0; g < k; g++) {
                    double distancia = distanciaQuadrada(dados[i], centros[g]);
                    if (distancia < menor) { menor = distancia; melhor = g; }
                }
                grupos[i] = melhor;
                tamanhos[melhor]++;
            }
            // Transfira o ponto mais distante de um grupo com pelo menos dois membros
            // Um grupo vazio nao tem media Para resolve-lo, movemos o ponto mais distante
            // do seu centro atual, escolhendo apenas grupos com pelo menos dois integrantes
            // Assim preenchemos o vazio sem esvaziar o grupo que cedeu o ponto
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
            // O novo centro e a media de cada caracteristica dos integrantes do grupo
            // Somar valor dividido pelo tamanho equivale a dividir a soma no final
            centros = new double[k][colunas];
            for (int i = 0; i < dados.length; i++) for (int j = 0; j < colunas; j++) {
                centros[grupos[i]][j] += dados[i][j] / tamanhos[grupos[i]];
                exigirFinito(centros[grupos[i]][j]);
            }
            iteracoes++;
            // Se as atribuicoes finais da rodada nao mudaram, podemos encerrar
            if (Arrays.equals(anteriores, grupos)) break;
        }
        // A inercia soma as distancias ao quadrado de cada ponto ao centro do seu grupo
        // Ela e calculada com os centros finais, no mesmo espaco dos dados recebidos
        double inercia = 0;
        for (int i = 0; i < dados.length; i++) {
            inercia += distanciaQuadrada(dados[i], centros[grupos[i]]);
            exigirFinito(inercia);
        }
        return new Resultado(grupos, centros, inercia, iteracoes, semente);
    }

    // Nao precisamos tirar a raiz para comparar distancias: a ordem continua igual
    private static double distanciaQuadrada(double[] a, double[] b) {
        double soma = 0;
        for (int j = 0; j < a.length; j++) { double d = a[j] - b[j]; soma += d * d; }
        exigirFinito(soma);
        return soma;
    }

    // Interrompe se os calculos ultrapassarem o que um double consegue representar
    private static void exigirFinito(double valor) {
        if (!Double.isFinite(valor)) throw new IllegalArgumentException("Amplitude numérica excessiva para K-Means.");
    }

    // Guarda uma tentativa concluida junto com a semente e o numero de rodadas
    // As copias protegem o resultado contra alteracoes nos arrays de fora
    private static final class Resultado implements ResultadoKMeans {
        private final int[] grupos;
        private final double[][] centros;
        private final double inercia;
        private final int iteracoes;
        private final long semente;
        // Recebe o estado final do algoritmo e guarda suas proprias copias
        Resultado(int[] grupos, double[][] centros, double inercia, int iteracoes, long semente) {
            this.grupos = grupos.clone(); this.centros = copiar(centros);
            this.inercia = inercia; this.iteracoes = iteracoes; this.semente = semente;
        }
        // Cada linha tambem precisa ser copiada, pois uma matriz e um array de arrays
        private static double[][] copiar(double[][] matriz) {
            double[][] copia = new double[matriz.length][];
            for (int i = 0; i < matriz.length; i++) copia[i] = matriz[i].clone();
            return copia;
        }
        // Devolve o grupo atribuido a cada linha, sem expor o vetor interno
        public int[] getGrupos() { return grupos.clone(); }
        // Entrega uma copia dos centros no espaco usado pelo treinamento
        public double[][] getCentroides() { return copiar(centros); }
        // Medida de dispersao desta tentativa
        public double getInercia() { return inercia; }
        // Quantidade de rodadas realizadas antes de parar
        public int getIteracoes() { return iteracoes; }
        // Semente usada no sorteio desta tentativa
        public long getSemente() { return semente; }
        // Mantem o metodo exigido pela interface; uma nova chamada inicia outro treinamento
        public ResultadoKMeans executar(double[][] dados, int k, int maxIteracoes, long semente) {
            return new KMeans().executar(dados, k, maxIteracoes, semente);
        }
    }
}
