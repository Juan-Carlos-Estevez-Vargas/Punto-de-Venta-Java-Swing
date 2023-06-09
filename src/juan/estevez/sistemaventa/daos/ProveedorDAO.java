package juan.estevez.sistemaventa.daos;

import java.sql.*;
import java.util.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con Proveedores.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorDAO {

	/**
	 * Registra un proveedor en la base de datos.
	 *
	 * @param proveedor el proveedor a registrar.
	 * @return true si el proveedor se registra correctamente, false en caso contrario.
	 * @throws SQLException si ocurre un error al registrar el proveedor.
	 */
	public boolean registrarProveedor(Proveedor proveedor) throws SQLException {
		String sql = "INSERT INTO PROVEEDOR (RUT, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setLong(1, proveedor.getRut());
			pst.setString(2, proveedor.getNombre());
			pst.setLong(3, proveedor.getTelefono());
			pst.setString(4, proveedor.getDireccion());
			pst.setString(5, proveedor.getRazonSocial());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al registrar proveedor", e);
		}
	}

	/**
	 * Obtiene una lista de todos los proveedores registrados en la base de datos.
	 *
	 * @return una lista de objetos Proveedor que representan a los proveedores registrados.
	 * @throws SQLException si ocurre un error al obtener la lista de proveedores.
	 */
	public List<Proveedor> listarProveedores() throws SQLException {
		List<Proveedor> listaProveedores = new ArrayList<>();
		String sql = "SELECT * FROM PROVEEDOR";

		try (Connection cn = Conexion.conectar();
				PreparedStatement pst = cn.prepareStatement(sql);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				Proveedor proveedor = new Proveedor();
				proveedor.setId(rs.getInt("ID"));
				proveedor.setRut(rs.getLong("RUT"));
				proveedor.setNombre(rs.getString("NOMBRE"));
				proveedor.setTelefono(rs.getLong("TELEFONO"));
				proveedor.setDireccion(rs.getString("DIRECCION"));
				proveedor.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				listaProveedores.add(proveedor);
			}
		} catch (SQLException e) {
			throw new SQLException("Error al listar proveedores", e);
		}
		return listaProveedores;
	}

	/**
	 * Elimina un proveedor de la base de datos dado su ID.
	 *
	 * @param id el ID del proveedor a eliminar.
	 * @return true si el proveedor se eliminó correctamente, false de lo contrario.
	 * @throws SQLException si ocurre un error al eliminar el proveedor.
	 */
	public boolean eliminarProveedor(int id) throws SQLException {
		String sql = "DELETE FROM PROVEEDOR WHERE ID = ?";

		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al eliminar proveedor", e);
		}
	}

	/**
	 * Modifica los datos de un proveedor en la base de datos.
	 *
	 * @param proveedor el proveedor con los datos actualizados.
	 * @return true si se modificaron los datos del proveedor correctamente, false de lo contrario.
	 * @throws SQLException si ocurre un error al modificar el proveedor.
	 */
	public boolean modificarProveedor(Proveedor proveedor) throws SQLException {
		String sql = "UPDATE PROVEEDOR SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
		try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setLong(1, proveedor.getRut());
			pst.setString(2, proveedor.getNombre());
			pst.setLong(3, proveedor.getTelefono());
			pst.setString(4, proveedor.getDireccion());
			pst.setString(5, proveedor.getRazonSocial());
			pst.setInt(6, proveedor.getId());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new SQLException("Error al modificar proveedor", e);
		}
	}
}
