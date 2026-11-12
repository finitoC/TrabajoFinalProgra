import java.time.LocalDateTime;

public class Asistencia implements ClaseDescribible {

    private int id;
    private final int socioId;
    private final Actividad actividad;
    private final LocalDateTime fechaHora;

    public Asistencia(int id, int socioId, Actividad actividad, LocalDateTime fechaHora) throws DatosInvalidosException {
        if (id < 0 || socioId <= 0) throw new DatosInvalidosException("Referencias de la asistencia inválidas.");
        if (actividad == null || fechaHora == null) throw new DatosInvalidosException("Actividad y fecha son obligatorias.");
        this.id = id;
        this.socioId = socioId;
        this.actividad = actividad;
        this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSocioId() { return socioId; }
    public Actividad getActividad() { return actividad; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    @Override
    public String resumen() {
        return String.format("[%d] %s | %s %s", id, actividad, fechaHora.toLocalDate(),
                fechaHora.toLocalTime().withNano(0));
    }
}
