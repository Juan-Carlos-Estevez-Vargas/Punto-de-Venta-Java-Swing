package juan.estevez.sistemaventa.daos;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Registra un proveedor en la base de datos.
     *
     * @param proveedor a registrar
     * @return true si se registró, false si no se registró
     */
    public boolean registrarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO PROVEEDOR (RUT, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, proveedor.getRut());
            pst.setString(2, proveedor.getNombre());
            pst.setInt(3, proveedor.getTelefono());
            pst.setString(4, proveedor.getDireccion());
            pst.setString(5, proveedor.getRazonSocial());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar proveedor en proveedorDAO" + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProveedorDAO " + e.toString());
            }
        }
    }

    /**
     * Obtiene los proveedores almacenados en la base de datos.
     *
     * @return lista con todos los proveedores obtenidos de la base de datos.
     */
    public List listarProveedores() {
        List<Proveedor> listaProveedores = new ArrayList<>();
        String sql = "SELECT * FROM PROVEEDOR";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("ID"));
                proveedor.setRut(rs.getInt("RUT"));
                proveedor.setNombre(rs.getString("NOMBRE"));
                proveedor.setTelefono(rs.getInt("TELEFONO"));
                proveedor.setDireccion(rs.getString("DIRECCION"));
                proveedor.setRazonSocial(rs.getString("RAZON_SOCIAL"));
                listaProveedores.add(proveedor);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar proveedores en ProveedorDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProveedorDAO " + e.toString());
            }
        }

        return listaProveedores;
    }

    /**
     * Elimina un proveedor de la base de datos.
     *
     * @param id por el cual se eliminará el proveedor.
     * @return true si se eliminó el proveedor y false si no se eliminó.
     */
    public boolean eliminarProveedor(int id) {
        String sql = "DELETE FROM PROVEEDOR WHERE ID = ?";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el eliminar proveedor en proveedor DAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en proveedorDAO " + e.toString());
            }
        }
    }

    /**
     * Actualiza un proveedor directamente de la base de datos.
     *
     * @param proveedor a actualizar
     * @return true si se actualizó, false si no se actualizó.
     */
    public boolean modificarProveedor(Proveedor proveedor) {
        String sql = "UPDATE PROVEEDOR SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, proveedor.getRut());
            pst.setString(2, proveedor.getNombre());
            pst.setInt(3, proveedor.getTelefono());
            pst.setString(4, proveedor.getDireccion());
            pst.setString(5, proveedor.getRazonSocial());
            pst.setInt(6, proveedor.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el modificar proveedor en ProveedorDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProveedorDAO " + e.toString());
            }
        }
    }
}
