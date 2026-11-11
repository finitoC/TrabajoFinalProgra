 /**CLASE ABSTRACTA: datos comunes de cualquier persona del gimnasio.
     * Todos los atributos son privados y se modifican solo mediante setters
     * que validan el dato antes de aceptarlo
     */
    public abstract class Persona {

        public static final int EDAD_MAXIMA = 90;

        private int id;
        private String nombre;
        private String apellido;
        private String dni;
        private int edad;
        private String telefono;
        private String email;
        private final int edadMinima;

        /** Usamos setters en vez de hacer this.__ == __ para poder usar nuestras propias verificaciones en el setter*/
        private Persona(int id, String nombre, String apellido, String dni, int edad,
                          String telefono, String email, int edadMinima) throws DatosInvalidosException {
            if (id < 0) {
                throw new DatosInvalidosException("El id no puede ser negativo.");
            }
            this.id = id;
            this.edadMinima = edadMinima;
            setNombre(nombre);
            setApellido(apellido);
            setDni(dni);
            setEdad(edad);
            setTelefono(telefono);
            setEmail(email);

        }

     public String getNombre() { return nombre; }
     public String getApellido() { return apellido; }
     public String getNombreCompleto() { return apellido + ", " + nombre; }
     public String getDni() { return dni; }
     public int getEdad() { return edad; }
     public String getTelefono() { return telefono; }
     public String getEmail() { return email; }

     public final void setNombre(String nombre) throws DatosInvalidosException {
         this.nombre = Validador.validarNombre(nombre);
     }
     public final void setApellido(String apellido) throws DatosInvalidosException {
         this.apellido = Validador.validarNombre(apellido);
     }
     public final void setDni(String dni) throws DatosInvalidosException {
         this.dni = Validador.validarDni(dni);
     }
     public final void setEdad(int edad) throws DatosInvalidosException {
         this.edad = Validador.validarEdad(edad);
     }
     public final void setTelefono(String telefono) throws DatosInvalidosException {
         this.telefono = Validador.validarTelefono(telefono);
     }
     public final void setEmail(String email) throws DatosInvalidosException {
         this.email = Validador.validarEmail(email);
     }

}
