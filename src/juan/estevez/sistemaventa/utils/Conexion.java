package juan.estevez.sistemaventa.utils;

import java.sql.*;
import lombok.NoArgsConstructor;

/**
 * Se encarga de crear la conexión a la base de datos.
 *
 * @author Juan Carlos Estevez Vargas
 */
@NoArgsConstructor
public class Conexion {

    private static Conexion instance;
    private Connection connection;
    public static final String URL = "jdbc:mysql://localhost:3306/punto_venta_java";
    public static final String USER = "root";
    public static final String CLAVE = "";

    /**
     * Conecta con la base de datos.
     *
     * @return conección con la base de datos
     */
    public Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, CLAVE);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error en la clase conexión: " + e.getMessage());
        }
        return connection;
    }
    
    public static Conexion getInstance() {
        if (instance == null) {
            synchronized (Conexion.class) {
                if (instance == null) {
                    instance = new Conexion();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

}
