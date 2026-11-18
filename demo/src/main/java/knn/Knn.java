package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Jogador;
import modelo.Perfil;

public class Knn {
    private int k;
    private List<Jogador> dadosTreinamento;
    private Metrica metrica;
    private NormalizadorMinMax normalizador;

    public Knn(int k, List<Jogador> dadosTreinamento, MedidaDeDistancia medida, NormalizadorMinMax normalizador) {
        this.k = k;
        this.dadosTreinamento = dadosTreinamento;
        this.medida = medida;
        this.normalizador = normalizador;
    }

    public double[] prepararCaracteristicas(double[] caracteristicas) {
        //para facilitar quando for comparar os resultados com ou sem normalizacao
        if(normalizador != null) {
            return normalizador.transformar(caracteristicas);
        } else {
            return caracteristicas;
        }
    }

    public Perfil prever(double[] caracteristicas) {

        caracteristicas = prepararCaracteristicas(caracteristicas);

        List<Vizinho> vizinhos = new ArrayList<>();
        
        for(Jogador jogador : dadosTreinamento) {

            double[] caracteristicasJogador = prepararCaracteristicas(jogador.getCaracteristicas());

            vizinhos.add(new Vizinho(jogador, medida.calcular(caracteristicas, caracteristicasJogador)));
        }

        Collections.sort(vizinhos);

        int contadorCasual = 0;
        int contadorRegular = 0;
        int contadorRanqueado = 0;

        for(int i = 0; i < k; i++) {
            switch (vizinhos.get(i).getJogador().getPerfil()) {
                case CASUAL:
                    contadorCasual++;
                    break;
                case REGULAR:
                    contadorRegular++;
                    break;
                case RANQUEADO:
                    contadorRanqueado++;
                    break;
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
