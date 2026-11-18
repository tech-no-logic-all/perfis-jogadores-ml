package kmeans;


// coloca as caracteristicas em uma escala comum usando minimo e maximo
// o ajuste usa todos os dados

public final class NormalizadorMinMax {
    private double[] minimos;
    private double[] maximos;

    // confere se existe uma matriz preenchida e com o mesmo tamanho em todas as linhas
    static int validar(double[][] dados) {
        if (dados == null || dados.length == 0 || dados[0] == null || dados[0].length == 0)
            throw new IllegalArgumentException("Matriz vazia ou nula.");
        int colunas = dados[0].length;
        for (double[] linha : dados) validarLinha(linha, colunas);
        return colunas;
    }

    // Valores NaN ou infinitos impedem calcular distancias e medias corretamente
    private static void validarLinha(double[] linha, int colunas) {
        if (linha == null || linha.length != colunas)
            throw new IllegalArgumentException("Quantidade de características incompatível.");
        for (double valor : linha) if (!Double.isFinite(valor))
            throw new IllegalArgumentException("As características devem ser finitas.");
    }

    // Descobre os limites de cada coluna sem alterar os dados recebidos
    // Comecamos pelos valores da primeira linha e comparamos com as demais
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

    // Sem os limites aprendidos, ainda nao temos como transformar ou inverter
    private void exigirAjuste() {
        if (minimos == null) throw new IllegalStateException("Ajuste o normalizador primeiro.");
    }

    // Usa os limites guardados Na ida aplicamos min-max; na volta recuperamos a escala
    // Uma coluna constante recebe zero na ida e volta ao seu valor fixo na inversa
    private double[] aplicar(double[] linha, boolean inversa) {
        exigirAjuste();
        validarLinha(linha, minimos.length);
        double[] saida = new double[linha.length];
        for (int j = 0; j < linha.length; j++) {
            double amplitude = maximos[j] - minimos[j];
            // A inversa devolve as unidades originais, como horas e reais
            if (inversa) {
                saida[j] = linha[j] * amplitude + minimos[j];
            } else if (amplitude == 0) {
                // Todos os jogadores tem o mesmo valor nesta coluna
                saida[j] = 0;
            } else {
                saida[j] = (linha[j] - minimos[j]) / amplitude;
            }
            if (!Double.isFinite(saida[j])) throw new IllegalArgumentException("Resultado numérico não finito.");
        }
        return saida;
    }

    // Aplica a mesma regra linha por linha, criando uma matriz nova
    private double[][] aplicar(double[][] dados, boolean inversa) {
        exigirAjuste();
        validar(dados);
        double[][] saida = new double[dados.length][];
        for (int i = 0; i < dados.length; i++) saida[i] = aplicar(dados[i], inversa);
        return saida;
    }

    // Versao para um unico jogador, usando os limites do ajuste
    public double[] transformar(double[] linha) { return aplicar(linha, false); }
    // Versao para o conjunto completo, sem recalcular os limites
    public double[][] transformar(double[][] dados) { return aplicar(dados, false); }
    // Desfaz a escala de um unico vetor, por exemplo um centroide
    public double[] inverter(double[] linha) { return aplicar(linha, true); }
    // Desfaz a escala de todos os vetores recebidos
    public double[][] inverter(double[][] dados) { return aplicar(dados, true); }
    // Devolve uma copia para que o ajuste nao seja alterado pelo chamador
    public double[] getMinimos() { exigirAjuste(); return minimos.clone(); }
    // Os maximos tambem saem por copia
    public double[] getMaximos() { exigirAjuste(); return maximos.clone(); }
}
