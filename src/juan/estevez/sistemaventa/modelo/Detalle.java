package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
public class Detalle {

    private int id;
    private int cantidad;
    private int idVenta;
    private String codigoProducto;
    private double precio;

}
