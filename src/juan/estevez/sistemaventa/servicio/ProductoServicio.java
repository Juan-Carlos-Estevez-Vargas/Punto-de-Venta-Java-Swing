package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTable;
import juan.estevez.sistemaventa.daos.ProductoDAO;
import juan.estevez.sistemaventa.modelo.Producto;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProductoServicio {

    private final ProductoDAO productoDAO = new ProductoDAO();

    public void actualizarStock(JTable tableVenta, Producto producto) throws SQLException {
        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            String codigoProducto = tableVenta.getValueAt(i, 0).toString();
            int cantidadProducto = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());
            producto = this.productoDAO.buscarProducto(codigoProducto);
            int stockActual = producto.getStock() - cantidadProducto;
            this.productoDAO.actualizarStock(stockActual, codigoProducto);
        }
    }
    
    public List<Producto> getAllProductos() throws SQLException {
        return this.productoDAO.listarProductos();
    }
    
    public void registrarProducto(Producto producto) throws SQLException {
        this.productoDAO.registrarProducto(producto);
    }
    
    public void eliminarProducto(int idProducto) throws SQLException {
        this.productoDAO.eliminarProducto(idProducto);
    }
    
    public void modificarProducto(Producto producto) throws SQLException {
        this.productoDAO.modificarProducto(producto);
    }
    
    public Producto buscarProducto(String codigo) throws SQLException {
        return this.productoDAO.buscarProducto(codigo);
    }
    
    public void consultarProveedor(JComboBox<String> proveedor) throws SQLException {
        this.productoDAO.consultarProveedor(proveedor);
    }
    
}
