package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con el inicio de sesi�n.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class LoginDAO {

    /**
     * Realiza el inicio de sesi�n de un usuario en la aplicaci�n.
     *
     * @param correo el correo electr�nico del usuario.
     * @param password la contrase�a del usuario.
     * @return un objeto Loginn que representa al usuario que inici� sesi�n, o
     * null si las credenciales son inv�lidas.
     * @throws SQLException si ocurre un error al realizar el inicio de sesi�n.
     */
    public Loginn login(String correo, String password) throws SQLException {
        String sql = "SELECT * FROM USUARIO WHERE CORREO = ? AND PASSWORD = ?";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, correo);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Loginn login = new Loginn();
                    login.setId(rs.getInt("ID"));
                    login.setNombre(rs.getString("NOMBRE"));
                    login.setCorreo(rs.getString("CORREO"));
                    login.setPassword(rs.getString("PASSWORD"));
                    login.setRol(rs.getString("ROL"));
                    return login;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error en el inicio de sesi�n de la aplicaci�n", e);
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param login el objeto Loginn que contiene la informaci�n del usuario a
     * registrar.
     * @throws SQLException si ocurre un error al registrar el usuario.
     */
    public void registrarUsuario(Loginn login) throws SQLException {
        String sql = "INSERT INTO USUARIO (NOMBRE, CORREO, PASSWORD, ROL) VALUES (?,?,?,?)";
        try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, login.getNombre());
            pst.setString(2, login.getCorreo());
            pst.setString(3, login.getPassword());
            pst.setString(4, login.getRol());
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el usuario en LoginDAO", e);
        }
    }
}
