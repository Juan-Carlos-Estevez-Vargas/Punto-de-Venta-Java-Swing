package juan.estevez.sistemaventa.utils;

import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.utils.enums.Colors;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Utilitarios {
    
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");
    private static final String ERROR = messages.getString("error");
    private static final String EXITO = messages.getString("exito");
    private static final String ADVERTENCIA = messages.getString("advertencia");

    /**
     * Muestra un mensaje en una ventana de diálogo.
     *
     * @param mensaje el mensaje a mostrar.
     * @param title el título de la ventana de diálogo.
     * @param tipo el tipo de mensaje, puede ser uno de los siguientes:
     * <ul>
     * <li>{@link JOptionPane#ERROR_MESSAGE}</li>
     * <li>{@link JOptionPane#INFORMATION_MESSAGE}</li>
     * <li>{@link JOptionPane#WARNING_MESSAGE}</li>
     * <li>{@link JOptionPane#QUESTION_MESSAGE}</li>
     * <li>{@link JOptionPane#PLAIN_MESSAGE}</li>
     * </ul>
     */
    public static void mostrarMensaje(String mensaje, String title, int tipo) {
        JOptionPane.showMessageDialog(null, mensaje, title, tipo);
    }
    
    public static void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, ERROR, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, EXITO, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, ADVERTENCIA, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void limpiarEstilosBotones(JButton btn1, JButton btn2, JButton btn3, JButton btn4, JButton btn5, JButton btn6) {
        btn1.setBackground(Colors.LIGHT_GRAY.getColor());
        btn2.setBackground(Colors.LIGHT_GRAY.getColor());
        btn3.setBackground(Colors.LIGHT_GRAY.getColor());
        btn4.setBackground(Colors.LIGHT_GRAY.getColor());
        btn5.setBackground(Colors.LIGHT_GRAY.getColor());
        btn6.setBackground(Colors.LIGHT_GRAY.getColor());
    }
    
    public static String eliminarEspaciosEnBlanco(String texto) {
        return texto.trim();
    }
    
    /**
     * Limpia la tabla venta donde se encuentran los productos de la venta.
     * @param modeloTemporal
     * @param tableVenta
     */
    public static void limpiarTableVenta(DefaultTableModel modeloTemporal, JTable tableVenta) {
        modeloTemporal = (DefaultTableModel) tableVenta.getModel();
        int filas = tableVenta.getRowCount();

        for (int i = 0; i < filas; i++) {
            modeloTemporal.removeRow(0);
        }
    }

}
