import java.util.List;

/** Mantém a mesma ordem da consulta, sem expor a matriz original. */
public final class ConjuntoDadosJogadores {
    private final List<Jogador> jogadores;
    ConjuntoDadosJogadores(List<Jogador> jogadores) { this.jogadores = List.copyOf(jogadores); }
    public List<Jogador> getJogadores() { return jogadores; }
    public double[][] getMatrizOriginal() {
        double[][] dados = new double[jogadores.size()][];
        for (int i = 0; i < dados.length; i++) dados[i] = jogadores.get(i).getCaracteristicas();
        return dados;
    }
    public int[] getIds() {
        int[] ids = new int[jogadores.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = jogadores.get(i).getId();
        }
        return ids;
    }

    public Perfil[] getPerfis() {
        Perfil[] perfis = new Perfil[jogadores.size()];
        for (int i = 0; i < perfis.length; i++) {
            perfis[i] = jogadores.get(i).getPerfil();
        }
        return perfis;
    }
}
