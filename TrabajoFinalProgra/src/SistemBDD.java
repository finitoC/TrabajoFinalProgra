import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class SistemBDD {

    /**
     * Abre una conexión nueva. Quien la pida es responsable de cerrarla (try-with-resources).
     */
    private static Connection conectar() throws GimnasioException {
        try {
            return DBHandler.obtenerConexion();
        } catch (SQLException e) {
            throw new GimnasioException("No se pudo conectar a la base de datos: " + e.getMessage());
        }
    }

    //Socio

    /**
     * Inserta el socio y le asigna el id que generó MySQL.
     */
    public static Socio guardarSocio(Socio socio) throws GimnasioException, DatosInvalidosException {
        if (socio == null) {
            throw new DatosInvalidosException("El socio no puede ser nulo.");
        }
        if (socio.getDni() == null || socio.getDni().trim().isEmpty()) {
            throw new DatosInvalidosException("El DNI del socio es obligatorio.");
        }

        String sql = "INSERT INTO socio (nombre, apellido, dni, edad, telefono, email, fecha_alta) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, socio.getNombre());
            ps.setString(2, socio.getApellido());
            ps.setString(3, socio.getDni());
            ps.setInt(4, socio.getEdad());
            ps.setString(5, socio.getTelefono());
            ps.setString(6, socio.getEmail());
            ps.setObject(7, socio.getFechaAlta());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new GimnasioException("No se pudo obtener el id del socio guardado.");
                }
                int idGenerado = rs.getInt(1);

                // Llamamos al constructor pasando el id generado y los getters del socio
                return new Socio(
                        idGenerado,
                        socio.getNombre(),
                        socio.getApellido(),
                        socio.getDni(),
                        socio.getEdad(),
                        socio.getTelefono(),
                        socio.getEmail(),
                        socio.getFechaAlta()
                );
            }
            ///SQLIntegrityConstraintViolationException  es una excepcion que comprueba que no se esten violando las normas
        ///de la tabla de mysql, por ejemplo si intentamos ingresar un dni de un socio duplicado va a tirar esta excepcion
        /// porque DNI en la bdd es unique
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GimnasioException("Ya existe un socio con el DNI " + socio.getDni() + ".");
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar el socio: " + e.getMessage());
        }
    }

    public static Socio buscarSocio(int id) throws GimnasioException, DatosInvalidosException {
        if (id <= 0) {
            throw new DatosInvalidosException("El ID del socio debe ser mayor a cero.");
        }

        String sql = "SELECT * FROM socio WHERE id = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new GimnasioException("No existe un socio con id " + id + ".");
                }
                return new Socio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        LocalDate.parse(rs.getString("fecha_alta")) // o "fechaAlta" según cómo se llame la columna en tu tabla
                );
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar el socio: " + e.getMessage());
        }
    }

    public static ArrayList<Socio> listarSocios() throws GimnasioException {
        ArrayList<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socio ORDER BY apellido, nombre";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                socios.add(new Socio(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("dni"),
                        rs.getInt("edad"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        LocalDate.parse(rs.getString("fecha_alta")) // o "fechaAlta" según tu tabla
                ));
            }
            return socios;
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar los socios: " + e.getMessage());
        }


    }

    public static void guardarPago(Pago pago) throws GimnasioException {
        if(pago == null){
            throw new GimnasioException("El pago esta vacio");
        }
        String sql = "INSERT INTO pago (socio_id, plan_id, actividad, monto, fecha_pago, fecha_vencimiento) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pago.getSocioId());
            ps.setInt(2, pago.getPlanId());
            ps.setString(3, pago.getActividad().name());
            ps.setDouble(4, pago.getMonto());
            ps.setObject(5, pago.getFechaPago());
            ps.setObject(6, pago.getFechaVencimiento());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) {
                    throw new GimnasioException("No se pudo obtener el id del pago guardado.");
                }
                pago.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar el pago: " + e.getMessage());
        }
    }

    public static ArrayList<Pago> listarPagosDeSocio(int socioId) throws GimnasioException {
        if (socioId <= 0){
            throw new DatosInvalidosException("El id del socio no existe");
        }
        ArrayList<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pago WHERE socio_id = ? ORDER BY fecha_pago DESC";

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pagos.add(new Pago(rs.getInt("id"), rs.getInt("socio_id"), rs.getInt("plan_id"),
                            Actividad.valueOf(rs.getString("actividad")), rs.getDouble("monto"),
                            rs.getDate("fecha_pago").toLocalDate(),
                            rs.getDate("fecha_vencimiento").toLocalDate()));
                }
            }
            return pagos;
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar los pagos del socio: " + e.getMessage());
        }
    }




    //Membresia

    public static Membresia guardarMembresia(Membresia membresia) throws GimnasioException {
        if (membresia == null) throw new DatosInvalidosException("El plan no puede ser nulo.");
        String sql = "INSERT INTO plan (nombre, actividad, duracion_dias, precio) VALUES (?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, membresia.getNombre());
            ps.setString(2, membresia.getActividad().name());
            ps.setInt(3, membresia.getDuracionDias());
            ps.setDouble(4, membresia.getPrecio());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new GimnasioException("No se pudo obtener el ID del plan.");
                membresia.setId(rs.getInt(1));
            }
            return membresia;
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar el plan: " + e.getMessage());
        }
    }

    public static Membresia buscarMembresia(int id) throws GimnasioException {
        if (id <= 0) throw new DatosInvalidosException("ID de plan invalido.");
        String sql = "SELECT * FROM plan WHERE id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new GimnasioException("No existe el plan con ID " + id);
                return leerMembresia(rs);
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar el plan: " + e.getMessage());
        }
    }

    public static ArrayList<Membresia> listarMembresias() throws GimnasioException {
        ArrayList<Membresia> lista = new ArrayList<>();
        String sql = "SELECT * FROM plan ORDER BY actividad, duracion_dias, nombre";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(leerMembresia(rs));
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar los planes: " + e.getMessage());
        }
        return lista;
    }

    private static Membresia leerMembresia(ResultSet rs) throws SQLException {
        Membresia plan = new Membresia(rs.getString("nombre"),
                Actividad.valueOf(rs.getString("actividad")),
                rs.getInt("duracion_dias"), rs.getDouble("precio"));
        plan.setId(rs.getInt("id"));
        return plan;
    }

    //Entrenadores

    public static Entrenador guardarEntrenador(Entrenador entrenador) throws GimnasioException {
        if (entrenador == null) throw new DatosInvalidosException("El entrenador no puede ser nulo.");
        String sql = "INSERT INTO entrenador (nombre, apellido, dni, edad, telefono, email, anios_experiencia, actividades) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entrenador.getNombre());
            ps.setString(2, entrenador.getApellido());
            ps.setString(3, entrenador.getDni());
            ps.setInt(4, entrenador.getEdad());
            ps.setString(5, entrenador.getTelefono());
            ps.setString(6, entrenador.getEmail());
            ps.setInt(7, entrenador.getAniosExperiencia());
            ps.setString(8, actividadesAString(entrenador.getActividades()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new GimnasioException("No se pudo obtener el ID del entrenador.");
                return new Entrenador(rs.getInt(1), entrenador.getNombre(), entrenador.getApellido(),
                        entrenador.getDni(), entrenador.getEdad(), entrenador.getTelefono(),
                        entrenador.getEmail(), entrenador.getAniosExperiencia(), entrenador.getActividades());
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GimnasioException("Ya existe un entrenador con ese DNI.");
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar entrenador: " + e.getMessage());
        }
    }

    public static Entrenador buscarEntrenador(int id) throws GimnasioException {
        if (id <= 0) throw new DatosInvalidosException("ID de entrenador invalido.");
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM entrenador WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new GimnasioException("No existe un entrenador con ID " + id);
                return leerEntrenador(rs);
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar entrenador: " + e.getMessage());
        }
    }

    public static ArrayList<Entrenador> listarEntrenadores() throws GimnasioException {
        ArrayList<Entrenador> lista = new ArrayList<>();
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM entrenador ORDER BY apellido, nombre");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(leerEntrenador(rs));
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar entrenadores: " + e.getMessage());
        }
        return lista;
    }

    private static Entrenador leerEntrenador(ResultSet rs) throws SQLException {
        return new Entrenador(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("dni"), rs.getInt("edad"), rs.getString("telefono"),
                rs.getString("email"), rs.getInt("anios_experiencia"),
                stringAActividades(rs.getString("actividades")));
    }

    private static String actividadesAString(HashSet<Actividad> actividades) {
        String texto = "";

        for (Actividad actividad : actividades) {
            if (!texto.isEmpty()) {
                texto += ",";
            }

            texto += actividad.name();
        }

        return texto;
    }
    private static HashSet<Actividad> stringAActividades(String texto) {
        HashSet<Actividad> actividades = new HashSet<>();

        for (Actividad actividad : Actividad.values()) {
            if (texto.contains(actividad.name())) {
                actividades.add(actividad);
            }
        }

        return actividades;
    }

    // Pagos y estado de membresia

    /** El ultimo vencimiento por actividad define si el socio esta al dia. */
    public static LocalDate ultimoVencimiento(int socioId, Actividad actividad) throws GimnasioException {
        if (socioId <= 0 || actividad == null) throw new DatosInvalidosException("Socio o actividad invalidos.");
        String sql = "SELECT MAX(fecha_vencimiento) AS vencimiento FROM pago WHERE socio_id = ? AND actividad = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            ps.setString(2, actividad.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next() || rs.getDate("vencimiento") == null) return null;
                return rs.getDate("vencimiento").toLocalDate();
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al consultar vencimiento: " + e.getMessage());
        }
    }

    public static EstadoMembresia estadoMembresia(int socioId, Actividad actividad) {
        return EstadoMembresia.calcular(ultimoVencimiento(socioId, actividad));
    }

}