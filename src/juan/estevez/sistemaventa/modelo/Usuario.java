package juan.estevez.sistemaventa.modelo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
@Data
@Builder
@NoArgsConstructor
public class Usuario {

    private int id;
    private String correo;
    private String password;
    private String nombre;
    private String rol;

}
