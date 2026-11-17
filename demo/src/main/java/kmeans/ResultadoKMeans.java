package kmeans;



// Define o que as outras classes conseguem consultar depois de uma execucao
public interface ResultadoKMeans {
    // A posicao i informa o grupo da linha i da matriz usada no treinamento
    int[] getGrupos();
    // Os centros ficam na escala usada no treino, original ou normalizada
    double[][] getCentroides();
    // Soma das distancias ao quadrado entre os pontos e seus centros
    double getInercia();
    // Numero de rodadas executadas
    int getIteracoes();
    // Permite identificar e repetir o sorteio inicial
    long getSemente();

    // Metodo mantido no contrato para iniciar uma execucao com os parametros informados
    ResultadoKMeans executar(
            double[][] dados,
            int k,
            int maxIteracoes,
            long semente);

}
