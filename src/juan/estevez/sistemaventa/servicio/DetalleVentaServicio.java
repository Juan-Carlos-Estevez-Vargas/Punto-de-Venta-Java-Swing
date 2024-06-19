package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import javax.swing.JTable;
import juan.estevez.sistemaventa.daos.VentaDAO;
import juan.estevez.sistemaventa.modelo.Detalle;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class DetalleVentaServicio {

    private final VentaDAO ventaDAO = new VentaDAO();

    public void registrarDetalleVenta(JTable tableVenta) throws SQLException {
        int id = this.ventaDAO.obtenerIdVenta();

        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            String codigoProducto = tableVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(tableVenta.getValueAt(i, 3).toString());
            this.ventaDAO.registrarDetalleVenta(Detalle.builder().id(id).codigoProducto(codigoProducto).cantidad(cantidad).precio(precio).build());
        }
    }
}
