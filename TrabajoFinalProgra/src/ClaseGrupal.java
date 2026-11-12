

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** Clase grupal semanal (ej: "Boxeo iniciales", lunes 19:00) con cupo limitado e inscriptos. */
public class ClaseGrupal implements ClaseDescribible {

    private int id;
    private final String nombre;
    private final Actividad actividad;
    private final DayOfWeek dia;
    private final LocalTime hora;
    private final int cupo;
    /** 0 significa "sin entrenador asignado". */
    private int entrenadorId;
    /** COLECCIÓN (Set): ids de socios inscriptos; el Set impide inscribir dos veces al mismo socio. */
    private final Set<Integer> inscriptos = new HashSet<>();

    public ClaseGrupal(int id, String nombre, Actividad actividad, DayOfWeek dia, LocalTime hora,
                       int cupo, int entrenadorId) throws DatosInvalidosException {
        if (id < 0 || entrenadorId < 0) throw new DatosInvalidosException("Referencias de la clase inválidas.");
        if (actividad == null || dia == null || hora == null) {
            throw new DatosInvalidosException("Actividad, día y hora son obligatorios.");
        }
        this.id = id;
        this.nombre = Validador.texto(nombre, "Nombre de la clase", 3, 40);
        this.actividad = actividad;
        this.dia = dia;
        this.hora = hora;
        this.cupo = Validador.entero(cupo, "Cupo", 1, 100);
        this.entrenadorId = entrenadorId;
    }

    public boolean hayCupo() { return inscriptos.size() < cupo; }
    public int cuposLibres() { return cupo - inscriptos.size(); }

    /** Inscribe a un socio. Lanza GimnasioException si no hay cupo o ya estaba inscripto. */
    public void inscribir(int socioId) throws GimnasioException {
        if (inscriptos.contains(socioId)) {
            throw new GimnasioException("El socio ya está inscripto en esta clase.");
        }
        if (!hayCupo()) {
            throw new GimnasioException("La clase '" + nombre + "' no tiene cupo disponible.");
        }
        inscriptos.add(socioId);
    }

    /** Usado por el DAO para reconstruir la clase desde la base de datos. */
    public void cargarInscriptos(Set<Integer> ids) { inscriptos.addAll(ids); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public Actividad getActividad() { return actividad; }
    public DayOfWeek getDia() { return dia; }
    public LocalTime getHora() { return hora; }
    public int getCupo() { return cupo; }
    public int getEntrenadorId() { return entrenadorId; }
    public void setEntrenadorId(int entrenadorId) { this.entrenadorId = entrenadorId; }
    public Set<Integer> getInscriptos() { return Collections.unmodifiableSet(inscriptos); }

    @Override
    public String resumen() {
        return String.format("[%d] %s (%s) | %s %s | cupo %d/%d | entrenador: %s", id, nombre, actividad,
                Utilidades.diaEnEspanol(dia), hora, inscriptos.size(), cupo,
                entrenadorId == 0 ? "sin asignar" : "id " + entrenadorId);
    }
}
