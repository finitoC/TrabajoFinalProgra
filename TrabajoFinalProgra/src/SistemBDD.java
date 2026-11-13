import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class SistemBDD {


    private static final String URL =
            "jdbc:mysql://localhost:3306/";


    /**
     * Abre una conexión nueva. Quien la pida es responsable de cerrarla (try-with-resources).
     */
    private static Connection conectar() throws GimnasioException {
        try {
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (SQLException e) {
            throw new GimnasioException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }

    // ================================================================ SOCIO

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
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new GimnasioException("Ya existe un socio con el DNI " + socio.getDni() + ".", e);
        } catch (SQLException e) {
            throw new GimnasioException("Error al guardar el socio: " + e.getMessage(), e);
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
            throw new GimnasioException("Error al buscar el socio: " + e.getMessage(), e);
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



}