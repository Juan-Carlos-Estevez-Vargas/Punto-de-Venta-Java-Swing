package juan.estevez.sistemaventa.reportes;

import com.itextpdf.text.DocumentException;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import juan.estevez.sistemaventa.reportes.builders.PdfReportBuilder;

/**
 * Clase para generar un reporte en PDF de la venta realizada. Requiere la librer�a iTextPDF en el classpath.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class ReporteVentaPDF {

    /**
     * Genera y abre un reporte de venta en formato PDF.
     *
     * @param txtRutEmpresa el RUT de la empresa.
     * @param txtNombreEmpresa el nombre de la empresa.
     * @param txtTelefonoEmpresa el tel�fono de la empresa.
     * @param txtDireccionEmpresa la direcci�n de la empresa.
     * @param txtRazonSocialEmpresa la raz�n social de la empresa.
     * @param txtDniRutVenta el DNI/RUT del cliente de la venta.
     * @param txtNombreClienteVenta el nombre del cliente de la venta.
     * @param txtTelefonoClienteVenta el tel�fono del cliente de la venta.
     * @param txtDireccionClienteVenta la direcci�n del cliente de la venta.
     * @param tableVenta la tabla que contiene los detalles de los productos vendidos.
     * @param totalPagar el total a pagar de la venta.
     * @throws DocumentException si ocurre un error al generar el documento PDF.
     * @throws IOException si ocurre un error al acceder al archivo PDF.
     * @throws SQLException
     */
    public void generarReporteVenta(String txtRutEmpresa, String txtNombreEmpresa, String txtTelefonoEmpresa,
            String txtDireccionEmpresa, String txtRazonSocialEmpresa, String txtDniRutVenta,
            String txtNombreClienteVenta, String txtTelefonoClienteVenta, String txtDireccionClienteVenta,
            JTable tableVenta, double totalPagar) throws DocumentException, IOException, SQLException {

        JFileChooser fc = new JFileChooser();
        int response = fc.showSaveDialog(tableVenta);

        if (response == JFileChooser.APPROVE_OPTION) {
            File chosenFile = fc.getSelectedFile();
            try (FileOutputStream archivo = new FileOutputStream(chosenFile)) {
                PdfReportBuilder builder = new PdfReportBuilder(archivo)
                        .addEncabezado(txtRutEmpresa, txtNombreEmpresa, txtTelefonoEmpresa, txtDireccionEmpresa, txtRazonSocialEmpresa)
                        .addDatosCliente(txtDniRutVenta, txtNombreClienteVenta, txtTelefonoClienteVenta, txtDireccionClienteVenta)
                        .addProductos(tableVenta)
                        .addTotalPagar(totalPagar)
                        .addFirma()
                        .addMensaje();
                builder.build();
            }
            Desktop.getDesktop().open(chosenFile);
        }
    }

}
