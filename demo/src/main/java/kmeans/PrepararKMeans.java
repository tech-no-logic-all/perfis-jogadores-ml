package kmeans;

import modelo.Perfil;
import sql.JogadorDAOJdbc;

import java.sql.Connection;
import java.sql.DriverManager;


// Ponto de entrada para conferir os dados e preparar os dois cenarios
public final class PrepararKMeans {
    // Reaproveita a conexao recebida e deixa a consulta por conta do DAO JDBC
    public static CenariosKMeans carregar(Connection conexao) {
        return new CenariosKMeans(new PreparadorDadosJogadores().carregar(new JogadorDAOJdbc(conexao)));
    }
    // A URL deve apontar para um banco que ja tenha a tabela jogador carregada
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Uso: PrepararKMeans <url-jdbc-do-banco-existente>");
        // Credenciais ficam no ambiente para nao gravar senha no codigo
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
        // O try fecha a conexao ao terminar Aqui apenas preparamos os dados, sem treinar
        try (Connection c = conexao) {
            CenariosKMeans cenarios = carregar(c);
            System.out.println("Dados conferidos: " + cenarios.getOriginais().length + " jogadores, quatro características; 50 por perfil.");
            System.out.println("Cenários original e min-max preparados. Treinamento e seleção de sementes: Pessoa 2.");
        }
    }
}
