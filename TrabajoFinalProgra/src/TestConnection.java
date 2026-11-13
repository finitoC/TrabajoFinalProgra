import java.sql.*;
public class TestConnection {


        public static void main(String[] args) {

            try (Connection con = DBHandler.BDHandler.obtenerConexion()) {

                System.out.println("Conexion exitosa!");

                String sql = "SELECT COUNT(*) FROM socio";

                try (PreparedStatement ps = con.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        System.out.println(
                                "Socios registrados: " + rs.getInt(1)
                        );
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

}
