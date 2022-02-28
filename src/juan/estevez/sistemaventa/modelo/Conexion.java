package juan.estevez.sistemaventa.modelo;

import java.sql.*;

/**
 * Se encarga de crear la conexión a la base de datos.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Conexion {

    /**
     * Conecta con la base de datos.
     *
     * @return conección con la base de datos
     */
    public static Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/punto_venta_java?useSSL=false&useTimezone=true&serverTimezone=UTC&allowPublicKeyRetrieval=true");
            return cn;
        } catch (SQLException ex) {
            System.out.println("Error en la conexion local" + ex.getMessage());
        }
        return (null);
    }

}
