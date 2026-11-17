package knn;

public enum Metrica {
    EUCLIDIANA {
        public double calcular(double[] a, double[] b) {
            validarVetores(a, b);

            double soma = 0;
            for (int i = 0; i < a.length; i++) {
                soma += Math.pow(a[i] - b[i], 2);
            }
            return Math.sqrt(soma);
        }
    },

    MANHATTAN {
        public double calcular(double[] a, double[] b) {
            validarVetores(a, b);

            double soma = 0;
            for (int i = 0; i < a.length; i++) {
                soma += Math.abs(a[i] - b[i]);
            }
            return soma;
        }
    };

    public abstract double calcular(double[] a, double[] b);

    private static void validarVetores(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Os vetores devem ter o mesmo tamanho.");
        }
    }
}
