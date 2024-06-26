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

    private static LoginControlador instance;
    private final LoginDAO loginDAO;
    private final ResourceBundle messages;

    public static LoginControlador getInstance() {
        if (instance == null) {
            synchronized (LoginControlador.class) {
                if (instance == null) {
                    instance = new LoginControlador();
                }
            }
        }
        return instance;
    }
    
    public LoginControlador() {
        this.loginDAO = LoginDAO.getInstance();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    public boolean validar(String correo, String password) {
        if (!correo.isEmpty() && !password.isEmpty()) {
            try {
                Loginn loginn = loginDAO.login(correo, password);
                if (loginn.getCorreo() != null && loginn.getPassword() != null) {
                    iniciarSistema(loginn);
                    return true;
                } else {
                    Utilitarios.mostrarMensajeAdvertencia(messages.getString("credencialesErroneas"));
                }
            } catch (SQLException ex) {
                Utilitarios.mostrarMensajeError(messages.getString("errorConexionDB"));
            }
        } else {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("mensajeRegistroInvalido"));
        }
        
        return false;
    }

    private void iniciarSistema(Loginn loginn) throws SQLException {
        Sistema sistema = new Sistema(loginn);
        sistema.setVisible(true);
    }

}
