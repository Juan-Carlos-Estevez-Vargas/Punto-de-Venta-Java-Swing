package juan.estevez.sistemaventa.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import juan.estevez.sistemaventa.utils.Conexion;
import juan.estevez.sistemaventa.modelo.Usuario;

/**
 * DAO para operaciones relacionadas con Usuarios.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class UsuarioDAO {

    /**
     * Obtiene una lista de todos los usuarios registrados en la base de datos.
     *
     * @return una lista de objetos Usuario que representan a los usuarios
     * registrados.
     * @throws SQLException si ocurre un error al listar los usuarios.
     */
    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setRol(rs.getString("ROL"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar usuarios", e);
        }
        return listaUsuarios;
    }

    /**
     * Modifica los datos de un usuario existente en la base de datos.
     *
     * @param usuario el objeto Usuario con los datos actualizados del usuario.
     * @throws SQLException si ocurre un error al modificar el usuario.
     */
    public void modificarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE USUARIO SET NOMBRE = ?, CORREO = ?, PASSWORD = ? WHERE ID = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, usuario.getNombre());
            pst.setString(2, usuario.getCorreo());
            pst.setString(3, usuario.getPassword());
            pst.setInt(4, usuario.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar usuario", e);
        }
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param id el ID del usuario a eliminar.
     * @throws SQLException si ocurre un error al eliminar el usuario.
     */
    public void eliminarUsuario(int id) throws SQLException {
        String sql = "DELETE FROM USUARIO WHERE ID = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar usuario", e);
        }
    }
}
