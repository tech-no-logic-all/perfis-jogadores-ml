package kmeans;


/** Entradas para a Pessoa 2; perfis ficam apenas no conjunto para avaliação. */
public final class CenariosKMeans {
    private final ConjuntoDadosJogadores conjunto;
    private final NormalizadorMinMax normalizador = new NormalizadorMinMax();
    public CenariosKMeans(ConjuntoDadosJogadores conjunto) {
        this.conjunto = java.util.Objects.requireNonNull(conjunto);
        normalizador.ajustar(conjunto.getMatrizOriginal());
    }
    public ConjuntoDadosJogadores getConjunto() { return conjunto; }
    public double[][] getOriginais() { return conjunto.getMatrizOriginal(); }
    public double[][] getNormalizados() { return normalizador.transformar(getOriginais()); }
    public double[][] inverterCentroides(double[][] centroides) { return normalizador.inverter(centroides); }
}
