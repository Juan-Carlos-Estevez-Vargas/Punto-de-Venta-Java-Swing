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
public class Loginn {

    private int id;
    private String nombre;
    private String correo;
    private String password;
    private String rol;

}
