package knn;

import modelo.Jogador;
import modelo.Perfil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Knn {
    private int k;
    private List<Jogador> dadosTreinamento;
    private Metrica metrica;

    public Knn(int k, List<Jogador> dadosTreinamento) {
        this.k = k;
        this.dadosTreinamento = dadosTreinamento;
        this.metrica = metrica;
    }

    //talvez private? nao sei
    public double calcularDistanciaEuclidiana(double[] p, double[] q) throws IllegalArgumentException {

        if (p.length != q.length) {
            throw new IllegalArgumentException("Os vetores devem ter o mesmo tamanho.");
        }

        double[] temporario = new double[p.length];

        for (int i = 0; i < p.length; i++) {
            temporario[i] = Math.pow(p[i] - q[i], 2);
        }

        double soma = 0;

        for (double valor : temporario) {
            soma += valor;
        }
        
        return Math.sqrt(soma);
    }

    public Perfil prever(double[] caracteristicas) {

        List<Vizinho> vizinhos = new ArrayList<>();

        for(Jogador jogador : dadosTreinamento) {
            vizinhos.add(new Vizinho(jogador, metrica.calcular(caracteristicas, jogador.getCaracteristicas())));
        }

        Collections.sort(vizinhos);

        int contadorCasual = 0;
        int contadorRegular = 0;
        int contadorRanqueado = 0;

        for(int i = 0; i < k; i++) {
            if(vizinhos.get(i).getJogador().getPerfil() == Perfil.CASUAL) {
                contadorCasual++;
            } else if(vizinhos.get(i).getJogador().getPerfil() == Perfil.REGULAR) {
                contadorRegular++;
            } else if(vizinhos.get(i).getJogador().getPerfil() == Perfil.RANQUEADO) {
                contadorRanqueado++;
            }
        }

        if (contadorCasual > contadorRegular && contadorCasual > contadorRanqueado) {
            return Perfil.CASUAL;
        } else if (contadorRegular > contadorCasual && contadorRegular > contadorRanqueado) {
            return Perfil.REGULAR;
        } else if (contadorRanqueado > contadorCasual && contadorRanqueado > contadorRegular) {
            return Perfil.RANQUEADO;
        } else if (contadorCasual == contadorRegular && contadorCasual > contadorRanqueado) {
            for(int i = 0; i < k; i++) {
                if(vizinhos.get(i).getJogador().getPerfil() == Perfil.CASUAL) {
                    return Perfil.CASUAL;
                } else if(vizinhos.get(i).getJogador().getPerfil() == Perfil.REGULAR) {
                    return Perfil.REGULAR;
                }
            }
        } else if (contadorCasual == contadorRanqueado && contadorCasual > contadorRegular) {
            for (int i = 0; i < k; i++) {
                if(vizinhos.get(i).getJogador().getPerfil() == Perfil.CASUAL) {
                    return Perfil.CASUAL;
                } else if(vizinhos.get(i).getJogador().getPerfil() == Perfil.RANQUEADO) {
                    return Perfil.RANQUEADO;
                }
            }
        } else if (contadorRegular == contadorRanqueado && contadorRegular > contadorCasual) {
            for (int i = 0; i < k; i++) {
                if(vizinhos.get(i).getJogador().getPerfil() == Perfil.REGULAR) {
                    return Perfil.REGULAR;
                } else if(vizinhos.get(i).getJogador().getPerfil() == Perfil.RANQUEADO) {
                    return Perfil.RANQUEADO;
                }
            }
        } else if (contadorCasual == contadorRegular && contadorRegular == contadorRanqueado) {
            return vizinhos.get(0).getJogador().getPerfil();
        }

        throw new IllegalStateException("Não foi possível prever o perfil.");
    }
}
