package kmeans;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

/** Experimentos oficiais sobre um banco existente; não cria nem altera tabelas. */
public final class ExecutarKMeans {
    public static final int MAX_ITERACOES = 300;
    private static long[] sementes() {
        long[] sementes = new long[30];
        for (int i = 0; i < sementes.length; i++) sementes[i] = i;
        return sementes;
    }
    public static void executar(CenariosKMeans cenarios, Path pasta) throws Exception {
        Files.createDirectories(pasta);
        ExperimentosKMeans experimentos = new ExperimentosKMeans();
        StringBuilder texto = new StringBuilder("K-Means: sementes 0 a 29; limite de 300 iterações.\n");
        StringBuilder execucoes = new StringBuilder("cenario,k,semente,iteracoes,inercia,pureza\n");
        for (boolean normalizado : new boolean[]{true, false}) {
            String nome = normalizado ? "min-max" : "original";
            double[][] dados = normalizado ? cenarios.getNormalizados() : cenarios.getOriginais();
            double[] inercias = new double[6];
            texto.append('\n').append(nome).append("\nK | Semente | Iterações | Inércia\n");
            for (int k = 1; k <= 6; k++) {
                ResultadoKMeans melhor = null;
                for (ResultadoKMeans r : experimentos.executar(dados, k, MAX_ITERACOES, sementes())) {
                    if (melhor == null || r.getInercia() < melhor.getInercia()) melhor = r;
                    double pureza = new AvaliadorKMeans().avaliar(r, cenarios, normalizado).getPureza();
                    execucoes.append(String.format(Locale.ROOT, "%s,%d,%d,%d,%.12f,%.12f%n",
                            nome, k, r.getSemente(), r.getIteracoes(), r.getInercia(), pureza));
                }
                inercias[k - 1] = melhor.getInercia();
                texto.append(String.format(Locale.ROOT, "%d | %d | %d | %.12f%n",
                        k, melhor.getSemente(), melhor.getIteracoes(), melhor.getInercia()));
                if (k == 3) {
                    texto.append(new AvaliadorKMeans().avaliar(melhor, cenarios, normalizado).formatar());
                    StringBuilder atribuicoes = new StringBuilder("id,grupo\n");
                    int[] ids = cenarios.getConjunto().getIds(), grupos = melhor.getGrupos();
                    for (int i = 0; i < ids.length; i++) atribuicoes.append(ids[i]).append(',').append(grupos[i]).append('\n');
                    Files.writeString(pasta.resolve("grupos-" + nome + ".csv"), atribuicoes);
                }
            }
            new GraficoCotovelo().gerar(inercias, pasta.resolve("cotovelo-" + nome + ".png"), "K-Means - " + nome);
        }
        Files.writeString(pasta.resolve("execucoes.csv"), execucoes);
        Files.writeString(pasta.resolve("resultados.txt"), texto);
        System.out.print(texto);
    }
    public static void main(String[] args) throws Exception {
        if (args.length < 1 || args.length > 2)
            throw new IllegalArgumentException("Uso: ExecutarKMeans <url-jdbc-existente> [pasta-saida]");
        String usuario = System.getenv("JDBC_USER"), senha = System.getenv("JDBC_PASSWORD");
        try (Connection conexao = usuario == null ? DriverManager.getConnection(args[0])
                : DriverManager.getConnection(args[0], usuario, senha == null ? "" : senha)) {
            executar(PrepararKMeans.carregar(conexao), Path.of(args.length == 2 ? args[1] : "target/kmeans"));
        }
    }
}
