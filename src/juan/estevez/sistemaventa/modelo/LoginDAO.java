package juan.estevez.sistemaventa.modelo;

import java.sql.*;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class LoginDAO {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Busca un usuario en la base de datos.
     *
     * @param correo del usuario que desea iniciar sesión
     * @param password del usuario que desea iniciar sesión
     * @return usuario encontrado
     */
    public Loginn log(String correo, String password) {
        Loginn login = new Loginn();
        String sql = "SELECT * FROM USUARIO WHERE CORREO = ? AND PASSWORD = ?";

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            pst.setString(1, correo);
            pst.setString(2, password);
            rs = pst.executeQuery();

            if (rs.next()) {
                login.setId(rs.getInt("ID"));
                login.setNombre(rs.getString("NOMBRE"));
                login.setCorreo(rs.getString("CORREO"));
                login.setPassword(rs.getString("PASSWORD"));
            }

        } catch (SQLException e) {
            System.err.println(e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en LoginDAO " + e.toString());
            }
        }

        return login;
    }
}
