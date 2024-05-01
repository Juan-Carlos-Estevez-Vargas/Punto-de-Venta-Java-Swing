package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import juan.estevez.sistemaventa.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones relacionadas con Usuarios.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class UsuarioDAO {

    private static final String SELECT_ALL_USUARIOS_SQL = "SELECT * FROM USUARIO";
    private static final String DELETE_USUARIO_SQL = "DELETE FROM USUARIO WHERE ID = ?";
    private static final String UPDATE_USUARIO_SQL = "UPDATE USUARIO SET NOMBRE = ?, CORREO = ?, PASSWORD = ? WHERE ID = ?";

    /**
     * Obtiene una lista de todos los usuarios registrados en la base de datos.
     *
     * @return una lista de objetos Usuario que representan a los usuarios registrados.
     * @throws SQLException si ocurre un error al listar los usuarios.
     */
    public List<Usuario> listarUsuarios() throws SQLException {
        List<Usuario> listaUsuarios = new ArrayList<>();
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_ALL_USUARIOS_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listaUsuarios.add(crearUsuarioDesdeResultSet(rs));
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
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(UPDATE_USUARIO_SQL)) {
            crearPreparedStatementDesdeUsuario(pst, usuario);
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
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(DELETE_USUARIO_SQL)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar usuario", e);
        }
    }

    /**
     * Crea un objeto de tipo Usuario con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo Usuario con los datos de inicio de sesión.
     * @throws SQLException en caso de error con la base de datos.
     */
    private Usuario crearUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("ID"));
        usuario.setNombre(rs.getString("NOMBRE"));
        usuario.setCorreo(rs.getString("CORREO"));
        usuario.setRol(rs.getString("ROL"));
        return usuario;
    }

    /**
     * Setea los datos del usuario al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param usuario objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeUsuario(PreparedStatement pst, Usuario usuario) throws SQLException {
        pst.setString(1, usuario.getNombre());
        pst.setString(2, usuario.getCorreo());
        pst.setString(3, usuario.getPassword());
        pst.setInt(4, usuario.getId());
    }

}
