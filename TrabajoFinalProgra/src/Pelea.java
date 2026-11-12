import java.time.LocalDate;

/** Pelea de un competidor contra un rival, en una fecha y lugar. */
public class Pelea implements ClaseDescribible {

    private int id;
    private final int competidorId;
    private final String rival;
    private final LocalDate fecha;
    private final String lugar;
    private ResultadoPelea resultado;

    public Pelea(int id, int competidorId, String rival, LocalDate fecha, String lugar, ResultadoPelea resultado)
            throws DatosInvalidosException {
        if (id < 0 || competidorId <= 0) throw new DatosInvalidosException("Referencias de la pelea inválidas.");
        if (resultado == null) throw new DatosInvalidosException("El resultado es obligatorio.");
        this.id = id;
        this.competidorId = competidorId;
        this.rival = Validador.texto(rival, "Rival", 2, 60);
        LocalDate hoy = LocalDate.now();
        this.fecha = Validador.fechaEntre(fecha, "Fecha de la pelea", hoy.minusYears(40), hoy.plusYears(2));
        this.lugar = Validador.texto(lugar, "Lugar", 2, 60);
        this.resultado = resultado;
    }

    /** Una pelea "por venir" */
    public boolean esProxima(LocalDate hoy) {
        return resultado == ResultadoPelea.PENDIENTE && !fecha.isBefore(hoy);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCompetidorId() {
        return competidorId;
    }
    public String getRival() {
        return rival;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public String getLugar() {
        return lugar;
    }
    public ResultadoPelea getResultado() {
        return resultado;
    }
    public void setResultado(ResultadoPelea resultado) {
        this.resultado = resultado;
    }

    @Override
    public String resumen() {
        return String.format("[%d] Competidor %d vs %s | %s en %s | %s", id, competidorId, rival, fecha, lugar, resultado);
    }
}