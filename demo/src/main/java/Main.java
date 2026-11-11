import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        List<Jogador> jogadores = List.of(
                new Jogador(1, "jogador1", 1.0, 1, 1.0, 1.0, Perfil.CASUAL),
                new Jogador(2, "jogador2", 3.0, 3, 3.0, 3.0, Perfil.RANQUEADO)
        );

        //testezinho da distancia euclidiana
        Knn knn = new Knn(1, jogadores);

        knn.prever(new double[]{0.0, 0, 0.0, 0.0});

        /*try (Connection c = DriverManager.getConnection("jdbc:h2:mem:jogos");
             Statement s = c.createStatement()) {
            for (String linha : Files.readAllLines(Path.of("jogadores.sql")))
                if (!linha.isBlank() && !linha.startsWith("--")) s.execute(linha);

            System.out.println("Linhas (via DAO): " + new JogadorDAOJdbc(c).listarTodos().size());
            consulta(s, "(a)", "SELECT perfil, COUNT(*) FROM jogador GROUP BY perfil ORDER BY perfil");
            consulta(s, "(b)", "SELECT perfil, ROUND(AVG(horas_semana),2), ROUND(AVG(partidas_semana),2), "
                    + "ROUND(AVG(gasto_mensal),2), ROUND(AVG(taxa_vitoria),2) FROM jogador GROUP BY perfil ORDER BY perfil");
            consulta(s, "(c)", "SELECT apelido, perfil, taxa_vitoria FROM jogador "
                    + "WHERE horas_semana > 20 AND taxa_vitoria > 55 ORDER BY taxa_vitoria DESC");
        }*/
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
