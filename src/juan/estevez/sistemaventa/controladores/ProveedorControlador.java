package juan.estevez.sistemaventa.controladores;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.modelo.Proveedor;
import juan.estevez.sistemaventa.servicio.ProveedorServicio;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.utils.vista.GUIUtils;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorControlador {

    private static ProveedorControlador instance;
    private final ProveedorServicio proveedorServicio;
    private final ResourceBundle messages;
    private List<Proveedor> proveedores;

    public static ProveedorControlador getInstance() {
        if (instance == null) {
            synchronized (ProveedorControlador.class) {
                if (instance == null) {
                    instance = new ProveedorControlador();
                }
            }
        }
        return instance;
    }

    public ProveedorControlador() {
        this.proveedorServicio = ProveedorServicio.getInstance();
        this.proveedores = listarProveedores();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    private List<Proveedor> listarProveedores() {
        try {
            this.proveedores = this.proveedorServicio.getAllProveedores();
            return this.proveedores;
        } catch (SQLException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("error"));
        }

        return new ArrayList<>();
    }

    public void listarProveedores(JTable tableProveedor) {
        this.listarProveedores();
        tableProveedor.setModel(GUIUtils.listarProveedores(proveedores, (DefaultTableModel) tableProveedor.getModel()));
    }
    
    public void listarProveedores(JComboBox<String> cbxProveedores) {
        this.proveedores.forEach(proveedor -> cbxProveedores.addItem(proveedor.getNombre()));
    }

    public boolean guardarProveedor(Proveedor proveedor) {
        try {
            proveedorServicio.registrarProveedor(proveedor);
            Utilitarios.mostrarMensajeExito(messages.getString("proveedor.registrado"));
            return true;
        } catch (NumberFormatException e) {
            Utilitarios.mostrarMensajeError(messages.getString("error.dni.telefono.numerico"));
        } catch (HeadlessException | SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }

    public boolean eliminarProveedor(int idProveedor) {
        try {
            if (Utilitarios.confirmarEliminacion()) {
                proveedorServicio.eliminarProveedor(idProveedor);
                Utilitarios.mostrarMensajeExito(messages.getString("proveedor.eliminado"));
                return true;
            }
        } catch (SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }

    public boolean editarProveedor(Proveedor proveedor) {
        try {
            proveedorServicio.modificarProveedor(proveedor);
            Utilitarios.mostrarMensajeExito(messages.getString("proveedor.actualizado"));
            return true;
        } catch (NumberFormatException e) {
            Utilitarios.mostrarMensajeError(messages.getString("error.dni.telefono.numerico"));
        } catch (HeadlessException | SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }
    
     public void limpiarTablaProveedores(JTable tableProveedores) {
        DefaultTableModel model = (DefaultTableModel) tableProveedores.getModel();
        model.setRowCount(0);
    }

}
