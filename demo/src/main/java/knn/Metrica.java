package knn;

public enum Metrica {
    EUCLIDIANA {
        public double calcular(double[] a, double[] b) {
            double soma = 0;
            for (int i = 0; i < a.length; i++) soma += Math.pow(a[i] - b[i], 2);
            return Math.sqrt(soma);
        }
    },
    MANHATTAN {
        public double calcular(double[] a, double[] b) {
            double soma = 0;
            for (int i = 0; i < a.length; i++) soma += Math.abs(a[i] - b[i]);
            return soma;
        }
    };

    public abstract double calcular(double[] a, double[] b);
}
