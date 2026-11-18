import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
public class TestConnection {

    public static void main(String[] args) {

        try (Connection conexion = DBHandler.obtenerConexion()) {
            System.out.println("Conexion exitosa!");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}
