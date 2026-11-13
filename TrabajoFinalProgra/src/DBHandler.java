import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBHandler {


    public class BDHandler {

        private static final String URL = "jdbc:mysql://tupalumni:3306/fightfit";

        private static final String USUARIO = "bdd1";
        private static final String PASSWORD = "bdd1";

        public static Connection obtenerConexion()
                throws SQLException {

            return DriverManager.getConnection(
                    URL, USUARIO, PASSWORD);
        }
    }
}
