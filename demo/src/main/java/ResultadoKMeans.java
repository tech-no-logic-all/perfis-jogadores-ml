
public interface ResultadoKMeans {
    int[] getGrupos();
    double[][] getCentroides();
    double getInercia();
    int getIteracoes();
    long getSemente();

    ResultadoKMeans executar(
            double[][] dados,
            int k,
            int maxIteracoes,
            long semente);

}
