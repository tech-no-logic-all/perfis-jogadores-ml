package kmeans;


// Repete o algoritmo com sorteios diferentes para reduzir a dependencia de um unico inicio
public final class ExperimentosKMeans {
    // Compara somente a inercia Em empate, fica com a primeira tentativa encontrada
    public ResultadoKMeans melhor(double[][] dados, int k, int maxIteracoes, long[] sementes) {
        ResultadoKMeans melhor = null;
        for (ResultadoKMeans resultado : executar(dados, k, maxIteracoes, sementes))
            if (melhor == null || resultado.getInercia() < melhor.getInercia()) melhor = resultado;
        return melhor;
    }

    // Cada semente produz uma execucao independente, com o mesmo K e limite
    // Devolvemos todas para permitir consultar as medidas sem refazer os treinamentos
    public ResultadoKMeans[] executar(double[][] dados, int k, int maxIteracoes, long[] sementes) {
        if (sementes == null || sementes.length == 0)
            throw new IllegalArgumentException("Informe ao menos uma semente.");
        ResultadoKMeans[] resultados = new ResultadoKMeans[sementes.length];
        for (int i = 0; i < sementes.length; i++)
            resultados[i] = new KMeans().executar(dados, k, maxIteracoes, sementes[i]);
        return resultados;
    }
}
