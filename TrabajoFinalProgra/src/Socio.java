
import java.time.LocalDate;

/**
 * Guardamos con esta clase el que se anote a alguna actividad/pague una membresia en una fecha determinada.
 * Un mismo socio puede tener membresía de Gimnasio, de Boxeo o de ambas.
 */
public class Socio extends Persona {

    public static final int EDAD_MINIMA = 12;

    private final LocalDate fechaAlta;

    public Socio(int id, String nombre, String apellido, String dni, int edad, String telefono, String email, LocalDate fechaAlta) throws DatosInvalidosException {
        super(id, nombre, apellido, dni, edad, telefono, email, EDAD_MINIMA);
        this.fechaAlta = Validador.fechaEntre(fechaAlta, "Fecha de alta", LocalDate.of(2000, 1, 1), LocalDate.now());
    }

    public LocalDate getFechaAlta() { return fechaAlta; }

    @Override
    public String getRol() { return "Socio"; }

    @Override
    public String resumen() {
        return super.resumen() + " | Alta: " + fechaAlta;
    }
}

