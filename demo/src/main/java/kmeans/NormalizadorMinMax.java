package kmeans;


/** No K-Means ajuste com todos os dados; no KNN, somente com o treino.
 * Colunas constantes transformam em zero e sua inversa retorna a constante. */
public final class NormalizadorMinMax {
    private double[] minimos;
    private double[] maximos;

    static int validar(double[][] dados) {
        if (dados == null || dados.length == 0 || dados[0] == null || dados[0].length == 0)
            throw new IllegalArgumentException("Matriz vazia ou nula.");
        int colunas = dados[0].length;
        for (double[] linha : dados) validarLinha(linha, colunas);
        return colunas;
    }

    private static void validarLinha(double[] linha, int colunas) {
        if (linha == null || linha.length != colunas)
            throw new IllegalArgumentException("Quantidade de características incompatível.");
        for (double valor : linha) if (!Double.isFinite(valor))
            throw new IllegalArgumentException("As características devem ser finitas.");
    }

    public void ajustar(double[][] dados) {
        int n = validar(dados);
        double[] min = dados[0].clone(), max = dados[0].clone();
        for (double[] linha : dados) for (int j = 0; j < n; j++) {
            min[j] = Math.min(min[j], linha[j]);
            max[j] = Math.max(max[j], linha[j]);
        }
        for (int j = 0; j < n; j++) if (!Double.isFinite(max[j] - min[j]))
            throw new IllegalArgumentException("Amplitude numérica excessiva.");
        minimos = min;
        maximos = max;
    }

    private void exigirAjuste() {
        if (minimos == null) throw new IllegalStateException("Ajuste o normalizador primeiro.");
    }

    private double[] aplicar(double[] linha, boolean inversa) {
        exigirAjuste();
        validarLinha(linha, minimos.length);
        double[] saida = new double[linha.length];
        for (int j = 0; j < linha.length; j++) {
            double amplitude = maximos[j] - minimos[j];
            // A inversa devolve as unidades originais, como horas e reais.
            if (inversa) {
                saida[j] = linha[j] * amplitude + minimos[j];
            } else if (amplitude == 0) {
                // Todos os jogadores têm o mesmo valor nesta coluna.
                saida[j] = 0;
            } else {
                saida[j] = (linha[j] - minimos[j]) / amplitude;
            }
            if (!Double.isFinite(saida[j])) throw new IllegalArgumentException("Resultado numérico não finito.");
        }
        return saida;
    }

    private double[][] aplicar(double[][] dados, boolean inversa) {
        exigirAjuste();
        validar(dados);
        double[][] saida = new double[dados.length][];
        for (int i = 0; i < dados.length; i++) saida[i] = aplicar(dados[i], inversa);
        return saida;
    }

    public double[] transformar(double[] linha) { return aplicar(linha, false); }
    public double[][] transformar(double[][] dados) { return aplicar(dados, false); }
    public double[] inverter(double[] linha) { return aplicar(linha, true); }
    public double[][] inverter(double[][] dados) { return aplicar(dados, true); }
    public double[] getMinimos() { exigirAjuste(); return minimos.clone(); }
    public double[] getMaximos() { exigirAjuste(); return maximos.clone(); }
}
