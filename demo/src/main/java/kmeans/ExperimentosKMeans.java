package kmeans;

/** Seleciona exclusivamente pela inércia; os perfis não participam. */
public final class ExperimentosKMeans {
    public ResultadoKMeans melhor(double[][] dados, int k, int maxIteracoes, long[] sementes) {
        ResultadoKMeans melhor = null;
        for (ResultadoKMeans resultado : executar(dados, k, maxIteracoes, sementes))
            if (melhor == null || resultado.getInercia() < melhor.getInercia()) melhor = resultado;
        return melhor;
    }

    public ResultadoKMeans[] executar(double[][] dados, int k, int maxIteracoes, long[] sementes) {
        if (sementes == null || sementes.length == 0)
            throw new IllegalArgumentException("Informe ao menos uma semente.");
        ResultadoKMeans[] resultados = new ResultadoKMeans[sementes.length];
        for (int i = 0; i < sementes.length; i++)
            resultados[i] = new KMeans().executar(dados, k, maxIteracoes, sementes[i]);
        return resultados;
    }
}
