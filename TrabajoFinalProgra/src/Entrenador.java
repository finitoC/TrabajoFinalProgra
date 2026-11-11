import java.util.HashSet;

    public class Entrenador extends Persona {

        public static final int EDAD_MINIMA = 18;
        /** Edad mínima a la que se puede haber empezado a trabajar como entrenador. */
        private static final int EDAD_INICIO_LABORAL = 16;

        private final HashSet<Actividad> actividades = new HashSet<>();
        private int aniosExperiencia;

        public Entrenador(int id, String nombre, String apellido, String dni, int edad, String telefono, String email, int aniosExperiencia, HashSet<Actividad> actividades) throws DatosInvalidosException {
            super(id, nombre, apellido, dni, edad, telefono, email, EDAD_MINIMA);
            /** Coherencia: no puede tener más años de experiencia que (edad - 16) */
            this.aniosExperiencia = Validador.validarAniosExperiencia(aniosExperiencia, edad);
            if (actividades == null || actividades.isEmpty()) {
                throw new DatosInvalidosException("El entrenador debe tener al menos una actividad asignada.");
            }
            this.actividades.addAll(actividades);
        }

        public int getAniosExperiencia() { return aniosExperiencia; }

        public HashSet<Actividad> getActividades() {
            return actividades;
        }

        public boolean entrena(Actividad actividad) {
            return actividades.contains(actividad);
        }

        public void asignarActividad(Actividad actividad) throws GimnasioException {

            if (actividad == null) {
                throw new GimnasioException("La actividad no puede ser nula.");
            }
            if (!actividades.add(actividad)) {
                throw new GimnasioException("El entrenador ya tiene asignada esa actividad.");
            }
        }
}
