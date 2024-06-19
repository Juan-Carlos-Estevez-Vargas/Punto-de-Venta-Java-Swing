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
public class Producto {

    private int id;
    private int stock;
    private String codigo;
    private String nombre;
    private String proveedor;
    private double precio;
    
}
