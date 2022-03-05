package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Venta {

    private int id;
    private String cliente, vendedor;
    private double total;

    public Venta() {
    }

    public Venta(int id, String cliente, String vendedor, double total) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

}
