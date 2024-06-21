package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.modelo.Cliente;
import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones relacionadas con Clientes.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteDAO {
    
    private static ClienteDAO instance;
    private final Connection connection;

    private static final String INSERT_CLIENTE_SQL = "INSERT INTO CLIENTES (DNI, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
    private static final String SELECT_ALL_CLIENTES_SQL = "SELECT * FROM CLIENTES";
    private static final String DELETE_CLIENTE_SQL = "DELETE FROM CLIENTES WHERE ID = ?";
    private static final String UPDATE_CLIENTE_SQL = "UPDATE CLIENTES SET DNI = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
    private static final String SELECT_CLIENTE_BY_DNI_SQL = "SELECT * FROM CLIENTES WHERE DNI = ?";

    public static ClienteDAO getInstance() {
        return instance == null ?  new ClienteDAO() : instance;
    }
    
    private ClienteDAO() {
        this.connection = Conexion.getInstance().getConnection();
    }
    
    /**
     * Registra un nuevo cliente en la base de datos.
     *
     * @param cliente el objeto Cliente a registrar.
     * @throws SQLException si ocurre un error al insertar el cliente en la base de datos.
     */
    public void registrarCliente(Cliente cliente) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(INSERT_CLIENTE_SQL)) {
            setClienteAPreparedStatement(pst, cliente);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar cliente en ClienteDAO", e);
        }
    }

    /**
     * Lista los clientes presentes en la base de datos.
     *
     * @return listado de clientes encontrados
     * @throws SQLException si ocurre un error al listar clientes en la base de datos.
     */
    public List<Cliente> listarClientes() throws SQLException {
        List<Cliente> listaClientes = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_ALL_CLIENTES_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = obtenerCliente(rs);
                listaClientes.add(cliente);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar clientes en ClienteDAO", e);
        }
        return listaClientes;
    }

    /**
     * Obtiene una lista de todos los clientes almacenados en la base de datos.
     *
     * @param id id del cliente a eliminar
     * @throws SQLException si ocurre un error al eliminar un cliente en la base de datos.
     */
    public void eliminarCliente(int id) throws SQLException{
        try (PreparedStatement pst = connection.prepareStatement(DELETE_CLIENTE_SQL)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar cliente en ClienteDAO", e);
        }
    }

    /**
     * Modifica los datos de un cliente existente en la base de datos.
     *
     * @param cliente el objeto Cliente con los nuevos datos a actualizar.
     * @throws SQLException si ocurre un error al modificar el cliente en la base de datos.
     */
    public void modificarCliente(Cliente cliente) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(UPDATE_CLIENTE_SQL)) {
            setClienteAPreparedStatement(pst, cliente);
            pst.setInt(6, cliente.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar cliente en ClienteDAO", e);
        }
    }

    /**
     * Busca un cliente en la base de datos por su n�mero de documento.
     *
     * @param dni el n�mero de documento del cliente a buscar.
     * @return el objeto Cliente encontrado con el n�mero de documento especificado, o un objeto Cliente vac�o si no se encontr� ning�n cliente.
     * @throws SQLException si ocurre un error al consultar el cliente en la base de datos.
     */
    public Cliente buscarCliente(int dni) throws SQLException {
        Cliente cliente = new Cliente();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_CLIENTE_BY_DNI_SQL)) {
            pst.setInt(1, dni);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cliente = obtenerCliente(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al consultar cliente en ClienteDAO", e);
        }
        return cliente;
    }

    /**
     * Obtiene un cliente del resulset de base de datos a objeto.
     *
     * @param rs resulset con la información del cliente traida desde la base de datos.
     * @return cliente obtenido.
     * @throws SQLException en caso de error con la base de datos
     */
    private Cliente obtenerCliente(ResultSet rs) throws SQLException {
        return Cliente.builder()
                .id(rs.getInt("ID"))
                .dni(rs.getLong("DNI"))
                .nombre(rs.getString("NOMBRE"))
                .telefono(rs.getLong("TELEFONO"))
                .direccion(rs.getString("DIRECCION"))
                .razonSocial(rs.getString("RAZON_SOCIAL"))
                .build();
    }

    /**
     * Setea los datos de un cliente en un objeto prepared statement.
     *
     * @param pst prepared estatement a setear los datos del cliente.
     * @param cliente a manipular.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void setClienteAPreparedStatement(PreparedStatement pst, Cliente cliente) throws SQLException {
        pst.setLong(1, cliente.getDni());
        pst.setString(2, cliente.getNombre());
        pst.setLong(3, cliente.getTelefono());
        pst.setString(4, cliente.getDireccion());
        pst.setString(5, cliente.getRazonSocial());
    }

}
