import java.nio.file.*;
import java.sql.*;
import java.util.*;
import javax.imageio.ImageIO;

/** Dados artificiais e banco em memória exclusivos de verificação. Sem ML. */
public final class VerificarPessoa1 {
    private static void conferir(boolean valor, String mensagem) {
        if (!valor) throw new AssertionError(mensagem);
    }
    private static void perto(double a, double b) { conferir(Math.abs(a - b) < 1e-9, a + " != " + b); }
    private static void rejeitar(Runnable acao) {
        try { acao.run(); } catch (IllegalArgumentException | IllegalStateException e) { return; }
        throw new AssertionError("Entrada inválida aceita.");
    }
    private static ResultadoKMeans resultado(int[] grupos, double[][] centros) {
        return new ResultadoKMeans() {
            public int[] getGrupos() { return grupos; }
            public double[][] getCentroides() { return centros; }
            public double getInercia() { return 0; }
            public int getIteracoes() { return 0; }
            public long getSemente() { return 0; }
            public ResultadoKMeans executar(double[][] d, int k, int m, long s) {
                throw new UnsupportedOperationException("Fixture de avaliação, sem treinamento.");
            }
        };
    }
    public static void main(String[] args) throws Exception {
        NormalizadorMinMax n = new NormalizadorMinMax();
        rejeitar(() -> n.transformar(new double[]{1}));
        rejeitar(() -> n.inverter(new double[]{1}));
        double[][] entrada = {{10, 5}, {20, 5}};
        n.ajustar(entrada);
        double[][] z = n.transformar(entrada);
        perto(z[0][0], 0); perto(z[1][0], 1); perto(z[1][1], 0);
        conferir(Arrays.deepEquals(entrada, n.inverter(z)), "Round-trip");
        perto(n.inverter(new double[]{0.5, 0})[0], 15);
        perto(n.transformar(new double[]{30, 5})[0], 2); // Teste KNN não reajusta nem limita a [0,1].
        n.getMinimos()[0] = -999;
        perto(n.getMinimos()[0], 10);
        rejeitar(() -> n.ajustar(new double[][]{{1}, {1, 2}}));
        rejeitar(() -> n.ajustar(new double[][]{{Double.NaN}}));
        perto(entrada[0][0], 10);

        List<Jogador> lista = new ArrayList<>();
        for (int i = 0; i < 150; i++) lista.add(new Jogador(1000 + i, "Teste" + i, i, i + 1, i * 2, 50, Perfil.values()[i % 3]));
        JogadorDAO dao = new JogadorDAO() {
            public List<Jogador> listarTodos() { return lista; }
            public Optional<Jogador> buscarPorId(int id) { throw new UnsupportedOperationException(); }
            public List<Jogador> listarPorPerfil(Perfil p) { throw new UnsupportedOperationException(); }
        };
        CenariosKMeans c = new CenariosKMeans(new PreparadorDadosJogadores().carregar(dao));
        double[][] antes = c.getOriginais();
        int[] grupos = new int[150];
        for (int i = 0; i < 150; i++) {
            grupos[i] = i % 3;
            conferir(c.getConjunto().getIds()[i] == 1000 + i, "Alinhamento ID");
            perto(antes[i][0], i);
        }
        double[][] centros = {{0, 0, 0, 0}, {0.5, 0.5, 0.5, 0}, {1, 1, 1, 0}, {0, 0, 0, 0}};
        AvaliadorKMeans avaliador = new AvaliadorKMeans();
        AvaliacaoKMeans a = avaliador.avaliar(resultado(grupos, centros), c, true);
        perto(a.getPureza(), 1);
        int total = 0;
        for (int tamanho : a.getTamanhos()) total += tamanho;
        conferir(total == 150, "Total de grupos");
        conferir(a.getTamanhos()[3] == 0, "Grupo vazio");
        perto(a.getCentroidesOriginais()[1][0], 74.5);
        int soma = 0; for (int[] linha : a.getTabela()) for (int v : linha) soma += v;
        conferir(soma == 150, "Tabela total");
        a.getTabela()[0][0] = -1; conferir(a.getTabela()[0][0] == 50, "Cópia defensiva");
        rejeitar(() -> avaliador.avaliar(resultado(new int[1], centros), c, true));
        int[] ruins = grupos.clone(); ruins[0] = 4;
        rejeitar(() -> avaliador.avaliar(resultado(ruins, centros), c, true));
        int[] misturados = new int[150];
        perto(avaliador.avaliar(resultado(misturados, centros), c, false).getPureza(), 1.0 / 3);
        GraficoCotovelo grafico = new GraficoCotovelo();
        Path png = Path.of("target/verificacoes/cotovelo-artificial.png");
        grafico.gerar(new double[]{6, 5, 4, 3, 2, 1}, png, "TESTE ARTIFICIAL - sem resultados oficiais");
        conferir(ImageIO.read(png.toFile()).getWidth() == 960, "PNG");
        rejeitar(() -> { try { grafico.gerar(new double[]{1}, png, "Teste"); } catch (java.io.IOException e) { throw new RuntimeException(e); } });
        rejeitar(() -> { try { grafico.gerar(new double[]{1, 2, 3, 4, 5, Double.NaN}, png, "Teste"); } catch (java.io.IOException e) { throw new RuntimeException(e); } });
        conferir(Arrays.deepEquals(antes, c.getOriginais()), "Dados originais alterados");
        conferir(lista.size() == 150 && lista.get(0).getId() == 1000, "Lista alterada");
        lista.remove(0); rejeitar(() -> new PreparadorDadosJogadores().carregar(dao));

        if (args.length > 0) {
            // Script executado exclusivamente em banco descartável; nunca no banco oficial.
            try (Connection conexao = DriverManager.getConnection("jdbc:h2:mem:verificacao_pessoa1"); Statement s = conexao.createStatement()) {
                for (String sql : Files.readString(Path.of(args[0])).split(";")) if (!sql.isBlank()) s.execute(sql);
                CenariosKMeans real = PrepararKMeans.carregar(conexao);
                conferir(real.getOriginais().length == 150, "JDBC");
                for (double[] linha : real.getNormalizados()) for (double v : linha) conferir(v >= 0 && v <= 1, "Min-max oficial");
                System.out.println("JDBC: 150 registros do SQL conferidos via DAO, 50 por perfil.");
            }
        }
        System.out.println("Verificações da Pessoa 1 passaram. Gráfico artificial em " + png);
    }
}
