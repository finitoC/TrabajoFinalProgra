import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CargarDatosPrueba {

    public static void main(String[] args) {
        String url = "jdbc:sqlite:fightfit.db";

        try (Connection conn = DriverManager.getConnection(url);
             Statement st = conn.createStatement()) {

            st.execute("PRAGMA foreign_keys = ON");

            // Socios
            st.executeUpdate("""
                INSERT OR IGNORE INTO socio (nombre,apellido,dni,edad,telefono,email,fecha_alta) VALUES
                ('Lucas','Ferreyra','40111222',24,'2234567801','lucas@gmail.com',date('now','-90 days')),
                ('Martina','Silva','42555666',22,'2234567802','martina@gmail.com',date('now','-60 days')),
                ('Diego','Acosta','38777888',28,'2234567803','diego@gmail.com',date('now','-120 days')),
                ('Sofia','Ruiz','43999111',20,'2234567804','sofia@gmail.com',date('now','-30 days'))
                """);

            // Plan de boxeo
            st.executeUpdate("""
                INSERT INTO plan(nombre,actividad,duracion_dias,precio)
                SELECT 'Boxeo mensual','BOXEO',30,32000
                WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre='Boxeo mensual')
                """);

            // Plan de gimnasio
            st.executeUpdate("""
                INSERT INTO plan(nombre,actividad,duracion_dias,precio)
                SELECT 'Gimnasio mensual','GIMNASIO',30,28000
                WHERE NOT EXISTS (SELECT 1 FROM plan WHERE nombre='Gimnasio mensual')
                """);

            // Pagos de boxeo
            st.executeUpdate("""
                INSERT INTO pago(socio_id,plan_id,actividad,monto,fecha_pago,fecha_vencimiento)
                SELECT s.id,p.id,'BOXEO',p.precio,date('now'),date('now','+30 days')
                FROM socio s JOIN plan p ON p.nombre='Boxeo mensual'
                WHERE s.dni IN ('40111222','42555666','38777888')
                AND NOT EXISTS (SELECT 1 FROM pago x WHERE x.socio_id=s.id AND x.plan_id=p.id)
                """);

            // Pago de gimnasio
            st.executeUpdate("""
                INSERT INTO pago(socio_id,plan_id,actividad,monto,fecha_pago,fecha_vencimiento)
                SELECT s.id,p.id,'GIMNASIO',p.precio,date('now'),date('now','+30 days')
                FROM socio s JOIN plan p ON p.nombre='Gimnasio mensual'
                WHERE s.dni='43999111'
                AND NOT EXISTS (SELECT 1 FROM pago x WHERE x.socio_id=s.id AND x.plan_id=p.id)
                """);

            // Competidor Lucas
            st.executeUpdate("""
                INSERT OR IGNORE INTO competidor(socio_id,peso_kg,victorias,derrotas,empates,activo)
                SELECT id,68.5,1,0,0,1 FROM socio WHERE dni='40111222' 
                """);

            // Competidora Martina
            st.executeUpdate("""
                INSERT OR IGNORE INTO competidor(socio_id,peso_kg,victorias,derrotas,empates,activo)
                SELECT id,59.2,0,0,0,1 FROM socio WHERE dni='42555666' 
                """);

            // Competidor Diego
            st.executeUpdate("""
                INSERT OR IGNORE INTO competidor(socio_id,peso_kg,victorias,derrotas,empates,activo)
                SELECT id,74.0,0,0,0,1 FROM socio WHERE dni='38777888' 
                """);

            // Asistencias de boxeo
            st.executeUpdate("""
                INSERT INTO asistencia(socio_id,actividad,fecha_hora)
                SELECT id,'BOXEO',datetime('now','localtime') FROM socio s
                WHERE dni IN ('40111222','42555666','38777888')
                AND NOT EXISTS (SELECT 1 FROM asistencia a WHERE a.socio_id=s.id AND a.actividad='BOXEO')
                """);

            // Asistencia de gimnasio
            st.executeUpdate("""
                INSERT INTO asistencia(socio_id,actividad,fecha_hora)
                SELECT id,'GIMNASIO',datetime('now','localtime') FROM socio s
                WHERE dni='43999111'
                AND NOT EXISTS (SELECT 1 FROM asistencia a WHERE a.socio_id=s.id AND a.actividad='GIMNASIO')
                """);

            // Pelea finalizada
            st.executeUpdate("""
                INSERT INTO pelea(competidor_id,rival,fecha,lugar,resultado)
                SELECT c.id,'Bruno Rojas',date('now','-10 days'),'Polideportivo','GANADA'
                FROM competidor c JOIN socio s ON s.id=c.socio_id WHERE s.dni='40111222'
                AND NOT EXISTS (SELECT 1 FROM pelea p WHERE p.competidor_id=c.id AND p.rival='Bruno Rojas')
                """);

            // Pelea pendiente de Diego
            st.executeUpdate("""
                INSERT INTO pelea(competidor_id,rival,fecha,lugar,resultado)
                SELECT c.id,'Nicolas Vera',date('now','+12 days'),'Club Atletico','PENDIENTE'
                FROM competidor c JOIN socio s ON s.id=c.socio_id WHERE s.dni='38777888'
                AND NOT EXISTS (SELECT 1 FROM pelea p WHERE p.competidor_id=c.id AND p.rival='Nicolas Vera')
                """);

            // Pelea pendiente de Martina
            st.executeUpdate("""
                INSERT INTO pelea(competidor_id,rival,fecha,lugar,resultado)
                SELECT c.id,'Camila Paz',date('now','+20 days'),'Gimnasio Central','PENDIENTE'
                FROM competidor c JOIN socio s ON s.id=c.socio_id WHERE s.dni='42555666'
                AND NOT EXISTS (SELECT 1 FROM pelea p WHERE p.competidor_id=c.id AND p.rival='Camila Paz')
                """);

            System.out.println("Datos de prueba cargados correctamente!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
