import java.time.LocalDate;

/** Plan de membresía (por ejemplo "Mensual Boxeo" o "Trimestral Gimnasio"). */
public class PlanMembresia implements ClaseDescribible {

    private int id;
    private final String nombre;
    private final Actividad actividad;
    private final int duracionDias;
    private final double precio;

    public PlanMembresia(int id, String nombre, Actividad actividad, int duracionDias, double precio)
            throws DatosInvalidosException {
        if (id < 0) throw new DatosInvalidosException("El id no puede ser negativo.");
        if (actividad == null) throw new DatosInvalidosException("La actividad del plan es obligatoria.");
        this.id = id;
        this.nombre = Validador.texto(nombre, "Nombre del plan", 3, 40);
        this.actividad = actividad;
        this.duracionDias = Validador.entero(duracionDias, "Duración (días)", 1, 730);
        this.precio = Validador.decimal(precio, "Precio", 1, 10_000_000);
    }

    /** Fecha en la que vence un plan que arranca en la fecha indicada. */
    public LocalDate calcularVencimiento(LocalDate desde) {
        return desde.plusDays(duracionDias);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public Actividad getActividad() { return actividad; }
    public int getDuracionDias() { return duracionDias; }
    public double getPrecio() { return precio; }

    @Override
    public String resumen() {
        return String.format("[%d] %s | %s | %d días | $%.2f", id, nombre, actividad, duracionDias, precio);
    }
}
