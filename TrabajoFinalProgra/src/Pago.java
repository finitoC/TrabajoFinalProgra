import java.time.LocalDate;

/** Pago de una membresía: deja registrado desde cuándo y hasta cuándo está cubierto el socio.
 * Esta clase es la que relaciona a socio con la actividad que esta haciendo */
public class Pago implements ClaseDescribible {

    private int id;
    private final int socioId;
    private final int planId;
    private final Actividad actividad;
    private final double monto;
    private final LocalDate fechaPago;
    private final LocalDate fechaVencimiento;

    public Pago(int id, int socioId, int planId, Actividad actividad, double monto, LocalDate fechaPago, LocalDate fechaVencimiento) throws DatosInvalidosException {
        if (id < 0 || socioId <= 0 || planId <= 0) throw new DatosInvalidosException("Referencias del pago inválidas.");
        if (actividad == null) throw new DatosInvalidosException("La actividad del pago es obligatoria.");
        this.id = id;
        this.socioId = socioId;
        this.planId = planId;
        this.actividad = actividad;
        this.monto = Validador.decimal(monto, "Monto", 1, 10_000_000);
        this.fechaPago = Validador.fechaEntre(fechaPago, "Fecha de pago", LocalDate.of(2000, 1, 1), LocalDate.now());
        if (fechaVencimiento == null || !fechaVencimiento.isAfter(fechaPago)) {
            throw new DatosInvalidosException("El vencimiento debe ser posterior a la fecha de pago.");
        }
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSocioId() { return socioId; }
    public int getPlanId() { return planId; }
    public Actividad getActividad() { return actividad; }
    public double getMonto() { return monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }

    @Override
    public String resumen() {
        return String.format("[%d] %s | pagó %s | $%.2f | cubre hasta %s", id, actividad, fechaPago, monto, fechaVencimiento);
    }
}
