/**
 * HERENCIA (segundo nivel): un competidor es un socio de boxeo que pelea.
 * Hereda todos los datos del socio y agrega peso, récord y estado.
 */
public class Competidor extends Socio {

    public static final int EDAD_MINIMA_COMPETIR = 16;

    private double pesoKg;
    private int victorias;
    private int derrotas;
    private int empates;
    private boolean activo;

    /** Convierte un socio existente en competidor, copiando sus datos personales. */
    public Competidor(Socio base, double pesoKg, int victorias, int derrotas, int empates, boolean activo)
            throws DatosInvalidosException {
        super(base.getId(), base.getNombre(), base.getApellido(), base.getDni(), base.getEdad(),
                base.getTelefono(), base.getEmail(), base.getFechaAlta());
        if (getEdad() < EDAD_MINIMA_COMPETIR) {
            throw new DatosInvalidosException("Para competir hay que tener al menos " + EDAD_MINIMA_COMPETIR + " años (el socio tiene " + getEdad() + ").");
        }
        setPesoKg(pesoKg);
        this.victorias = Validador.entero(victorias, "Victorias", 0, 500);
        this.derrotas = Validador.entero(derrotas, "Derrotas", 0, 500);
        this.empates = Validador.entero(empates, "Empates", 0, 500);
        this.activo = activo;
    }

    public final void setPesoKg(double pesoKg) throws DatosInvalidosException {
        this.pesoKg = Validador.decimal(pesoKg, "Peso (kg)", 30, 200);
    }

    public int getId(){
        return super.getId();
    }

    public double getPesoKg() { return pesoKg; }
    public CategoriaPeso getCategoria() { return CategoriaPeso.dePeso(pesoKg); }
    public int getVictorias() { return victorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    /** Actualiza el récord según el resultado de una pelea. PENDIENTE no modifica nada. */
    public void registrarResultado(ResultadoPelea resultado) {
        switch (resultado) {
            case GANADA -> victorias++;
            case PERDIDA -> derrotas++;
            case EMPATE -> empates++;
            case PENDIENTE -> { }
        }
    }

    @Override
    public String getRol() { return "Competidor"; }

    @Override
    public String resumen() {
        return String.format("[%d] Competidor - %s | %.1f kg (%s) | Récord %d-%d-%d | %s",
                getId(), getNombreCompleto(), pesoKg, getCategoria(),
                victorias, derrotas, empates, activo ? "ACTIVO" : "INACTIVO");
    }
}
