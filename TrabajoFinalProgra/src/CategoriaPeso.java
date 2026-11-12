public enum CategoriaPeso {
    MOSCA(50.8), PLUMA(57.2), LIGERO(61.2), WELTER(66.7),
    MEDIANO(72.6), SEMIPESADO(79.4), PESADO(Double.MAX_VALUE);

    private final double limiteKg;

    CategoriaPeso(double limiteKg) { this.limiteKg = limiteKg; }

    /** Devuelve la categoría que le corresponde a un peso en kilos. */
    public static CategoriaPeso dePeso(double kg) {
        for (CategoriaPeso c : values()) {
            if (kg <= c.limiteKg) return c;
        }
        return PESADO;
    }
}
