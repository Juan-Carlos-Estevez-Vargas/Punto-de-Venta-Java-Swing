package juan.estevez.sistemaventa.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import juan.estevez.sistemaventa.modelo.Cliente;
import juan.estevez.sistemaventa.modelo.Conexion;
import juan.estevez.sistemaventa.modelo.Usuario;

/**
 *
 * @author User
 */
public class UsuarioDAO {
    
     Connection cn;
    PreparedStatement pst;
    ResultSet rs;
    /**
     * Obtiene los clientes almacenados en la base de datos.
     *
     * @return lista con todos los clientes obtenidos de la base de datos.
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
}
