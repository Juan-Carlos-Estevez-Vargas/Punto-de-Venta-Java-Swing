package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import java.util.List;
import juan.estevez.sistemaventa.daos.ClienteDAO;
import juan.estevez.sistemaventa.modelo.Cliente;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteServicio {

    private final ClienteDAO clienteDAO = new ClienteDAO();

    public List<Cliente> getAllClientes() throws SQLException {
        return this.clienteDAO.listarClientes();
    }
    
    public void registrarCliente(Cliente cliente) throws SQLException {
        this.clienteDAO.registrarCliente(cliente);
    }
    
    public void modificarCliente(Cliente cliente) throws SQLException {
        this.clienteDAO.modificarCliente(cliente);
    }
    
    public void eliminarCliente(int idCliente) throws SQLException {
        this.clienteDAO.eliminarCliente(idCliente);
    }
    
    public Cliente buscarCliente(int dniRut) throws SQLException {
        return this.clienteDAO.buscarCliente(dniRut);
    }

}
