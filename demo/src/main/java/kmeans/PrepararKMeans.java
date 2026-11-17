package kmeans;

import modelo.Perfil;
import sql.JogadorDAOJdbc;

import java.sql.Connection;
import java.sql.DriverManager;

/** Entrada oficial: conecta a um banco já carregado, sem recriar tabelas. */
public final class PrepararKMeans {
    public static CenariosKMeans carregar(Connection conexao) {
        return new CenariosKMeans(new PreparadorDadosJogadores().carregar(new JogadorDAOJdbc(conexao)));
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Uso: PrepararKMeans <url-jdbc-do-banco-existente>");
        String usuario = System.getenv("JDBC_USER"), senha = System.getenv("JDBC_PASSWORD");
        Connection conexao;
        if (usuario == null) {
            conexao = DriverManager.getConnection(args[0]);
        } else {
            if (senha == null) {
                senha = "";
            }
            conexao = DriverManager.getConnection(args[0], usuario, senha);
        }
        try (Connection c = conexao) {
            CenariosKMeans cenarios = carregar(c);
            System.out.println("Dados conferidos: " + cenarios.getOriginais().length + " jogadores, quatro características; 50 por perfil.");
            System.out.println("Cenários original e min-max preparados. Treinamento e seleção de sementes: Pessoa 2.");
        }
    }
}
