public enum Actividad {
    /** Actividades que ofrece el gimnasio **/
    public enum Actividad {
        GIMNASIO("Gimnasio"),
        BOXEO("Boxeo");

        private final String etiqueta;

        Actividad(String etiqueta) { this.etiqueta = etiqueta; }

        @Override
        public String toString() { return etiqueta; }
    }

}
