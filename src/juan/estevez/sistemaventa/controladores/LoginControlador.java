package juan.estevez.sistemaventa.controladores;

import java.sql.SQLException;
import java.util.ResourceBundle;
import juan.estevez.sistemaventa.daos.LoginDAO;
import juan.estevez.sistemaventa.modelo.Loginn;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.vista.Sistema;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class LoginControlador {

    private final LoginDAO loginDAO;
    private final ResourceBundle messages;

    public LoginControlador() {
        this.loginDAO = LoginDAO.getInstance();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    public void validar(String correo, String password) {
        if (!correo.isEmpty() && !password.isEmpty()) {
            try {
                Loginn loginn = loginDAO.login(correo, password);
                if (loginn.getCorreo() != null && loginn.getPassword() != null) {
                    iniciarSistema(loginn);
                } else {
                    Utilitarios.mostrarMensajeAdvertencia(messages.getString("credencialesErroneas"));
                }
            } catch (SQLException ex) {
                Utilitarios.mostrarMensajeError(messages.getString("errorConexionDB"));
            }
        } else {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("mensajeRegistroInvalido"));
        }
    }

    private void iniciarSistema(Loginn loginn) throws SQLException {
        Sistema sistema = new Sistema(loginn);
        sistema.setVisible(true);
    }

}
