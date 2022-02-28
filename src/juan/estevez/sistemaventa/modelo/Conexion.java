package juan.estevez.sistemaventa.modelo;

import java.sql.*;

/**
 * Se encarga de crear la conexión a la base de datos.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Conexion {

    public static final String URL = "jdbc:mysql://localhost:3306/punto_venta_java";
    public static final String USER = "root";
    public static final String CLAVE = "";

    /**
     * Conecta con la base de datos.
     *
     * @return conección con la base de datos
     */
    public static Connection conectar() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = (Connection) DriverManager.getConnection(URL, USER, CLAVE);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en la clase conexión: " + e.getMessage());
        }
        return con;
    }
    /* public static Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/punto_venta_java", "root", "");
            return cn;
        } catch (SQLException ex) {
            System.out.println("Error en la conexion local" + ex);
        }
        return (null);
    }*/

}
