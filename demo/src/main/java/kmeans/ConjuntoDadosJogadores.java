package kmeans;

import modelo.Jogador;
import modelo.Perfil;

import java.util.List;


// Guarda os jogadores na ordem da consulta ao banco
public final class ConjuntoDadosJogadores {
    private final List<Jogador> jogadores;
    // A copia da lista impede adicoes e remocoes que quebrariam o alinhamento das linhas
    ConjuntoDadosJogadores(List<Jogador> jogadores) { this.jogadores = List.copyOf(jogadores); }
    // A lista nao pode ser modificada, e Jogador tambem e imutavel
    public List<Jogador> getJogadores() { return jogadores; }
    // Cada jogador fornece um vetor novo com suas quatro caracteristicas
    // ID, apelido e perfil ficam fora da matriz, pois nao entram na distancia
    public double[][] getMatrizOriginal() {
        double[][] dados = new double[jogadores.size()][];
        for (int i = 0; i < dados.length; i++) dados[i] = jogadores.get(i).getCaracteristicas();
        return dados;
    }
    // O ID da posicao i identifica a linha i quando formos salvar os grupos
    public int[] getIds() {
        int[] ids = new int[jogadores.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = jogadores.get(i).getId();
        }
        return ids;
    }

    // Separa os perfis para avaliar os grupos depois, mantendo a ordem original
    public Perfil[] getPerfis() {
        Perfil[] perfis = new Perfil[jogadores.size()];
        for (int i = 0; i < perfis.length; i++) {
            perfis[i] = jogadores.get(i).getPerfil();
        }
        return perfis;
    }
}
