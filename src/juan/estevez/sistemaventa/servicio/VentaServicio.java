package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import java.util.List;
import juan.estevez.sistemaventa.daos.VentaDAO;
import juan.estevez.sistemaventa.modelo.Venta;

/**
 *
 * @author Juan Carlos Estevez vargas
 */
public class VentaServicio {

    private final VentaDAO ventaDAO = new VentaDAO();

    public List<Venta> getAllVentas() throws SQLException {
        return this.ventaDAO.listarVentas();
    }
    
    public void registrarVenta(Venta venta) throws SQLException {
        this.ventaDAO.registrarVenta(venta);
    }
    
    public int obtenerIdVenta() throws SQLException {
        return this.ventaDAO.obtenerIdVenta();
    }

}
