package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import java.util.List;
import juan.estevez.sistemaventa.daos.UsuarioDAO;
import juan.estevez.sistemaventa.modelo.Usuario;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class UsuarioServicio {

    private static UsuarioServicio instance;
    private final UsuarioDAO usuarioDAO;

    public static UsuarioServicio getInstance() {
        return instance == null ? new UsuarioServicio() : instance;
    }

    private UsuarioServicio() {
        this.usuarioDAO = UsuarioDAO.getInstance();
    }

    public List<Usuario> getAllUsuarios() throws SQLException{
        return this.usuarioDAO.listarUsuarios();
    }
    
    public void eliminarUsuario(int idUsuario) throws SQLException {
        this.usuarioDAO.eliminarUsuario(idUsuario);
    }
    
    public void modificarUsuario(Usuario usuario) throws SQLException {
        this.usuarioDAO.modificarUsuario(usuario);
    }
    
}
