package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
public class Venta {

    private int id;
    private String cliente;
    private String vendedor;
    private String fecha;
    private double total;

}
