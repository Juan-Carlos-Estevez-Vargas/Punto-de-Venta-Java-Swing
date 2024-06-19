package juan.estevez.sistemaventa.modelo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
@Builder
@NoArgsConstructor
public class Detalle {

    private int id;
    private int cantidad;
    private int idVenta;
    private String codigoProducto;
    private double precio;

}
