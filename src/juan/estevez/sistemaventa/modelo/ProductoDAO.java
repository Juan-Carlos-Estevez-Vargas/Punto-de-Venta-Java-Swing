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
            JOptionPane.showMessageDialog(null, "Error al registrar producto " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerras los objetos en ProductoDAO " + e.toString());
            }
        }
    }

    /**
     * Consulta el nombre de los proveedores de la base de datos.
     *
     * @param proveedor de tipo JComboBox al cuál se le seteará la lista de
     * proveedores
     */
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
            System.err.println("Error al consultar proveedores en ProductoDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerras los objetos en ProductoDAO " + e.toString());
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
            System.err.println("Error al listar los productos en ProductoDAO " + e.toString());
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

    /**
     * Elimina un producto de la base de datos.
     *
     * @param id por el cual se eliminará el producto.
     * @return true si se eliminó el producto y false si no se eliminó.
     */
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM PRODUCTO WHERE ID = ?";

        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el eliminar producto en productoDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en productoDAO " + e.toString());
            }
        }
    }

    /**
     * Actualiza un producto directamente de la base de datos.
     *
     * @param producto a actualizar
     * @return true si se actualizó, false si no se actualizó.
     */
    public boolean modificarProducto(Producto producto) {
        String sql = "UPDATE PRODUCTO SET CODIGO = ?, DESCRIPCION = ?, PROVEEDOR = ?, STOCK = ?, PRECIO = ? WHERE ID = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, producto.getCodigo());
            pst.setString(2, producto.getNombre());
            pst.setString(3, producto.getProveedor());
            pst.setInt(4, producto.getStock());
            pst.setDouble(5, producto.getPrecio());
            pst.setInt(6, producto.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el modificar producto en productoDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProductoDAO " + e.toString());
            }
        }
    }

    /**
     * Busca un producto en específico de la base de datos.
     *
     * @param codigoProducto por el cual se buscará el producto.
     * @return producto encontrado.
     */
    public Producto buscarProducto(String codigoProducto) {
        Producto producto = new Producto();
        String sql = "SELECT * FROM PRODUCTO WHERE CODIGO = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, codigoProducto);
            rs = pst.executeQuery();
            if (rs.next()) {
                producto.setNombre(rs.getString("DESCRIPCION"));
                producto.setPrecio(rs.getDouble("PRECIO"));
                producto.setStock(rs.getInt("STOCK"));

            }
        } catch (SQLException e) {
            System.err.println("Error el buscar producto en productoDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en ProductoDAO " + e.toString());
            }
        }
        return producto;
    }
}
