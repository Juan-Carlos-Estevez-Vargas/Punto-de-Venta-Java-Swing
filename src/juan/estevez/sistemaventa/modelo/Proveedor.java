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
public class Proveedor {

    private int id;
    private long rut;
    private long telefono;
    private String nombre;
    private String direccion;
    private String razonSocial;

}
