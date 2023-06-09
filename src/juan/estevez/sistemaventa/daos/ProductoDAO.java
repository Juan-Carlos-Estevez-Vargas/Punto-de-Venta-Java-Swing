package juan.estevez.sistemaventa.daos;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con Productos.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProductoDAO {

	/**
	 * Registra un nuevo producto en la base de datos.
	 *
	 * @param producto el objeto Producto que contiene la información del producto a registrar.
	 * @return true si el producto se registró correctamente, false en caso contrario.
	 * @throws SQLException si ocurre un error al registrar el producto.
	 */
	public boolean registrarProducto(Producto producto) throws SQLException {
		String sql = "INSERT INTO PRODUCTO (CODIGO, DESCRIPCION, PROVEEDOR, STOCK, PRECIO) VALUES (?,?,?,?,?)";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setString(1, producto.getCodigo());
			pst.setString(2, producto.getNombre());
			pst.setString(3, producto.getProveedor());
			pst.setInt(4, producto.getStock());
			pst.setDouble(5, producto.getPrecio());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al registrar producto", e);
		}
	}

	/**
	 * Consulta los proveedores de la base de datos y los agrega al JComboBox especificado.
	 *
	 * @param proveedor el JComboBox donde se agregarán los nombres de los proveedores.
	 * @throws SQLException si ocurre un error al consultar los proveedores.
	 */
	public void consultarProveedor(JComboBox<String> proveedor) throws SQLException {
		String sql = "SELECT NOMBRE FROM PROVEEDOR";
		try (Connection cn = Conexion.conectar();
				PreparedStatement pst = cn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
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
		String sql = "SELECT * FROM PRODUCTO";
		try (Connection cn = Conexion.conectar();
				PreparedStatement pst = cn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				Producto producto = new Producto();
				producto.setId(rs.getInt("ID"));
				producto.setCodigo(rs.getString("CODIGO"));
				producto.setNombre(rs.getString("DESCRIPCION"));
				producto.setProveedor(rs.getString("PROVEEDOR"));
				producto.setStock(rs.getInt("STOCK"));
				producto.setPrecio(rs.getDouble("PRECIO"));
				listaProductos.add(producto);
			}
		} catch (SQLException e) {
			throw new SQLException("Error al listar los productos", e);
		}
		return listaProductos;
	}

	/**
	 * Elimina un producto de la base de datos según su identificador.
	 *
	 * @param id el identificador del producto a eliminar.
	 * @return true si el producto se eliminó correctamente, false de lo contrario.
	 * @throws SQLException si ocurre un error al eliminar el producto.
	 */
	public boolean eliminarProducto(int id) throws SQLException {
		String sql = "DELETE FROM PRODUCTO WHERE ID = ?";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al eliminar producto", e);
		}
	}

	/**
	 * Modifica un producto en la base de datos.
	 *
	 * @param producto el producto con los nuevos datos a modificar.
	 * @return true si el producto se modificó correctamente, false de lo contrario.
	 * @throws SQLException si ocurre un error al modificar el producto.
	 */
	public boolean modificarProducto(Producto producto) throws SQLException {
		String sql = "UPDATE PRODUCTO SET CODIGO = ?, DESCRIPCION = ?, PROVEEDOR = ?, STOCK = ?, PRECIO = ? WHERE ID = ?";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setString(1, producto.getCodigo());
			pst.setString(2, producto.getNombre());
			pst.setString(3, producto.getProveedor());
			pst.setInt(4, producto.getStock());
			pst.setDouble(5, producto.getPrecio());
			pst.setInt(6, producto.getId());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al modificar producto", e);
		}
	}

	/**
	 * Busca un producto en la base de datos por su código.
	 *
	 * @param codigoProducto el código del producto a buscar.
	 * @return el objeto Producto correspondiente al código especificado, o un objeto Producto vacío si no se encuentra ningún producto con ese código.
	 * @throws SQLException si ocurre un error al buscar el producto.
	 */
	public Producto buscarProducto(String codigoProducto) throws SQLException {
		Producto producto = new Producto();
		String sql = "SELECT * FROM PRODUCTO WHERE CODIGO = ?";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
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
	 * @param cantidad       la nueva cantidad de stock del producto.
	 * @param codigoProducto el código del producto a actualizar.
	 * @return true si el stock se actualizó correctamente, false en caso contrario.
	 * @throws SQLException si ocurre un error al actualizar el stock.
	 */
	public boolean actualizarStock(int cantidad, String codigoProducto) throws SQLException {
		String sql = "UPDATE PRODUCTO SET STOCK = ? WHERE CODIGO = ?";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setInt(1, cantidad);
			pst.setString(2, codigoProducto);
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al actualizar stock", e);
		}
	}

}
