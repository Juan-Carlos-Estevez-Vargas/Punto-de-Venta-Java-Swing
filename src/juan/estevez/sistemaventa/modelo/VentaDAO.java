package juan.estevez.sistemaventa.modelo;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class VentaDAO {

    Connection cn;
    PreparedStatement pst;
    int response;

    /**
     * Ingresa una venta a la base de datos.
     *
     * @param venta a registrar en la base de datos.
     * @return registros insertados.
     */
    public int registrarVenta(Venta venta) {
        String sql = "INSERT INTO VENTAS (CLIENTE, VENDEDOR, TOTAL) VALUES (?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, venta.getCliente());
            pst.setString(2, venta.getVendedor());
            pst.setDouble(3, venta.getTotal());
            pst.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al resgitrar la venta ");
            System.err.println(e.toString());
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en VentaDAO " + e.toString());
            }
        }
        return response;
    }

    /**
     * Ingresa el detalle de la venta a la base de datos.
     *
     * @param detalle a insertar en la base de datos.
     * @return número de registros insertados.
     */
    public int registrarDetalleVenta(Detalle detalle) {
        String sql = "INSERT INTO DETALLE (CODIGO_PRODUCTO, CANTIDAD, PRECIO, ID_VENTA) VALUES (?,?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, detalle.getCodigoProducto());
            pst.setInt(2, detalle.getCantidad());
            pst.setDouble(3, detalle.getPrecio());
            pst.setInt(4, detalle.getIdVenta());
            pst.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al resgitrar el detalle de venta ");
            System.err.println(e.toString());
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en VentaDAO " + e.toString());
            }
        }
        return response;
    }
}
