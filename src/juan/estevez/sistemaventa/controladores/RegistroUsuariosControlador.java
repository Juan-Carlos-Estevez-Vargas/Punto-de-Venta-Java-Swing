package juan.estevez.sistemaventa.controladores;

import java.sql.SQLException;
import java.util.ResourceBundle;
import juan.estevez.sistemaventa.daos.LoginDAO;
import juan.estevez.sistemaventa.modelo.Loginn;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.vista.Login;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class RegistroUsuariosControlador {

    private static RegistroUsuariosControlador instance;
    private final LoginDAO loginDAO;
    private final ResourceBundle messages;

    public static RegistroUsuariosControlador getInstance() {
        return instance == null ? new RegistroUsuariosControlador() : instance;
    }

    public RegistroUsuariosControlador() {
        this.loginDAO = LoginDAO.getInstance();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    public boolean validar(String correo, String password, String nombre, String rol) {
        if (!correo.isEmpty() && !password.isEmpty() && !nombre.isEmpty() && !rol.isEmpty()) {
            Loginn loginn = Loginn.builder()
                    .nombre(nombre)
                    .correo(correo)
                    .rol(rol)
                    .password(password)
                    .build();

            try {
                loginDAO.registrarUsuario(loginn);
                Utilitarios.mostrarMensajeExito(messages.getString("mensajeRegistroExitoso"));
                openLogin();
                return true;
            } catch (SQLException ex) {
                Utilitarios.mostrarMensajeError(messages.getString("errorConexionDB"));
            }
        } else {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("mensajeRegistroInvalido"));
        }

        return false;
    }

    private void openLogin() {
        new Login().setVisible(true);
    }
}
