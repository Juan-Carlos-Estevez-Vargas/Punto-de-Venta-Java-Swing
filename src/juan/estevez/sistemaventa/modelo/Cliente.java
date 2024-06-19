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
public class Cliente {

    private int id;
    private long telefono;
    private long dni;
    private String nombre;
    private String razonSocial;
    private String direccion;

}
