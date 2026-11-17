public final class AvaliadorKMeans {
    public AvaliacaoKMeans avaliar(ResultadoKMeans resultado, CenariosKMeans cenarios, boolean normalizado) {
        if (resultado == null || cenarios == null) throw new IllegalArgumentException("Resultado e cenários obrigatórios.");
        int[] grupos = resultado.getGrupos();
        double[][] centros = resultado.getCentroides();
        Perfil[] perfis = cenarios.getConjunto().getPerfis();
        if (grupos == null || grupos.length != perfis.length)
            throw new IllegalArgumentException("Quantidade de grupos atribuídos não corresponde aos jogadores.");
        if (NormalizadorMinMax.validar(centros) != 4)
            throw new IllegalArgumentException("Centroides devem ter quatro características.");
        int[][] tabela = new int[centros.length][Perfil.values().length];
        for (int i = 0; i < grupos.length; i++) {
            if (grupos[i] < 0 || grupos[i] >= centros.length)
                throw new IllegalArgumentException("Rótulo de grupo inválido na linha " + i);
            tabela[grupos[i]][perfis[i].ordinal()]++;
        }
        return new AvaliacaoKMeans(tabela, normalizado ? cenarios.inverterCentroides(centros) : centros);
    }
}
