package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Detalle {

    private int id;
    private int cantidad;
    private int idVenta;
    private String codigoProducto;
    private double precio;

    public Detalle() {
    }

    public Detalle(int id, int cantidad, int idVenta, String codigoProducto, double precio) {
        this.id = id;
        this.cantidad = cantidad;
        this.idVenta = idVenta;
        this.codigoProducto = codigoProducto;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

}
