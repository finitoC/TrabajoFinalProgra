import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;

public class SistemBDD {

    /**
     * Abre una conexión nueva. Quien la pida es responsable de cerrarla (try-with-resources).
     */
    private static final String URL = "jdbc:sqlite:fightfit.db";
    private static Connection conectar() throws GimnasioException {

        try {
            Connection con = DriverManager.getConnection(URL);

            try (Statement st = con.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }

            return con;

        } catch (SQLException e) {
            throw new GimnasioException(
                    "Error al conectar a SQLite: " + e.getMessage()
            );
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
            ps.setString(7, socio.getFechaAlta().toString());

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
    public static Socio buscarSocioPorDni(String dni) throws GimnasioException {

        Validador.validarDni(dni);

        String sql = "SELECT * FROM socio WHERE dni = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Socio(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("dni"),
                            rs.getInt("edad"),
                            rs.getString("telefono"),
                            rs.getString("email"),
                            LocalDate.parse(rs.getString("fecha_alta"))
                    );
                } else {
                    throw new GimnasioException("No existe un socio con ese DNI");
                }
            }

        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar socio: " + e.getMessage());
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
            ps.setString(5, pago.getFechaPago().toString());
            ps.setString(6, pago.getFechaVencimiento().toString());
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


    public static ArrayList<Pago> listarPagosDeSocio(int socioId)
            throws GimnasioException {

        ArrayList<Pago> pagos = new ArrayList<>();

        String sql = "SELECT * FROM pago WHERE socio_id = ? ORDER BY fecha_pago DESC";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, socioId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Pago pago = new Pago(
                            rs.getInt("id"),
                            rs.getInt("socio_id"),
                            rs.getInt("plan_id"),
                            Actividad.valueOf(rs.getString("actividad")),
                            rs.getDouble("monto"),
                            LocalDate.parse(rs.getString("fecha_pago")),
                            LocalDate.parse(rs.getString("fecha_vencimiento"))
                    );

                    pagos.add(pago);
                }
            }

        } catch (SQLException e) {
            throw new GimnasioException(
                    "Error al listar los pagos del socio: " + e.getMessage()
            );
        }

        return pagos;
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

    public static Entrenador buscarEntrenadorPorDni(String dni) throws GimnasioException {

        Validador.validarDni(dni);

        String sql = "SELECT * FROM entrenador WHERE dni = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return leerEntrenador(rs);
                } else {
                    throw new GimnasioException("No existe un entrenador con ese DNI.");
                }
            }

        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar entrenador: " + e.getMessage());
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

    public static LocalDate ultimoVencimiento(int socioId, Actividad actividad)
            throws GimnasioException {

        String sql = "SELECT MAX(fecha_vencimiento) AS vencimiento "
                + "FROM pago WHERE socio_id = ? AND actividad = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, socioId);
            ps.setString(2, actividad.name());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    String fecha = rs.getString("vencimiento");

                    if (fecha != null) {
                        return LocalDate.parse(fecha);
                    }
                }
            }

        } catch (SQLException e) {
            throw new GimnasioException(
                    "Error al consultar vencimiento: " + e.getMessage()
            );
        }

        return null;
    }


    public static EstadoMembresia estadoMembresia(int socioId, Actividad actividad) {
        return EstadoMembresia.calcular(ultimoVencimiento(socioId, actividad));
    }

    public static void guardarAsistencia(Asistencia asistencia) throws GimnasioException {
        if (asistencia == null) throw new DatosInvalidosException("La asistencia no puede ser nula.");
        if (estadoMembresia(asistencia.getSocioId(), asistencia.getActividad()) != EstadoMembresia.ACTIVO) {
            throw new GimnasioException("El socio no tiene una membresia activa para " + asistencia.getActividad());
        }
        String sql = "INSERT INTO asistencia (socio_id, actividad, fecha_hora) VALUES (?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, asistencia.getSocioId());
            ps.setString(2, asistencia.getActividad().name());
            ps.setString(3, asistencia.getFechaHora().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) asistencia.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al registrar asistencia: " + e.getMessage());
        }
    }

    public static ArrayList<Asistencia> listarAsistenciasDeSocio(int socioId) throws GimnasioException {
        if (socioId <= 0) throw new DatosInvalidosException("ID de socio invalido.");
        ArrayList<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia WHERE socio_id = ? ORDER BY datetime(fecha_hora) DESC";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(new Asistencia(rs.getInt("id"), rs.getInt("socio_id"),
                        Actividad.valueOf(rs.getString("actividad")),
                        LocalDateTime.parse(rs.getString("fecha_hora").replace(' ', 'T'))));
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar asistencias: " + e.getMessage());
        }
        return lista;
    }

    public static ArrayList<ClaseGrupal> listarClases() throws GimnasioException {

        ArrayList<ClaseGrupal> lista = new ArrayList<>();

        String sql = "SELECT * FROM clase ORDER BY actividad, dia, hora";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClaseGrupal clase = new ClaseGrupal(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        Actividad.valueOf(rs.getString("actividad")),
                        DiaSemana.valueOf(rs.getString("dia")),
                        LocalTime.parse(rs.getString("hora")),
                        rs.getInt("cupo"),
                        rs.getInt("entrenador_id")
                );

                cargarInscriptos(con, clase);
                lista.add(clase);
            }

        } catch (SQLException e) {
            throw new GimnasioException("Error al listar clases: " + e.getMessage());

        }

        return lista;
    }


    // ================================================================ CLASES GRUPALES

    public static ClaseGrupal guardarClase(ClaseGrupal clase) throws GimnasioException {
        if (clase == null) throw new DatosInvalidosException("La clase no puede ser nula.");
        if (clase.getEntrenadorId() > 0 && !buscarEntrenador(clase.getEntrenadorId()).entrena(clase.getActividad())) {
            throw new GimnasioException("El entrenador no dicta la actividad de esta clase.");
        }
        String sql = "INSERT INTO clase (nombre, actividad, dia, hora, cupo, entrenador_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clase.getNombre());
            ps.setString(2, clase.getActividad().name());
            ps.setString(3, clase.getDia().name());
            ps.setString(4, clase.getHora().toString());
            ps.setInt(5, clase.getCupo());
            if (clase.getEntrenadorId() == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, clase.getEntrenadorId());
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next()) throw new GimnasioException("No se obtuvo el ID de la clase.");
                clase.setId(rs.getInt(1));
            }
            return clase;
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar clase: " + e.getMessage());
        }
    }

    public static ClaseGrupal buscarClase(int id) throws GimnasioException {
        if (id <= 0) throw new DatosInvalidosException("ID de clase invalido.");
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM clase WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new GimnasioException("No existe una clase con ID " + id);
                ClaseGrupal clase = leerClase(rs);
                cargarInscriptos(con, clase);
                return clase;
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar clase: " + e.getMessage());
        }
    }

    private static ClaseGrupal leerClase(ResultSet rs) throws SQLException {
        return new ClaseGrupal(rs.getInt("id"), rs.getString("nombre"),
                Actividad.valueOf(rs.getString("actividad")), DiaSemana.valueOf(rs.getString("dia")),
                LocalTime.parse(rs.getString("hora")), rs.getInt("cupo"), rs.getInt("entrenador_id"));
    }

    private static void cargarInscriptos(Connection con, ClaseGrupal clase) throws SQLException {
        java.util.HashSet<Integer> ids = new java.util.HashSet<>();
        try (PreparedStatement ps = con.prepareStatement("SELECT socio_id FROM clase_socio WHERE clase_id = ?")) {
            ps.setInt(1, clase.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt("socio_id"));
            }
        }
        clase.cargarInscriptos(ids);
    }

    public static void inscribirSocioEnClase(int claseId, int socioId) throws GimnasioException {
        buscarSocio(socioId);
        ClaseGrupal clase = buscarClase(claseId);
        if (estadoMembresia(socioId, clase.getActividad()) != EstadoMembresia.ACTIVO) {
            throw new GimnasioException("El socio necesita una membresia activa para inscribirse.");
        }
        clase.inscribir(socioId); // Reutilizamos la validacion de duplicados y cupo de su clase.
        String sql = "INSERT INTO clase_socio (clase_id, socio_id) VALUES (?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            ps.setInt(2, socioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GimnasioException("Error al inscribir socio: " + e.getMessage());
        }
    }


    public static ArrayList<Socio> listarInscriptosDeClase(int claseId) throws GimnasioException {
        buscarClase(claseId);
        ArrayList<Socio> socios = new ArrayList<>();
        String sql = "SELECT s.* FROM socio s JOIN clase_socio cs ON cs.socio_id = s.id WHERE cs.clase_id = ? ORDER BY s.apellido, s.nombre";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, claseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) socios.add(leerSocio(rs));
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar inscriptos: " + e.getMessage());
        }
        return socios;
    }

    private static Socio leerSocio(ResultSet rs) throws SQLException {
        return new Socio(rs.getInt("id"), rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("dni"), rs.getInt("edad"), rs.getString("telefono"),
                rs.getString("email"), LocalDate.parse(rs.getString("fecha_alta")));
    }

    // COMPETIDORES

    /** El ID del competidor para Java es el ID del socio del cual hereda. */
    public static void guardarCompetidor(Competidor competidor) throws GimnasioException {
        if (competidor == null) throw new DatosInvalidosException("El competidor no puede ser nulo.");
        buscarSocio(competidor.getId());
        String sql = "INSERT INTO competidor (socio_id, peso_kg, victorias, derrotas, empates, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, competidor.getId());
            ps.setDouble(2, competidor.getPesoKg());
            ps.setInt(3, competidor.getVictorias());
            ps.setInt(4, competidor.getDerrotas());
            ps.setInt(5, competidor.getEmpates());
            ps.setBoolean(6, competidor.isActivo());
            ps.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GimnasioException("Ese socio ya esta registrado como competidor.");
        } catch (SQLException e) {
            throw new GimnasioException("Error al registrar competidor: " + e.getMessage());
        }
    }


    public static Competidor buscarCompetidor(int socioId) throws GimnasioException {
        if (socioId <= 0) throw new DatosInvalidosException("ID de socio invalido.");
        String sql = "SELECT s.*, c.peso_kg, c.victorias, c.derrotas, c.empates, c.activo FROM competidor c JOIN socio s ON s.id = c.socio_id WHERE c.socio_id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new GimnasioException("El socio " + socioId + " no es competidor.");
                return leerCompetidor(rs);
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al buscar competidor: " + e.getMessage());
        }
    }

    private static Competidor leerCompetidor(ResultSet rs) throws SQLException {
        return new Competidor(leerSocio(rs), rs.getDouble("peso_kg"), rs.getInt("victorias"),
                rs.getInt("derrotas"), rs.getInt("empates"), rs.getBoolean("activo"));
    }

    public static ArrayList<Competidor> listarCompetidores() throws GimnasioException {
        ArrayList<Competidor> lista = new ArrayList<>();
        String sql = "SELECT s.*, c.peso_kg, c.victorias, c.derrotas, c.empates, c.activo FROM competidor c JOIN socio s ON s.id = c.socio_id ORDER BY s.apellido, s.nombre";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(leerCompetidor(rs));
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar competidores: " + e.getMessage());
        }
        return lista;
    }

    public static void actualizarPesoCompetidor(int socioId, double pesoKg) throws GimnasioException {
        Competidor competidor = buscarCompetidor(socioId);
        competidor.setPesoKg(pesoKg);
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement("UPDATE competidor SET peso_kg = ? WHERE socio_id = ?")) {
            ps.setDouble(1, competidor.getPesoKg());
            ps.setInt(2, socioId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new GimnasioException("Error al actualizar peso: " + e.getMessage());
        }
    }

    // ================================================================ PELEAS


    private static int buscarIdFilaCompetidor(int socioId) {
        String sql = "SELECT id FROM competidor WHERE socio_id = ?";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new GimnasioException("El socio no es competidor.");
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al obtener ID de competidor: " + e.getMessage());
        }
    }

    public static Pelea guardarPelea(Pelea pelea) throws GimnasioException {
        if (pelea == null) throw new DatosInvalidosException("La pelea no puede ser nula.");
        Competidor competidor = buscarCompetidor(pelea.getCompetidorId());
        if (!competidor.isActivo()) throw new GimnasioException("El competidor esta inactivo.");
        if (pelea.getResultado() != ResultadoPelea.PENDIENTE) {
            throw new GimnasioException("Primero debe registrar la pelea como PENDIENTE.");
        }
        String sql = "INSERT INTO pelea (competidor_id, rival, fecha, lugar, resultado) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // En Java el competidor se identifica con su ID de socio;
            // la tabla pelea guarda la PK interna de competidor.
            ps.setInt(1, buscarIdFilaCompetidor(pelea.getCompetidorId()));
            ps.setString(2, pelea.getRival());
            ps.setString(3, pelea.getFecha().toString());
            ps.setString(4, pelea.getLugar());
            ps.setString(5, pelea.getResultado().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) pelea.setId(rs.getInt(1));
            }
            return pelea;
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar pelea: " + e.getMessage());
        }
    }

    public static ArrayList<Pelea> listarPeleasDeCompetidor(int socioId) throws GimnasioException {
        if (socioId <= 0) throw new DatosInvalidosException("ID de socio invalido.");
        ArrayList<Pelea> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.socio_id AS socio_competidor_id FROM pelea p " +
                "JOIN competidor c ON c.id = p.competidor_id WHERE c.socio_id = ? ORDER BY p.fecha DESC";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, socioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(leerPelea(rs));
            }
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar peleas: " + e.getMessage());
        }
        return lista;
    }

    private static Pelea leerPelea(ResultSet rs) throws SQLException {
        return new Pelea(rs.getInt("id"), rs.getInt("socio_competidor_id"), rs.getString("rival"),
                LocalDate.parse(rs.getString("fecha")), rs.getString("lugar"),
                ResultadoPelea.valueOf(rs.getString("resultado")));
    }

    public static ArrayList<Pelea> listarPeleas() throws GimnasioException {
        ArrayList<Pelea> lista = new ArrayList<>();
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(
                "SELECT p.*, c.socio_id AS socio_competidor_id FROM pelea p " +
                        "JOIN competidor c ON c.id = p.competidor_id ORDER BY p.fecha DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(leerPelea(rs));
        } catch (SQLException e) {
            throw new GimnasioException("Error al listar peleas: " + e.getMessage());
        }
        return lista;
    }

    /** Actualiza resultado y record del competidor */
    public static void registrarResultadoPelea(int peleaId, ResultadoPelea resultado) throws GimnasioException {
        if (peleaId <= 0 || resultado == null || resultado == ResultadoPelea.PENDIENTE) {
            throw new DatosInvalidosException("ID o resultado de pelea invalido.");
        }

        String sqlBuscar = "SELECT competidor_id, resultado FROM pelea WHERE id = ?";
        String sqlActualizarCompetidor = "UPDATE competidor SET victorias = victorias + ?, derrotas = derrotas + ?, empates = empates + ? WHERE id = ?";
        String sqlActualizarPelea = "UPDATE pelea SET resultado = ? WHERE id = ?";

        try (Connection con = conectar();
             PreparedStatement psBuscar = con.prepareStatement(sqlBuscar)) {

            psBuscar.setInt(1, peleaId);

            int competidorId;

            try (ResultSet rs = psBuscar.executeQuery()) {
                if (!rs.next()) {
                    throw new GimnasioException("No existe esa pelea.");
                }

                if (!"PENDIENTE".equals(rs.getString("resultado"))) {
                    throw new GimnasioException("Esta pelea ya tiene resultado.");
                }

                competidorId = rs.getInt("competidor_id");
            }

            try (PreparedStatement ps = con.prepareStatement(sqlActualizarCompetidor)) {
                ps.setInt(1, resultado == ResultadoPelea.GANADA ? 1 : 0);
                ps.setInt(2, resultado == ResultadoPelea.PERDIDA ? 1 : 0);
                ps.setInt(3, resultado == ResultadoPelea.EMPATE ? 1 : 0);
                ps.setInt(4, competidorId);

                if (ps.executeUpdate() == 0) {
                    throw new GimnasioException("El competidor no existe.");
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlActualizarPelea)) {
                ps.setString(1, resultado.name());
                ps.setInt(2, peleaId);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new GimnasioException("Error al registrar resultado: " + e.getMessage());
        }
    }



}