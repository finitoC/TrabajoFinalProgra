
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CrearBaseDeDatos {

    public static void main(String[] args) {

        String url = "jdbc:sqlite:fightfit.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement()) {

            st.execute("PRAGMA foreign_keys = ON");

            // TABLA SOCIO
            st.execute("""
                CREATE TABLE IF NOT EXISTS socio (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    dni VARCHAR(20) NOT NULL UNIQUE,
                    edad INT NOT NULL,
                    telefono VARCHAR(25),
                    email VARCHAR(100),
                    fecha_alta TEXT NOT NULL
                )
            """);

            // TABLA ENTRENADOR
            st.execute("""
                CREATE TABLE IF NOT EXISTS entrenador (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(50) NOT NULL,
                    apellido VARCHAR(50) NOT NULL,
                    dni VARCHAR(20) NOT NULL UNIQUE,
                    edad INT NOT NULL,
                    telefono VARCHAR(25),
                    email VARCHAR(100),
                    anios_experiencia INT NOT NULL,
                    actividades VARCHAR(100) NOT NULL
                )
            """);

            // TABLA PLAN
            st.execute("""
                CREATE TABLE IF NOT EXISTS plan (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(40) NOT NULL,
                    actividad VARCHAR(20) NOT NULL,
                    duracion_dias INT NOT NULL,
                    precio REAL NOT NULL
                )
            """);

            // TABLA PAGO
            st.execute("""
                CREATE TABLE IF NOT EXISTS pago (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    socio_id INT NOT NULL,
                    plan_id INT NOT NULL,
                    actividad VARCHAR(20) NOT NULL,
                    monto REAL NOT NULL,
                    fecha_pago TEXT NOT NULL,
                    fecha_vencimiento TEXT NOT NULL,
                    FOREIGN KEY (socio_id) REFERENCES socio(id),
                    FOREIGN KEY (plan_id) REFERENCES plan(id)
                )
            """);

            // TABLA ASISTENCIA
            st.execute("""
                CREATE TABLE IF NOT EXISTS asistencia (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    socio_id INT NOT NULL,
                    actividad VARCHAR(20) NOT NULL,
                    fecha_hora TEXT NOT NULL,
                    FOREIGN KEY (socio_id) REFERENCES socio(id)
                )
            """);

            // TABLA CLASE GRUPAL
            st.execute("""
                CREATE TABLE IF NOT EXISTS clase (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre VARCHAR(80) NOT NULL,
                    actividad VARCHAR(20) NOT NULL,
                    dia VARCHAR(20) NOT NULL,
                    hora TEXT NOT NULL,
                    cupo INT NOT NULL,
                    entrenador_id INT NOT NULL,
                    FOREIGN KEY (entrenador_id) REFERENCES entrenador(id)
                )
            """);

            // TABLA INSCRIPCIONES
            st.execute("""
                CREATE TABLE IF NOT EXISTS clase_socio (
                    clase_id INT NOT NULL,
                    socio_id INT NOT NULL,
                    PRIMARY KEY (clase_id, socio_id),
                    FOREIGN KEY (clase_id) REFERENCES clase(id),
                    FOREIGN KEY (socio_id) REFERENCES socio(id)
                )
            """);

            // TABLA COMPETIDOR
            st.execute("""
                CREATE TABLE IF NOT EXISTS competidor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    socio_id INT NOT NULL UNIQUE,
                    peso_kg REAL NOT NULL,
                    victorias INT NOT NULL DEFAULT 0,
                    derrotas INT NOT NULL DEFAULT 0,
                    empates INT NOT NULL DEFAULT 0,
                    activo INTEGER NOT NULL DEFAULT 1,
                    FOREIGN KEY (socio_id) REFERENCES socio(id)
                )
            """);

            // TABLA PELEA
            st.execute("""
                CREATE TABLE IF NOT EXISTS pelea (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    competidor_id INT NOT NULL,
                    rival VARCHAR(100) NOT NULL,
                    fecha TEXT NOT NULL,
                    lugar VARCHAR(100) NOT NULL,
                    resultado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
                    FOREIGN KEY (competidor_id) REFERENCES competidor(id)
                )
            """);

            System.out.println("Base de datos FightFit creada correctamente!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

