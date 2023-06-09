package juan.estevez.sistemaventa.daos;

import java.sql.*;
import java.util.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con Clientes.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteDAO {

	private Connection cn;

	/**
	 * Registra un nuevo cliente en la base de datos.
	 *
	 * @param cliente el objeto Cliente a registrar.
	 * @return true si el cliente se registró correctamente, false de lo contrario.
	 * @throws RuntimeException si ocurre un error al insertar el cliente en la base de datos.
	 */
	public boolean registrarCliente(Cliente cliente) {
		String sql = "INSERT INTO CLIENTES (DNI, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
		try (PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setLong(1, cliente.getDni());
			pst.setString(2, cliente.getNombre());
			pst.setLong(3, cliente.getTelefono());
			pst.setString(4, cliente.getDireccion());
			pst.setString(5, cliente.getRazonSocial());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException("Error al insertar cliente en ClienteDAO", e);
		}
	}

	public List<Cliente> listarClientes() {
		List<Cliente> listaClientes = new ArrayList<>();
		String sql = "SELECT * FROM CLIENTES";
		try (PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
			while (rs.next()) {
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("ID"));
				cliente.setDni(rs.getLong("DNI"));
				cliente.setNombre(rs.getString("NOMBRE"));
				cliente.setTelefono(rs.getLong("TELEFONO"));
				cliente.setDireccion(rs.getString("DIRECCION"));
				cliente.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				listaClientes.add(cliente);
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error al listar clientes en ClienteDAO", e);
		}
		return listaClientes;
	}

	/**
	 * Obtiene una lista de todos los clientes almacenados en la base de datos.
	 *
	 * @return una lista de objetos Cliente que representan a los clientes almacenados.
	 * @throws RuntimeException si ocurre un error al listar los clientes en la base de datos.
	 */
	public boolean eliminarCliente(int id) {
		String sql = "DELETE FROM CLIENTES WHERE ID = ?";
		try (PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setInt(1, id);
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException("Error al eliminar cliente en ClienteDAO", e);
		}
	}

	/**
	 * Modifica los datos de un cliente existente en la base de datos.
	 *
	 * @param cliente el objeto Cliente con los nuevos datos a actualizar.
	 * @return true si se realizó la modificación correctamente, false en caso contrario.
	 * @throws RuntimeException si ocurre un error al modificar el cliente en la base de datos.
	 */
	public boolean modificarCliente(Cliente cliente) {
		String sql = "UPDATE CLIENTES SET DNI = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
		try (PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setLong(1, cliente.getDni());
			pst.setString(2, cliente.getNombre());
			pst.setLong(3, cliente.getTelefono());
			pst.setString(4, cliente.getDireccion());
			pst.setString(5, cliente.getRazonSocial());
			pst.setInt(6, cliente.getId());
			pst.execute();
			return true;
		} catch (SQLException e) {
			throw new RuntimeException("Error al modificar cliente en ClienteDAO", e);
		}
	}

	/**
	 * Busca un cliente en la base de datos por su número de documento.
	 *
	 * @param dni el número de documento del cliente a buscar.
	 * @return el objeto Cliente encontrado con el número de documento especificado, o un objeto Cliente vacío si no se encontró ningún cliente.
	 * @throws RuntimeException si ocurre un error al consultar el cliente en la base de datos.
	 */
	public Cliente buscarCliente(int dni) {
		Cliente cliente = new Cliente();
		String sql = "SELECT * FROM CLIENTES WHERE DNI = ?";
		try (PreparedStatement pst = cn.prepareStatement(sql)) {
			pst.setInt(1, dni);
			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					cliente.setDni(rs.getLong("DNI"));
					cliente.setNombre(rs.getString("NOMBRE"));
					cliente.setTelefono(rs.getLong("TELEFONO"));
					cliente.setDireccion(rs.getString("DIRECCION"));
					cliente.setRazonSocial(rs.getString("RAZON_SOCIAL"));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error al consultar cliente en ClienteDAO", e);
		}
		return cliente;
	}
}
