package kmeans;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;


// Junta a leitura do banco, os experimentos e as saidas do programa
public final class ExecutarKMeans {
    public static final int MAX_ITERACOES = 300;
    // Usamos as sementes de 0 a 29 para repetir os sorteios de forma reproduzivel
    private static long[] sementes() {
        long[] sementes = new long[30];
        for (int i = 0; i < sementes.length; i++) sementes[i] = i;
        return sementes;
    }
    // Cada cenario roda com K de 1 a 6 e com todas as sementes
    // A melhor tentativa de cada K e escolhida somente pela menor inercia
    // A pureza e registrada para avaliacao, mas nao interfere nessa escolha
    public static void executar(CenariosKMeans cenarios, Path pasta) throws Exception {
        Files.createDirectories(pasta);
        ExperimentosKMeans experimentos = new ExperimentosKMeans();
        StringBuilder texto = new StringBuilder("K-Means: sementes 0 a 29; limite de 300 iterações.\n");
        StringBuilder execucoes = new StringBuilder("cenario,k,semente,iteracoes,inercia,pureza\n");
        // Executa uma vez com min-max e outra com os valores originais
        for (boolean normalizado : new boolean[]{true, false}) {
            String nome = normalizado ? "min-max" : "original";
            double[][] dados = normalizado ? cenarios.getNormalizados() : cenarios.getOriginais();
            // A posicao zero guarda K=1, e assim por diante ate K=6
            double[] inercias = new double[6];
            texto.append('\n').append(nome).append("\nK | Semente | Iterações | Inércia\n");
            for (int k = 1; k <= 6; k++) {
                ResultadoKMeans melhor = null;
                // Guarda as medidas de cada tentativa e acompanha a menor inercia
                for (ResultadoKMeans r : experimentos.executar(dados, k, MAX_ITERACOES, sementes())) {
                    if (melhor == null || r.getInercia() < melhor.getInercia()) melhor = r;
                    double pureza = new AvaliadorKMeans().avaliar(r, cenarios, normalizado).getPureza();
                    execucoes.append(String.format(Locale.ROOT, "%s,%d,%d,%d,%.12f,%.12f%n",
                            nome, k, r.getSemente(), r.getIteracoes(), r.getInercia(), pureza));
                }
                inercias[k - 1] = melhor.getInercia();
                texto.append(String.format(Locale.ROOT, "%d | %d | %d | %.12f%n",
                        k, melhor.getSemente(), melhor.getIteracoes(), melhor.getInercia()));
                // Para K=3 tambem mostramos os perfis e salvamos o grupo de cada ID
                if (k == 3) {
                    texto.append(new AvaliadorKMeans().avaliar(melhor, cenarios, normalizado).formatar());
                    StringBuilder atribuicoes = new StringBuilder("id,grupo\n");
                    int[] ids = cenarios.getConjunto().getIds(), grupos = melhor.getGrupos();
                    for (int i = 0; i < ids.length; i++) atribuicoes.append(ids[i]).append(',').append(grupos[i]).append('\n');
                    Files.writeString(pasta.resolve("grupos-" + nome + ".csv"), atribuicoes);
                }
            }
            // Os cenarios recebem graficos separados porque as escalas das inercias diferem
            new GraficoCotovelo().gerar(inercias, pasta.resolve("cotovelo-" + nome + ".png"), "K-Means - " + nome);
        }
        // Salva as medidas calculadas e mostra o resumo no console
        Files.writeString(pasta.resolve("execucoes.csv"), execucoes);
        Files.writeString(pasta.resolve("resultados.txt"), texto);
        System.out.print(texto);
    }
    // Recebe a URL do banco e uma pasta opcional para as saidas
    // A conexao usa as credenciais do ambiente, quando fornecidas, e fecha ao sair do try
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
