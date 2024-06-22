package juan.estevez.sistemaventa.modelo;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Venta {

    private int id;
    private String cliente;
    private String vendedor;
    private String fecha;
    private double total;

}
