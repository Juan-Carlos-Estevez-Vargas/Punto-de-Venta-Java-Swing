package juan.estevez.sistemaventa.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import juan.estevez.sistemaventa.modelo.Conexion;
import juan.estevez.sistemaventa.modelo.Usuario;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class UsuarioDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Obtiene los usuario almacenados en la base de datos.
     *
     * @return lista con todos los usuarios obtenidos de la base de datos.
     */
    public List listarUsuarios() {
        List<Usuario> listaUsuarios = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("ID"));
                usuario.setNombre(rs.getString("NOMBRE"));
                usuario.setCorreo(rs.getString("CORREO"));
                usuario.setRol(rs.getString("ROL"));
                listaUsuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios en UsuarioDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en UsuarioDAO " + e.toString());
            }
        }
        return listaUsuarios;
    }
    
    /**
     * Elimina un usuario de la base de datos.
     *
     * @param id por el cual se eliminará el usuario.
     * @return true si se eliminó el usuario y false si no se eliminó.
     */
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM USUARIO WHERE ID = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el eliminar usuario en UsuarioDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en UsuarioDAO " + e.toString());
            }
        }
    }
}
