package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
@Data
public class Usuario {

    private int id;
    private String correo;
    private String password;
    private String nombre;
    private String rol;

}
