package knn;

public class PrevisaoImpossivelException extends RuntimeException {
    public PrevisaoImpossivelException() {
        super("Não foi possível prever o perfil do jogador");
    }
}
