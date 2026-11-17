package kmeans;

import java.util.Arrays;

/** Verificações executáveis sem bibliotecas de teste. */
public final class VerificarPessoa2 {
    private static void conferir(boolean condicao) {
        if (!condicao) throw new AssertionError("Falha na verificação do K-Means.");
    }
    private static void perto(double a, double b) { conferir(Math.abs(a - b) < 1e-9); }
    private static void rejeitar(Runnable acao) {
        try { acao.run(); } catch (IllegalArgumentException e) { return; }
        throw new AssertionError("Entrada inválida aceita.");
    }
    private static void verificarResultado(double[][] dados, ResultadoKMeans r) {
        int[] grupos = r.getGrupos();
        double[][] centros = r.getCentroides();
        int[] tamanhos = new int[centros.length];
        double[][] somas = new double[centros.length][dados[0].length];
        double inercia = 0;
        for (int i = 0; i < dados.length; i++) {
            tamanhos[grupos[i]]++;
            for (int j = 0; j < dados[i].length; j++) {
                somas[grupos[i]][j] += dados[i][j];
                double d = dados[i][j] - centros[grupos[i]][j]; inercia += d * d;
            }
        }
        for (int g = 0; g < centros.length; g++) {
            conferir(tamanhos[g] > 0);
            for (int j = 0; j < centros[g].length; j++) perto(centros[g][j], somas[g][j] / tamanhos[g]);
        }
        perto(inercia, r.getInercia());
    }
    public static void main(String[] args) throws Exception {
        KMeans algoritmo = new KMeans();
        double[][] dados = {{0, 0}, {0, 2}, {10, 10}, {10, 12}};
        ResultadoKMeans um = algoritmo.executar(dados, 1, 100, 42);
        perto(um.getCentroides()[0][0], 5); perto(um.getCentroides()[0][1], 6);
        perto(um.getInercia(), 204);
        ResultadoKMeans r = algoritmo.executar(dados, 2, 100, 42);
        perto(r.getInercia(), 4);
        conferir(r.getGrupos()[0] == r.getGrupos()[1]);
        conferir(r.getGrupos()[0] != r.getGrupos()[2]);
        conferir(Arrays.equals(r.getGrupos(), algoritmo.executar(dados, 2, 100, 42).getGrupos()));
        r.getGrupos()[0] = 999; r.getCentroides()[0][0] = 999;
        verificarResultado(dados, r);
        perto(dados[0][0], 0);
        for (int limite : new int[]{1, 100}) for (int k = 1; k <= 4; k++) {
            ResultadoKMeans resultado = algoritmo.executar(dados, k, limite, 9);
            conferir(resultado.getIteracoes() <= limite);
            verificarResultado(dados, resultado);
        }
        double[][] repetidos = {{7}, {7}, {7}, {7}};
        for (int k = 1; k <= 4; k++) {
            ResultadoKMeans resultado = algoritmo.executar(repetidos, k, 100, 0);
            verificarResultado(repetidos, resultado);
            perto(resultado.getInercia(), 0); conferir(resultado.getIteracoes() == 2);
        }
        long[] sementes = {0, 1, 2, 3, 4, 42};
        ExperimentosKMeans experimentos = new ExperimentosKMeans();
        ResultadoKMeans melhor = experimentos.melhor(dados, 2, 100, sementes);
        for (ResultadoKMeans resultado : experimentos.executar(dados, 2, 100, sementes))
            conferir(melhor.getInercia() <= resultado.getInercia());
        conferir(experimentos.melhor(repetidos, 2, 100, sementes).getSemente() == 0);
        rejeitar(() -> algoritmo.executar(dados, 0, 100, 0));
        rejeitar(() -> algoritmo.executar(dados, 5, 100, 0));
        rejeitar(() -> algoritmo.executar(dados, 1, 0, 0));
        rejeitar(() -> algoritmo.executar(new double[][]{{Double.NaN}}, 1, 1, 0));
        rejeitar(() -> algoritmo.executar(new double[][]{{1}, {1, 2}}, 1, 1, 0));
        rejeitar(() -> experimentos.melhor(dados, 1, 1, new long[0]));
        if (args.length > 0) {
            try (java.sql.Connection c = java.sql.DriverManager.getConnection("jdbc:h2:mem:verificacao_pessoa2");
                    java.sql.Statement s = c.createStatement()) {
                for (String sql : java.nio.file.Files.readString(java.nio.file.Path.of(args[0])).split(";"))
                    if (!sql.isBlank()) s.execute(sql);
                ExecutarKMeans.executar(PrepararKMeans.carregar(c), java.nio.file.Path.of("target/verificacoes/pessoa2"));
                conferir(java.nio.file.Files.readAllLines(java.nio.file.Path.of("target/verificacoes/pessoa2/execucoes.csv")).size() == 361);
                conferir(java.nio.file.Files.readAllLines(java.nio.file.Path.of("target/verificacoes/pessoa2/grupos-min-max.csv")).size() == 151);
            }
        }
        System.out.println("Verificações da Pessoa 2 passaram.");
    }
}
