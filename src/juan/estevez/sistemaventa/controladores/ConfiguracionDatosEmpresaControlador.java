package juan.estevez.sistemaventa.controladores;

import java.sql.SQLException;
import juan.estevez.sistemaventa.modelo.ConfiguracionDatosEmpresa;
import juan.estevez.sistemaventa.servicio.ConfiguracionDatosEmpresaServicio;
import juan.estevez.sistemaventa.utils.Utilitarios;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresaControlador {

    private static ConfiguracionDatosEmpresaControlador instance;
    private final ConfiguracionDatosEmpresaServicio configuracionDatosEmpresaServicio;

    public static ConfiguracionDatosEmpresaControlador getInstance() {
        if (instance == null) {
            synchronized (ConfiguracionDatosEmpresaControlador.class) {
                if (instance == null) {
                    instance = new ConfiguracionDatosEmpresaControlador();
                }
            }
        }
        return instance;
    }
    
    public ConfiguracionDatosEmpresaControlador() {
        this.configuracionDatosEmpresaServicio = ConfiguracionDatosEmpresaServicio.getInstance();
    }
    
    public ConfiguracionDatosEmpresa obtenerDatosEmpresa() {
        try {
            return this.configuracionDatosEmpresaServicio.getDatosEmpresa();
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
        
        return null;
    }
    
    public void modificarDatosEmpresa(ConfiguracionDatosEmpresa configuracionDatosEmpresa) {
        try {
            this.configuracionDatosEmpresaServicio.modificarDatosEmpresa(configuracionDatosEmpresa);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }
    
}
