package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con ventas.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class VentaDAO {

    /**
     * Registra una venta en la base de datos.
     *
     * @param venta la venta a registrar.
     * @return el n�mero de registros insertados.
     * @throws SQLException si ocurre un error al registrar la venta.
     */
    public int registrarVenta(Venta venta) throws SQLException {
        String sql = "INSERT INTO VENTAS (CLIENTE, VENDEDOR, TOTAL, FECHA) VALUES (?,?,?,?)";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, venta.getCliente());
            pst.setString(2, venta.getVendedor());
            pst.setDouble(3, venta.getTotal());
            pst.setString(4, venta.getFecha());
            return pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta en VentaDAO");
            throw new SQLException("Error al registrar la venta", e);
        }
    }

    /**
     * Registra el detalle de una venta en la base de datos.
     *
     * @param detalle el detalle de la venta a registrar.
     * @return el n�mero de registros insertados.
     * @throws SQLException si ocurre un error al registrar el detalle de la
     * venta.
     */
    public int registrarDetalleVenta(Detalle detalle) throws SQLException {
        String sql = "INSERT INTO DETALLE (CODIGO_PRODUCTO, CANTIDAD, PRECIO, ID_VENTA) VALUES (?,?,?,?)";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, detalle.getCodigoProducto());
            pst.setInt(2, detalle.getCantidad());
            pst.setDouble(3, detalle.getPrecio());
            pst.setInt(4, detalle.getIdVenta());
            return pst.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar el detalle de venta", e);
        }
    }

    /**
     * Obtiene el ID m�ximo de las ventas registradas en la base de datos.
     *
     * @return el ID m�ximo de las ventas.
     * @throws SQLException si ocurre un error al obtener el ID de venta.
     */
    public int obtenerIdVenta() throws SQLException {
        int id = 1;
        String sql = "SELECT MAX(ID) FROM VENTAS";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener el ID de venta", e);
        }
        return id;
    }

    /**
     * Obtiene una lista de todas las ventas almacenadas en la base de datos.
     *
     * @return una lista de ventas.
     * @throws SQLException si ocurre un error al listar las ventas.
     */
    public List<Venta> listarVentas() throws SQLException {
        List<Venta> listaVentas = new ArrayList<>();
        String sql = "SELECT * FROM VENTAS";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("ID"));
                venta.setCliente(rs.getString("CLIENTE"));
                venta.setVendedor(rs.getString("VENDEDOR"));
                venta.setTotal(rs.getDouble("TOTAL"));
                listaVentas.add(venta);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las ventas", e);
        }
        return listaVentas;
    }

}
