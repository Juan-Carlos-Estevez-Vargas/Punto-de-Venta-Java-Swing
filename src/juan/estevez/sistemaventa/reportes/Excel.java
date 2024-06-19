package juan.estevez.sistemaventa.reportes;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import juan.estevez.sistemaventa.reportes.builders.ExcelBuilder;
import juan.estevez.sistemaventa.utils.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase que genera reportes en formato Excel y PDF. Se genera un reporte de productos con informaci�n de la base de datos. Los reportes se guardan en el directorio de documentos del usuario y se abren autom�ticamente. Utiliza las bibliotecas Apache POI y iTextPDF. Requiere la clase de conexi�n a la base de datos "Conexion".
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Excel {

    private static final String SELECT_PRODUCTOS_SQL = "SELECT CODIGO, DESCRIPCION, PRECIO, STOCK FROM PRODUCTO";

    /**
     * Genera un reporte en formato Excel con informaci�n de productos y lo guarda en un archivo.El reporte incluye el c�digo, descripci�n, precio y stock de los productos.El archivo generado se guarda en la carpeta "Documents" del directorio de inicio del usuario. Se utiliza la biblioteca Apache POI para manipular el archivo Excel. Se muestra una ventana de di�logo con un mensaje de confirmaci�n despu�s de generar el reporte. Si ocurren errores durante la generaci�n del reporte, se registran en el registro de eventos.
     *
     */
    public static void generarReporte() {
        try (Connection con = Conexion.conectar(); Workbook workbook = new XSSFWorkbook(); PreparedStatement ps = con.prepareStatement(SELECT_PRODUCTOS_SQL); ResultSet rs = ps.executeQuery()) {

            Sheet sheet = workbook.createSheet("Productos");
            ExcelBuilder builder = new ExcelBuilder(workbook, sheet);

            builder.createTitleRow("Reporte de Productos", 1, 2, 1, 3)
                    .createHeaderRow(new String[]{"Código", "Nombre", "Precio", "Existencia"}, 4)
                    .populateData(rs, 5)
                    .autosizeColumns(4);

            Path filePath = Paths.get(System.getProperty("user.home"), "Documents/productos.xlsx");

            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
            }

            Desktop.getDesktop().open(filePath.toFile());
            JOptionPane.showMessageDialog(null, "Reporte generado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, "Error al generar el reporte", ex);
        }
    }

}
