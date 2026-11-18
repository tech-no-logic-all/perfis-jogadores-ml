import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import modelo.Jogador;
import modelo.Perfil;
import sql.JogadorDAOJdbc;
import kmeans.CenariosKMeans;
import kmeans.PrepararKMeans;
import kmeans.ExecutarKMeans;
import knn.Knn;
import knn.ExperimentoKnn;

public class Main {
    public static void main(String[] args) throws Exception {

        List<Jogador> jogadores;
        List<Jogador> copiaJogadores;

        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:jogos");
             Statement s = c.createStatement()) {
            try (BufferedReader leitor = new BufferedReader(new InputStreamReader(
                    Main.class.getResourceAsStream("/sql/jogadores.sql"), StandardCharsets.UTF_8))) {
                String linha;
                while ((linha = leitor.readLine()) != null) {
                    if (!linha.isBlank() && !linha.startsWith("--")) s.execute(linha);
                }
            }

            System.out.println("Linhas (via DAO): " + new JogadorDAOJdbc(c).listarTodos().size());
            consulta(s, "(a)", "SELECT perfil, COUNT(*) FROM jogador GROUP BY perfil ORDER BY perfil");
            consulta(s, "(b)", "SELECT perfil, ROUND(AVG(horas_semana),2), ROUND(AVG(partidas_semana),2), "
                    + "ROUND(AVG(gasto_mensal),2), ROUND(AVG(taxa_vitoria),2) FROM jogador GROUP BY perfil ORDER BY perfil");
            consulta(s, "(c)", "SELECT apelido, perfil, taxa_vitoria FROM jogador "
                    + "WHERE horas_semana > 20 AND taxa_vitoria > 55 ORDER BY taxa_vitoria DESC");

            jogadores = new JogadorDAOJdbc(c).listarTodos();
            ExperimentoKnn experimento = new ExperimentoKnn(jogadores);
            experimento.executar(3, new double[]{10, 30, 50, 48});
            experimento.curvaDeAprendizagem(3, 30);
            // Prepara todos os jogadores do banco para o K-Means, sem dividir treino e teste
            CenariosKMeans cenarios = PrepararKMeans.carregar(c);

            // Executa com e sem normalizacao e salva os resultados e graficos na pasta target/kmeans
            System.out.println("\nExperimentos do K-Means");
            ExecutarKMeans.executar(cenarios, Path.of("target/kmeans"));
        }
    }

    static void consulta(Statement s, String titulo, String sql) throws SQLException {
        System.out.println("\n" + titulo);
        try (ResultSet rs = s.executeQuery(sql)) {
            int n = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= n; i++) sb.append(rs.getString(i)).append('\t');
                System.out.println(sb);
            }
        }
    }
}
