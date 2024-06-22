package juan.estevez.sistemaventa.controladores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.modelo.Venta;
import juan.estevez.sistemaventa.servicio.VentaServicio;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.utils.vista.GUIUtils;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class VentaControlador {

    private static VentaControlador instance;
    private final VentaServicio ventaServicio;
    private final ResourceBundle messages;
    private final List<Venta> ventas;

    public static VentaControlador getInstance() {
        if (instance == null) {
            synchronized (VentaControlador.class) {
                if (instance == null) {
                    instance = new VentaControlador();
                }
            }
        }
        return instance;
    }

    public VentaControlador() {
        this.ventaServicio = VentaServicio.getInstance();
        this.ventas = listarVentas();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    private List<Venta> listarVentas() {
        try {
            return this.ventaServicio.getAllVentas();
        } catch (SQLException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("error"));
        }

        return new ArrayList<>();
    }

    public void listarVentas(JTable tableVenta) {
        tableVenta.setModel(GUIUtils.listarVentas(ventas, (DefaultTableModel) tableVenta.getModel()));
    }

    public void eliminarVenta(JTable tableVenta) {
        DefaultTableModel modelo = (DefaultTableModel) tableVenta.getModel();
        modelo.removeRow(tableVenta.getSelectedRow());
    }
    
    public void registrarVenta(Venta venta) {
        try {
            this.ventaServicio.registrarVenta(venta);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }
    
    public void registrarDetalleVenta(JTable table) {
        this.ventaServicio.registrarDetalleVenta(table);
    }

}
