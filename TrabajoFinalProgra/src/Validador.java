public class Validador {

    public static String validarNombre(String nombre) throws DatosInvalidosException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío.");
        }

        if (nombre.trim().length() < 2 || nombre.trim().length() > 40) {
            throw new DatosInvalidosException("El nombre debe tener entre 2 y 40 caracteres.");
        }

        return nombre.trim();
    }

    public static String validarApellido(String apellido) throws DatosInvalidosException {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new DatosInvalidosException("El apellido no puede estar vacío.");
        }

        if (apellido.trim().length() < 2 || apellido.trim().length() > 40) {
            throw new DatosInvalidosException("El apellido debe tener entre 2 y 40 caracteres.");
        }

        return apellido.trim();
    }

    public static String validarDni(String dni) throws DatosInvalidosException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new DatosInvalidosException("El DNI no puede estar vacío.");
        }

        if (dni.length() < 7 || dni.length() > 8) {
            throw new DatosInvalidosException("El DNI debe tener entre 7 y 8 caracteres.");
        }

        for (int i = 0; i < dni.length(); i++) {
            if (!Character.isDigit(dni.charAt(i))) {
                throw new DatosInvalidosException("El DNI solo puede contener números.");
            }
        }

        return dni.trim();
    }

    public static int validarEdad(int edad) throws DatosInvalidosException {
        if (edad < 12 || edad > 90) {
            throw new DatosInvalidosException("La edad debe estar entre 16 y 100 años.");
        }

        return edad;
    }

    public static String validarTelefono(String telefono) throws DatosInvalidosException {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new DatosInvalidosException("El teléfono no puede estar vacío.");
        }

        if (telefono.trim().length() < 8 || telefono.trim().length() > 15) {
            throw new DatosInvalidosException("El teléfono debe tener entre 8 y 15 caracteres.");
        }

        for (int i = 0; i < telefono.length(); i++) {
            if (!Character.isDigit(telefono.charAt(i))) {
                throw new DatosInvalidosException("El teléfono solo puede contener números.");
            }
        }

        return telefono.trim();
    }

    public static String validarEmail(String email) throws DatosInvalidosException {
        if (email == null || email.trim().isEmpty()) {
            throw new DatosInvalidosException("El email no puede estar vacío.");
        }

        if (!email.contains("@") || !email.contains(".")) {
            throw new DatosInvalidosException("El email no tiene un formato válido.");
        }

        return email.trim();
    }
}

