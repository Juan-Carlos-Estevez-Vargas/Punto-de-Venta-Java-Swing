package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.*;
import java.util.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con Clientes.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteDAO {

    /**
     * Registra un nuevo cliente en la base de datos.
     *
     * @param cliente el objeto Cliente a registrar.
     * @throws RuntimeException si ocurre un error al insertar el cliente en la
     * base de datos.
     */
    public void registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTES (DNI, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setLong(1, cliente.getDni());
            pst.setString(2, cliente.getNombre());
            pst.setLong(3, cliente.getTelefono());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getRazonSocial());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente en ClienteDAO", e);
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTES";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
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
     * @param id id del cliente a eliminar
     * @throws RuntimeException si ocurre un error al listar los clientes en la
     * base de datos.
     */
    public void eliminarCliente(int id) {
        String sql = "DELETE FROM CLIENTES WHERE ID = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente en ClienteDAO", e);
        }
    }

    /**
     * Modifica los datos de un cliente existente en la base de datos.
     *
     * @param cliente el objeto Cliente con los nuevos datos a actualizar.
     * @throws RuntimeException si ocurre un error al modificar el cliente en la
     * base de datos.
     */
    public void modificarCliente(Cliente cliente) {
        String sql = "UPDATE CLIENTES SET DNI = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setLong(1, cliente.getDni());
            pst.setString(2, cliente.getNombre());
            pst.setLong(3, cliente.getTelefono());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getRazonSocial());
            pst.setInt(6, cliente.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al modificar cliente en ClienteDAO", e);
        }
    }

    /**
     * Busca un cliente en la base de datos por su n�mero de documento.
     *
     * @param dni el n�mero de documento del cliente a buscar.
     * @return el objeto Cliente encontrado con el n�mero de documento
     * especificado, o un objeto Cliente vac�o si no se encontr� ning�n cliente.
     * @throws RuntimeException si ocurre un error al consultar el cliente en la
     * base de datos.
     */
    public Cliente buscarCliente(int dni) {
        Cliente cliente = new Cliente();
        String sql = "SELECT * FROM CLIENTES WHERE DNI = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
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
