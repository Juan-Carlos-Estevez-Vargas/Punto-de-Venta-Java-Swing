package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Producto {

    private int id, stock;
    private String codigo, nombre, proveedor;
    private double precio;

    public Producto() {
    }

    public Producto(int id, int stock, String codigo, String nombre, String proveedor, double precio) {
        this.id = id;
        this.stock = stock;
        this.codigo = codigo;
        this.nombre = nombre;
        this.proveedor = proveedor;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
