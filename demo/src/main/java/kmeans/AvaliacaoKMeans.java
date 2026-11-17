package kmeans;

import modelo.Perfil;

import java.util.Locale;


// Guarda as medidas calculadas depois do agrupamento Aqui nao acontece treinamento
public final class AvaliacaoKMeans {
    private final int[][] tabela;
    private final double[][] centroides;
    private final double pureza;
    // Copiamos as matrizes para que uma mudanca fora desta classe nao altere a avaliacao
    // Para a pureza, pegamos a maior contagem de perfil de cada grupo
    // Depois somamos essas contagens e dividimos pelo total de jogadores
    AvaliacaoKMeans(int[][] tabela, double[][] centroides) {
        this.tabela = new int[tabela.length][];
        this.centroides = new double[centroides.length][];
        int total = 0, majoritarios = 0;
        for (int g = 0; g < tabela.length; g++) {
            this.tabela[g] = tabela[g].clone();
            this.centroides[g] = centroides[g].clone();
            int maior = 0;
            for (int quantidade : tabela[g]) { total += quantidade; maior = Math.max(maior, quantidade); }
            majoritarios += maior;
        }
        if (total == 0) throw new IllegalArgumentException("Avaliação sem jogadores.");
        // O cast para double evita uma divisao inteira, que perderia a parte decimal
        pureza = (double) majoritarios / total;
    }
    // Devolve a pureza entre zero e um
    public double getPureza() { return pureza; }
    // Copia cada linha, porque copiar so a matriz externa ainda compartilharia os vetores
    public int[][] getTabela() {
        int[][] copia = new int[tabela.length][];
        for (int g = 0; g < copia.length; g++) copia[g] = tabela[g].clone();
        return copia;
    }
    // Os centros ja estao em horas, partidas, reais e porcentagem
    public double[][] getCentroidesOriginais() {
        double[][] copia = new double[centroides.length][];
        for (int g = 0; g < copia.length; g++) copia[g] = centroides[g].clone();
        return copia;
    }
    // Somar os perfis de uma linha da tabela da o tamanho daquele grupo
    public int[] getTamanhos() {
        int[] tamanhos = new int[tabela.length];
        for (int g = 0; g < tabela.length; g++) for (int n : tabela[g]) tamanhos[g] += n;
        return tamanhos;
    }
    // Monta a saida do console: primeiro os tamanhos e centros, depois os perfis
    // Locale.ROOT mantem o mesmo formato numerico em computadores diferentes
    public String formatar() {
        StringBuilder texto = new StringBuilder("Grupo | Tamanho | Horas/semana | Partidas/semana | Gasto (R$) | Vitória (%)\n");
        int[] tamanhos = getTamanhos();
        for (int g = 0; g < tabela.length; g++) {
            texto.append(g).append(" | ").append(tamanhos[g]);
            for (double valor : centroides[g]) texto.append(String.format(Locale.ROOT, " | %.4f", valor));
            texto.append('\n');
        }
        texto.append("Grupo");
        for (Perfil perfil : Perfil.values()) texto.append(" | ").append(perfil);
        texto.append('\n');
        for (int g = 0; g < tabela.length; g++) {
            texto.append(g);
            for (int n : tabela[g]) texto.append(" | ").append(n);
            texto.append('\n');
        }
        return texto.append(String.format(Locale.ROOT, "Pureza: %.6f (%.2f%%)%n", pureza, pureza * 100)).toString();
    }
}
