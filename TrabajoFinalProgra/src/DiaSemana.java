public enum DiaSemana {


        LUNES("Lunes"),
        MARTES("Martes"),
        MIERCOLES("Miercoles"),
        JUEVES("Jueves");

        private final String nombre;

        DiaSemana(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }