package juan.estevez.sistemaventa.utils;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Eventos {

    /**
     * Filtra el evento para permitir solo letras.
     *
     * @param evt el evento al que se le aplicar� el filtro.
     */
    public void textKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if (!Character.isLetter(car) && car != KeyEvent.VK_BACK_SPACE && car != KeyEvent.VK_SPACE) {
            evt.consume();
        }
    }

    /**
     * Filtra el evento para permitir solo n�meros enteros.
     *
     * @param evt el evento al que se le aplicar� el filtro.
     */
    public void numberKeyPress(KeyEvent evt) {
        char car = evt.getKeyChar();
        if (!Character.isDigit(car) && car != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }

    /**
     * Filtra el evento para permitir solo n�meros enteros y decimales en un componente JTextField.
     *
     * @param evt el evento al que se le aplicar� el filtro.
     * @param textField el componente JTextField al que se le asignar� el evento.
     */
    public void numberDecimalKeyPress(KeyEvent evt, JTextField textField) {
        char car = evt.getKeyChar();
        String text = textField.getText();
        if ((!Character.isDigit(car) && car != '.' && car != KeyEvent.VK_BACK_SPACE)
                || (car == '.' && text.contains("."))) {
            evt.consume();
        }
    }

}
