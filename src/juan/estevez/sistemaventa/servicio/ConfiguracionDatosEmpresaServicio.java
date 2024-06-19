package juan.estevez.sistemaventa.servicio;

import java.sql.SQLException;
import juan.estevez.sistemaventa.daos.ConfiguracionDatosEmpresaDAO;
import juan.estevez.sistemaventa.modelo.ConfiguracionDatosEmpresa;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresaServicio {
    
    private final ConfiguracionDatosEmpresaDAO configuracionDatosEmpresaDAO = new ConfiguracionDatosEmpresaDAO();
    
    public ConfiguracionDatosEmpresa getDatosEmpresa() throws SQLException {
        return this.configuracionDatosEmpresaDAO.buscarDatosEmpresa();
    }
    
    public void modificarDatosEmpresa(ConfiguracionDatosEmpresa datosEmpresa) throws SQLException {
        this.configuracionDatosEmpresaDAO.modificarDatosEmpresa(datosEmpresa);
    }
    
}
