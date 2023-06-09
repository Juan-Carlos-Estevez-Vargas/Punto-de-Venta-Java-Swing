package juan.estevez.sistemaventa.daos;

import java.sql.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con el inicio de sesión.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class LoginDAO {

	/**
	 * Realiza el inicio de sesión de un usuario en la aplicación.
	 *
	 * @param correo el correo electrónico del usuario.
	 * @param password la contraseña del usuario.
	 * @return un objeto Loginn que representa al usuario que inició sesión, o null si las credenciales son inválidas.
	 * @throws SQLException si ocurre un error al realizar el inicio de sesión.
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
			throw new SQLException("Error en el inicio de sesión de la aplicación", e);
		}
		return null;
	}

	/**
	 * Registra un nuevo usuario en la base de datos.
	 *
	 * @param login el objeto Loginn que contiene la información del usuario a registrar.
	 * @return true si el usuario se registró correctamente, false en caso contrario.
	 * @throws SQLException si ocurre un error al registrar el usuario.
	 */
	public boolean registrarUsuario(Loginn login) throws SQLException {
		String sql = "INSERT INTO USUARIO (NOMBRE, CORREO, PASSWORD, ROL) VALUES (?,?,?,?)";
		try (Connection conn = Conexion.conectar(); PreparedStatement pst = conn.prepareStatement(sql)) {
			pst.setString(1, login.getNombre());
			pst.setString(2, login.getCorreo());
			pst.setString(3, login.getPassword());
			pst.setString(4, login.getRol());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al registrar el usuario en LoginDAO", e);
		}
	}
}
