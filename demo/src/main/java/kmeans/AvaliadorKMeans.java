package kmeans;

import modelo.Perfil;

// Cruza os grupos encontrados com os perfis reais, somente para avaliar o resultado
public final class AvaliadorKMeans {
    // A ordem das linhas precisa ser a mesma usada no treinamento
    // O perfil da posicao i pertence ao jogador que recebeu grupos[i]
    // O boolean informa se precisamos desfazer o min-max dos centroides
    public AvaliacaoKMeans avaliar(ResultadoKMeans resultado, CenariosKMeans cenarios, boolean normalizado) {
        if (resultado == null || cenarios == null) throw new IllegalArgumentException("Resultado e cenários obrigatórios.");
        int[] grupos = resultado.getGrupos();
        double[][] centros = resultado.getCentroides();
        Perfil[] perfis = cenarios.getConjunto().getPerfis();
        // Se faltarem grupos, nao temos como associar cada jogador ao seu resultado
        if (grupos == null || grupos.length != perfis.length)
            throw new IllegalArgumentException("Quantidade de grupos atribuídos não corresponde aos jogadores.");
        if (NormalizadorMinMax.validar(centros) != 4)
            throw new IllegalArgumentException("Centroides devem ter quatro características.");
        // Cada linha e um grupo; cada coluna e um perfil na ordem do enum
        int[][] tabela = new int[centros.length][Perfil.values().length];
        for (int i = 0; i < grupos.length; i++) {
            if (grupos[i] < 0 || grupos[i] >= centros.length)
                throw new IllegalArgumentException("Rótulo de grupo inválido na linha " + i);
            // ordinal transforma o perfil em um indice de coluna, sem usar esse numero no treino
            tabela[grupos[i]][perfis[i].ordinal()]++;
        }
        // So os centroides normalizados precisam voltar para a escala original
        return new AvaliacaoKMeans(tabela, normalizado ? cenarios.inverterCentroides(centros) : centros);
    }
}
