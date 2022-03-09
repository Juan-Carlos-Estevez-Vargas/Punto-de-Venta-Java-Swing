package juan.estevez.sistemaventa.daos;

import java.sql.*;
import javax.swing.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class VentaDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;
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
            JOptionPane.showMessageDialog(null, "Error al resgitrar la venta en VentaDAO");
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

    /**
     * Consulta el id máximo de la tabla ventas.
     *
     * @return id obtenido.
     */
    public int idVenta() {
        int id = 1;
        String sql = "SELECT MAX(ID) FROM VENTAS";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ID_VENTA en VentaDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en VentaDAO " + e.toString());
            }
        }
        return id;
    }

}
