package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import juan.estevez.sistemaventa.modelo.Detalle;
import juan.estevez.sistemaventa.modelo.Venta;

/**
 * DAO para operaciones relacionadas con ventas.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class VentaDAO {

    private static final String INSERT_VENTA_SQL = "INSERT INTO VENTAS (CLIENTE, VENDEDOR, TOTAL, FECHA) VALUES (?,?,?,?)";
    private static final String INSERT_DETALLE_VENTA_SQL = "INSERT INTO DETALLE (CODIGO_PRODUCTO, CANTIDAD, PRECIO, ID_VENTA) VALUES (?,?,?,?)";
    private static final String SELECT_MAX_ID_FROM_VENTAS_SQL = "SELECT MAX(ID) FROM VENTAS";
    private static final String SELECT_ALL_VENTAS_SQL = "SELECT * FROM VENTAS";

    /**
     * Registra una venta en la base de datos.
     *
     * @param venta la venta a registrar.
     * @return el n�mero de registros insertados.
     * @throws SQLException si ocurre un error al registrar la venta.
     */
    public int registrarVenta(Venta venta) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(INSERT_VENTA_SQL)) {
            crearPreparedStatementDesdeVenta(pst, venta);
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
     * @throws SQLException si ocurre un error al registrar el detalle de la venta.
     */
    public int registrarDetalleVenta(Detalle detalle) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(INSERT_DETALLE_VENTA_SQL)) {
            crearPreparedStatementDesdeDetalleVenta(pst, detalle);
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
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_MAX_ID_FROM_VENTAS_SQL); ResultSet rs = pst.executeQuery()) {
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
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_ALL_VENTAS_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listaVentas.add(crearVentaDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar las ventas", e);
        }
        return listaVentas;
    }

    /**
     * Crea un objeto de tipo Venta con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo Venta con los datos de inicio de sesión.
     * @throws SQLException en caso de error con la base de datos.
     */
    private Venta crearVentaDesdeResultSet(ResultSet rs) throws SQLException {
        return Venta.builder()
                .id(rs.getInt("ID"))
                .cliente(rs.getString("CLIENTE"))
                .vendedor(rs.getString("VENDEDOR"))
                .total(rs.getDouble("TOTAL"))
                .build();
    }

    /**
     * Setea los datos de la venta al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param venta objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeVenta(PreparedStatement pst, Venta venta) throws SQLException {
        pst.setString(1, venta.getCliente());
        pst.setString(2, venta.getVendedor());
        pst.setDouble(3, venta.getTotal());
        pst.setString(4, venta.getFecha());
    }

    /**
     * Setea los datos del detalle de la venta al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param detalle objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeDetalleVenta(PreparedStatement pst, Detalle detalle) throws SQLException {
        pst.setString(1, detalle.getCodigoProducto());
        pst.setInt(2, detalle.getCantidad());
        pst.setDouble(3, detalle.getPrecio());
        pst.setInt(4, detalle.getIdVenta());
    }

}
