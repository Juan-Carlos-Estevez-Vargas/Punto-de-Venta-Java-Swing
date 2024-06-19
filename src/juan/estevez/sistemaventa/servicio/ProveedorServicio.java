package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import java.util.List;
import juan.estevez.sistemaventa.daos.ProveedorDAO;
import juan.estevez.sistemaventa.modelo.Proveedor;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorServicio {

    private final ProveedorDAO proveedorDAO = new ProveedorDAO();

    public List<Proveedor> getAllProveedores() throws SQLException {
        return this.proveedorDAO.listarProveedores();
    }

    public void registrarProveedor(Proveedor proveedor) throws SQLException {
        this.proveedorDAO.registrarProveedor(proveedor);
    }
    
    public void modificarProveedor(Proveedor proveedor) throws SQLException {
        this.proveedorDAO.modificarProveedor(proveedor);
    }
    
    public void eliminarProveedor(int idProveedor) throws SQLException {
        this.proveedorDAO.eliminarProveedor(idProveedor);
    }
}
