package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.modelo.Producto;
import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 * DAO para operaciones relacionadas con Productos.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProductoDAO {

    private static final String INSERT_PRODUCTO_SQL = "INSERT INTO PRODUCTO (CODIGO, DESCRIPCION, PROVEEDOR, STOCK, PRECIO) VALUES (?,?,?,?,?)";
    private static final String SELECT_NOMBRE_BY_PROVEEDOR_SQL = "SELECT NOMBRE FROM PROVEEDOR";
    private static final String SELECT_ALL_PRODUCTOS_SQL = "SELECT * FROM PRODUCTO";
    private static final String DELETE_PRODUCTO_SQL = "DELETE FROM PRODUCTO WHERE ID = ?";
    private static final String UPDATE_PRODUCTO_SQL = "UPDATE PRODUCTO SET CODIGO = ?, DESCRIPCION = ?, PROVEEDOR = ?, STOCK = ?, PRECIO = ? WHERE ID = ?";
    private static final String SELECT_PRODUCTO_BY_CODIGO = "SELECT * FROM PRODUCTO WHERE CODIGO = ?";
    private static final String UPDATE_STOCK_SQL = "UPDATE PRODUCTO SET STOCK = ? WHERE CODIGO = ?";

    /**
     * Registra un nuevo producto en la base de datos.
     *
     * @param producto el objeto Producto que contiene la informaci�n del producto a registrar.
     * @throws SQLException si ocurre un error al registrar el producto.
     */
    public void registrarProducto(Producto producto) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(INSERT_PRODUCTO_SQL)) {
            crearPreparedStatementDesdeProducto(pst, producto);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar producto", e);
        }
    }

    /**
     * Consulta los proveedores de la base de datos y los agrega al JComboBox especificado.
     *
     * @param proveedor el JComboBox donde se agregar�n los nombres de los proveedores.
     * @throws SQLException si ocurre un error al consultar los proveedores.
     */
    public void consultarProveedor(JComboBox<String> proveedor) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_NOMBRE_BY_PROVEEDOR_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                proveedor.addItem(rs.getString("NOMBRE"));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al consultar proveedores", e);
        }
    }

    /**
     * Obtiene una lista de productos almacenados en la base de datos.
     *
     * @return una lista de productos.
     * @throws SQLException si ocurre un error al listar los productos.
     */
    public List<Producto> listarProductos() throws SQLException {
        List<Producto> listaProductos = new ArrayList<>();
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_ALL_PRODUCTOS_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listaProductos.add(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar los productos", e);
        }
        return listaProductos;
    }

    /**
     * Elimina un producto de la base de datos seg�n su identificador.
     *
     * @param id el identificador del producto a eliminar.
     * @throws SQLException si ocurre un error al eliminar el producto.
     */
    public void eliminarProducto(int id) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(DELETE_PRODUCTO_SQL)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar producto", e);
        }
    }

    /**
     * Modifica un producto en la base de datos.
     *
     * @param producto el producto con los nuevos datos a modificar.
     * @throws SQLException si ocurre un error al modificar el producto.
     */
    public void modificarProducto(Producto producto) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(UPDATE_PRODUCTO_SQL)) {
            crearPreparedStatementDesdeProducto(pst, producto);
            pst.setInt(6, producto.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar producto", e);
        }
    }

    /**
     * Busca un producto en la base de datos por su c�digo.
     *
     * @param codigoProducto el c�digo del producto a buscar.
     * @return el objeto Producto correspondiente al c�digo especificado, o un objeto Producto vac�o si no se encuentra ning�n producto con ese c�digo.
     * @throws SQLException si ocurre un error al buscar el producto.
     */
    public Producto buscarProducto(String codigoProducto) throws SQLException {
        Producto producto = new Producto();
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_PRODUCTO_BY_CODIGO)) {
            pst.setString(1, codigoProducto);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    producto.setNombre(rs.getString("DESCRIPCION"));
                    producto.setPrecio(rs.getDouble("PRECIO"));
                    producto.setStock(rs.getInt("STOCK"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar producto", e);
        }
        return producto;
    }

    /**
     * Actualiza el stock de un producto en la base de datos.
     *
     * @param cantidad la nueva cantidad de stock del producto.
     * @param codigoProducto el c�digo del producto a actualizar.
     * @throws SQLException si ocurre un error al actualizar el stock.
     */
    public void actualizarStock(int cantidad, String codigoProducto) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(UPDATE_STOCK_SQL)) {
            pst.setInt(1, cantidad);
            pst.setString(2, codigoProducto);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar stock", e);
        }
    }

    /**
     * Crea un objeto de tipo Producto con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo Producto con los datos de inicio de sesión.
     * @throws SQLException en caso de error con la base de datos.
     */
    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        return Producto.builder()
                .id(rs.getInt("ID"))
                .codigo(rs.getString("CODIGO"))
                .nombre(rs.getString("DESCRIPCION"))
                .proveedor(rs.getString("PROVEEDOR"))
                .stock(rs.getInt("STOCK"))
                .precio(rs.getDouble("PRECIO"))
                .build();
    }

    /**
     * Setea los datos del producto al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param producto objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeProducto(PreparedStatement pst, Producto producto) throws SQLException {
        pst.setString(1, producto.getCodigo());
        pst.setString(2, producto.getNombre());
        pst.setString(3, producto.getProveedor());
        pst.setInt(4, producto.getStock());
        pst.setDouble(5, producto.getPrecio());
    }

}
