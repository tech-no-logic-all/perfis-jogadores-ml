package kmeans;



// Mantem os dois conjuntos de entrada sem perder os valores originais
public final class CenariosKMeans {
    private final ConjuntoDadosJogadores conjunto;
    private final NormalizadorMinMax normalizador = new NormalizadorMinMax();
    // No K-Means usamos todos os jogadores para descobrir os minimos e maximos
    // Guardamos esse ajuste para inverter os centros depois, usando a mesma escala
    public CenariosKMeans(ConjuntoDadosJogadores conjunto) {
        this.conjunto = java.util.Objects.requireNonNull(conjunto);
        normalizador.ajustar(conjunto.getMatrizOriginal());
    }
    
    public ConjuntoDadosJogadores getConjunto() { return conjunto; } // Os jogadores e seus perfis ficam disponiveis para a avaliacao posterior
    public double[][] getOriginais() { return conjunto.getMatrizOriginal(); } // Entrega uma copia dos valores vindos do banco
    public double[][] getNormalizados() { return normalizador.transformar(getOriginais()); } // Aplica min-max em uma copia, sem sobrescrever o outro cenario
    public double[][] inverterCentroides(double[][] centroides) { return normalizador.inverter(centroides); } // Converte os centros para unidades que conseguimos interpretar
}
