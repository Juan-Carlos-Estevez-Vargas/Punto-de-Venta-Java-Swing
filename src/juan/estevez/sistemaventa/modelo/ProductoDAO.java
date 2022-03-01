package juan.estevez.sistemaventa.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProductoDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Registra un producto en la base de datos.
     *
     * @param producto a registrar
     * @return true si se registró, false si no se registró
     */
    public boolean registrarProducto(Producto producto) {
        String sql = "INSERT INTO PRODUCTO (CODIGO, DESCRIPCION, PROVEEDOR, STOCK, PRECIO) VALUES (?,?,?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, producto.getCodigo());
            pst.setString(2, producto.getNombre());
            pst.setString(3, producto.getProveedor());
            pst.setInt(4, producto.getStock());
            pst.setDouble(5, producto.getPrecio());
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
    
    public void consultarProveedor(JComboBox proveedor) {
        String sql = "SELECT NOMBRE FROM PROVEEDOR";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();
            
            while (rs.next()) {
                proveedor.addItem(rs.getString("NOMBRE"));
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println(e.toString());
            }
        }
    }
    
    /**
     * Obtiene los productos almacenados en la base de datos.
     *
     * @return lista con todos los productos obtenidos de la base de datos.
     */
    public List listarProductos() {
        List<Producto> listaProductos = new ArrayList<>();
        String sql = "SELECT * FROM PRODUCTO";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();

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
            System.err.println(e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProveedorDAO " + e.toString());
            }
        }

        return listaProductos;
    }
}
