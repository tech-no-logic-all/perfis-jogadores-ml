
public interface ResultadoKMeans {

    ResultadoKMeans executar(
            double[][] dados,
            int k,
            int maxIteracoes,
            long semente);

}