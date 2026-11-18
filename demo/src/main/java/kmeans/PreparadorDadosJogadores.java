package kmeans;

import java.util.HashSet;
import java.util.List;

import modelo.Jogador;
import modelo.Perfil;
import sql.JogadorDAO;

// confere o conjunto do trabalho antes de entregar os numeros ao algoritmo
public final class PreparadorDadosJogadores {
    
    public ConjuntoDadosJogadores carregar(JogadorDAO dao) {
        if (dao == null) {
            throw new IllegalArgumentException("DAO obrigatório.");
        }

        List<Jogador> jogadores = List.copyOf(dao.listarTodos());
        
        if (jogadores.size() != 150) throw new IllegalArgumentException("Esperados 150 jogadores; encontrados " + jogadores.size());
        
        // O vetor conta os perfis // o conjunto de IDs detecta repeticoes
        int[] contagens = new int[Perfil.values().length];
        
        HashSet<Integer> ids = new HashSet<>();
        
        for (Jogador jogador : jogadores) {
            if (!ids.add(jogador.getId())) throw new IllegalArgumentException("ID duplicado: " + jogador.getId());
            contagens[jogador.getPerfil().ordinal()]++;
        }
        // O arquivo do trabalho deve ter exatamente 50 jogadores de cada perfil
        for (Perfil perfil : Perfil.values()) if (contagens[perfil.ordinal()] != 50)
            throw new IllegalArgumentException("Esperados 50 jogadores do perfil " + perfil);
        
        ConjuntoDadosJogadores conjunto = new ConjuntoDadosJogadores(jogadores);
        // A preparacao pode ser usada antes de existir um normalizador
        // valido as quatro caracteristicas antes de normalizar
        // A matriz continua na ordem da consulta para ligar cada grupo ao jogador certo
        
        for (double[] linha : conjunto.getMatrizOriginal()) {
            if (linha.length != 4) {
                throw new IllegalArgumentException("Esperadas quatro características.");
            }
            for (double valor : linha) {
                if (!Double.isFinite(valor)) {
                    throw new IllegalArgumentException("As características devem ser finitas.");
                }
            }
        }
        return conjunto;
    }
}
