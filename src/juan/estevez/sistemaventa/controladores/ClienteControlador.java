package juan.estevez.sistemaventa.controladores;

import java.awt.HeadlessException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.modelo.Cliente;
import juan.estevez.sistemaventa.servicio.ClienteServicio;
import juan.estevez.sistemaventa.utils.Utilitarios;
import juan.estevez.sistemaventa.utils.vista.GUIUtils;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteControlador {

    private static ClienteControlador instance;
    private final ClienteServicio clienteServicio;
    private final ResourceBundle messages;
    private final List<Cliente> clientes;

    public static ClienteControlador getInstance() {
        return instance == null ? new ClienteControlador() : instance;
    }

    public ClienteControlador() {
        this.clienteServicio = ClienteServicio.getInstance();
        this.clientes = listarClientes();
        this.messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    }

    private List<Cliente> listarClientes() {
        try {
            return this.clienteServicio.getAllClientes();
        } catch (SQLException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("error"));
        }

        return new ArrayList<>();
    }

    public void listarClientes(JTable tableClientes) {
        tableClientes.setModel(GUIUtils.listarClientes(clientes, (DefaultTableModel) tableClientes.getModel()));
    }

    public boolean guardarCliente(Cliente cliente) {
        try {
            clienteServicio.registrarCliente(cliente);
            Utilitarios.mostrarMensajeExito(messages.getString("cliente.registrado"));
            return true;
        } catch (NumberFormatException e) {
            Utilitarios.mostrarMensajeError(messages.getString("error.dni.telefono.numerico"));
        } catch (HeadlessException | SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }

    public boolean eliminarCliente(int idCliente) {
        try {
            if (Utilitarios.confirmarEliminacion()) {
                clienteServicio.eliminarCliente(idCliente);
                Utilitarios.mostrarMensajeExito(messages.getString("cliente.eliminado"));
                return true;
            }
        } catch (HeadlessException | SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }

    public boolean editarCliente(Cliente cliente) {
        try {
            clienteServicio.modificarCliente(cliente);
            Utilitarios.mostrarMensajeExito(messages.getString("cliente.actualizado"));
            return true;
        } catch (NumberFormatException e) {
            Utilitarios.mostrarMensajeError(messages.getString("error.dni.telefono.numerico"));
        } catch (HeadlessException | SQLException e) {
            Utilitarios.mostrarErrorGenerico(e);
        }

        return false;
    }
    
    public Cliente buscarCliente(int dniRut) {
        try {
            return this.clienteServicio.buscarCliente(dniRut);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
        return null;
    }
    
}
