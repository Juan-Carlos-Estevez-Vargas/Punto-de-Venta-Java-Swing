package juan.estevez.sistemaventa.controladores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.modelo.Usuario;
import juan.estevez.sistemaventa.servicio.UsuarioServicio;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.utils.vista.GUIUtils;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class UsuarioControlador {
    
    private static UsuarioControlador instance;
    private final UsuarioServicio usuarioServicio;
    private final ResourceBundle messages;
    private List<Usuario> usuarios;

    public static UsuarioControlador getInstance() {
        if (instance == null) {
            synchronized (UsuarioControlador.class) {
                if (instance == null) {
                    instance = new UsuarioControlador();
                }
            }
        }
        return instance;
    }

    public UsuarioControlador() {
        this.usuarioServicio = UsuarioServicio.getInstance();
        this.usuarios = listarUsuarios();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    private List<Usuario> listarUsuarios() {
        try {
            this.usuarios = this.usuarioServicio.getAllUsuarios();
            return this.usuarios;
        } catch (SQLException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("error"));
        }

        return new ArrayList<>();
    }

    public void listarUsuarios(JTable tableProductos) {
        listarUsuarios();
        tableProductos.setModel(GUIUtils.listarUsuarios(usuarios, (DefaultTableModel) tableProductos.getModel()));
    }
    
    public void eliminarUsuario(int idUsuario) {
        try {
            this.usuarioServicio.eliminarUsuario(idUsuario);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }
    
    public void editarUsuario(Usuario usuario) {
        try {
            this.usuarioServicio.modificarUsuario(usuario);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }
    
}
