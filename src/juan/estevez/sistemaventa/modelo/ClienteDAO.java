package juan.estevez.sistemaventa.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ClienteDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Registra un cliente en la base de datos.
     *
     * @param cliente a registrar
     * @return true si se registró, false si no se registró
     */
    public boolean registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO CLIENTES (DNI, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, cliente.getDni());
            pst.setString(2, cliente.getNombre());
            pst.setInt(3, cliente.getTelefono());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getRazonSocial());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar cliente en ClienteDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println(e.toString());
            }
        }
    }

    /**
     * Obtiene los clientes almacenados en la base de datos.
     *
     * @return lista con todos los clientes obtenidos de la base de datos.
     */
    public List listarClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT * FROM CLIENTES";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("ID"));
                cliente.setDni(rs.getInt("DNI"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setTelefono(rs.getInt("TELEFONO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setRazonSocial(rs.getString("RAZON_SOCIAL"));
                listaClientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar clientes en ClienteDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ClienteDAO " + e.toString());
            }
        }

        return listaClientes;
    }

    /**
     * Elimina un cliente de la base de datos.
     *
     * @param id por el cual se eliminará el cliente.
     * @return true si se eliminó el cliente y false si no se eliminó.
     */
    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM CLIENTES WHERE ID = ?";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el eliminar cliente en ClienteDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ClienteDAO " + e.toString());
            }
        }
    }

    /**
     * Actualiza un cliente directamente de la base de datos.
     *
     * @param cliente a actualizar
     * @return true si se actualizó, false si no se actualizó.
     */
    public boolean modificarCliente(Cliente cliente) {
        String sql = "UPDATE CLIENTES SET DNI = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, cliente.getDni());
            pst.setString(2, cliente.getNombre());
            pst.setInt(3, cliente.getTelefono());
            pst.setString(4, cliente.getDireccion());
            pst.setString(5, cliente.getRazonSocial());
            pst.setInt(6, cliente.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el modificar cliente en ClienteDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ClienteDAO " + e.toString());
            }
        }
    }

    /**
     * Busca un cliente en específico mediante su identificador primario.
     *
     * @param dni por el cuál se buscará el cliente.
     * @return cliente encontrado.
     */
    public Cliente buscarCliente(int dni) {
        Cliente cliente = new Cliente();
        String sql = "SELECT * FROM CLIENTES WHERE DNI = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, dni);
            rs = pst.executeQuery();
            if (rs.next()) {
                cliente.setDni(rs.getInt("DNI"));
                cliente.setNombre(rs.getString("NOMBRE"));
                cliente.setTelefono(rs.getInt("TELEFONO"));
                cliente.setDireccion(rs.getString("DIRECCION"));
                cliente.setRazonSocial(rs.getString("RAZON_SOCIAL"));
            }
        } catch (SQLException e) {
            System.err.println("Error el consultar cliente en ClienteDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ClienteDAO " + e.toString());
            }
        }
        return cliente;
    }
}
