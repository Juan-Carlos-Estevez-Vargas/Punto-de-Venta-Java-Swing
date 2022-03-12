package juan.estevez.sistemaventa.modelo;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Eventos {

    /**
     * Se encarga de limitar a que se escriba solo letras cuando se llame al
     * evento.
     *
     * @param evt al cuál se le aplicará el filtro.
     */
    public void textKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if ((car < 'a' || car > 'z') && (car < 'A' || car > 'Z')
                && (car != (char) KeyEvent.VK_BACK_SPACE) && (car != (char) KeyEvent.VK_SPACE)) {
            evt.consume();
        }
    }

    /**
     * Se encarga de permitir solo la escritura de números enteros en un
     * componente.
     *
     * @param evt al cuál se le aplicará el filtro.
     */
    public void numberKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }

    /**
     * Se encarga de permitir solo la escritura de números enteros y decimales
     * en un componente.
     *
     * @param evt al cuál se le aplicará el filtro.
     * @param textField al cuál se le colocará el evento.
     */
    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
        char car = evt.getKeyChar();
        if ((car < '0' || car > '9') && textField.getText().contains(".") && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        } else if ((car < '0' || car > '9') && (car != '.') && (car != (char) KeyEvent.VK_BACK_SPACE)) {
            evt.consume();
        }
    }

}
