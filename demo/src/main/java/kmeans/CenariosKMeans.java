package kmeans;



// Mantem os dois conjuntos de entrada sem perder os valores originais
public final class CenariosKMeans {
    
    private final ConjuntoDadosJogadores conjunto;
    private final NormalizadorMinMax normalizador = new NormalizadorMinMax();
   
    // usamos todos os jogadores para descobrir os min e max
    // guardo os ajustes para depois inverter os centros, usando a mesma escala
    public CenariosKMeans(ConjuntoDadosJogadores conjunto) {
        this.conjunto = java.util.Objects.requireNonNull(conjunto); // so incializa se existir um obj nao nulo
        normalizador.ajustar(conjunto.getMatrizOriginal()); // normaliza o conjunto
    }
    
    public ConjuntoDadosJogadores getConjunto() { return conjunto; } // Os jogadores e seus perfis ficam disponiveis para a avaliacao posterior
    public double[][] getOriginais() { return conjunto.getMatrizOriginal(); } // retorna uma copia dos valores vindos do banco
    public double[][] getNormalizados() { return normalizador.transformar(getOriginais()); } // Aplica min-max em uma copia sem sobrescrever o outro cenario
    public double[][] inverterCentroides(double[][] centroides) { return normalizador.inverter(centroides); } // converte os centros para unidades interpretáveis
}
