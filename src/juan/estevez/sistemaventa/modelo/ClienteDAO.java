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
            JOptionPane.showMessageDialog(null, e.toString());
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
            System.err.println(e.toString());
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
}
