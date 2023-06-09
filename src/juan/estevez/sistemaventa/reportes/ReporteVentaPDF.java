package juan.estevez.sistemaventa.reportes;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import juan.estevez.sistemaventa.daos.VentaDAO;

/**
 * Clase para generar un reporte en PDF de la venta realizada. Requiere la
 * librer�a iTextPDF en el classpath.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class ReporteVentaPDF {

    private final VentaDAO ventaDao = new VentaDAO();

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
     * @param tableVenta la tabla que contiene los detalles de los productos
     * vendidos.
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
                Document documento = new Document();
                PdfWriter.getInstance(documento, archivo);
                
                documento.open();
                Image img = Image.getInstance("src/juan/estevez/sistemaventa/img/logo.png");
                
                Paragraph fecha = new Paragraph();
                Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
                fecha.add(Chunk.NEWLINE);
                Date date = new Date();
                fecha.add("Factura N�" + ventaDao.obtenerIdVenta() + "\n" + "Fecha: "
                        + new SimpleDateFormat("dd-MM-yyyy").format(date) + "\n\n");
                
                PdfPTable encabezado = new PdfPTable(4);
                encabezado.setWidthPercentage(100);
                encabezado.getDefaultCell().setBorder(0);
                float[] columnaEncabezado = new float[]{20f, 40f, 70f, 40f};
                encabezado.setWidths(columnaEncabezado);
                encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
                encabezado.addCell(img);
                encabezado.addCell("");
                encabezado.addCell(
                        "Rut: " + txtRutEmpresa + "\nNombre: " + txtNombreEmpresa + "\nTel�fono: " + txtTelefonoEmpresa
                                + "\nDirecci�n: " + txtDireccionEmpresa + "\nRaz�n Social: " + txtRazonSocialEmpresa);
                encabezado.addCell(fecha);
                documento.add(encabezado);
                
                Paragraph paragraphCliente = new Paragraph();
                paragraphCliente.add(Chunk.NEWLINE);
                paragraphCliente.add("Datos del cliente \n\n");
                documento.add(paragraphCliente);
                
                PdfPTable tablaClientes = new PdfPTable(4);
                tablaClientes.setWidthPercentage(100);
                tablaClientes.getDefaultCell().setBorder(0);
                float[] columnaCliente = new float[]{20f, 50f, 30f, 40f};
                tablaClientes.setWidths(columnaCliente);
                tablaClientes.setHorizontalAlignment(Element.ALIGN_LEFT);
                
                PdfPCell celdaRutCliente = new PdfPCell(new Phrase("DNI/RUT", negrita));
                PdfPCell celdaNombreCliente = new PdfPCell(new Phrase("Nombre", negrita));
                PdfPCell celdaTelefonoCliente = new PdfPCell(new Phrase("Tel�fono", negrita));
                PdfPCell celdaDireccionCliente = new PdfPCell(new Phrase("Direcci�n", negrita));
                
                celdaRutCliente.setBorder(0);
                celdaNombreCliente.setBorder(0);
                celdaTelefonoCliente.setBorder(0);
                celdaDireccionCliente.setBorder(0);
                
                tablaClientes.addCell(celdaRutCliente);
                tablaClientes.addCell(celdaNombreCliente);
                tablaClientes.addCell(celdaTelefonoCliente);
                tablaClientes.addCell(celdaDireccionCliente);
                
                tablaClientes.addCell(txtDniRutVenta);
                tablaClientes.addCell(txtNombreClienteVenta);
                tablaClientes.addCell(txtTelefonoClienteVenta);
                tablaClientes.addCell(txtDireccionClienteVenta);
                documento.add(tablaClientes);
                
                // Productos
                PdfPTable tablaProductos = new PdfPTable(4);
                tablaProductos.setWidthPercentage(100);
                tablaProductos.getDefaultCell().setBorder(0);
                float[] columnaProducto = new float[]{20f, 50f, 25f, 30f};
                tablaProductos.setWidths(columnaProducto);
                tablaProductos.setHorizontalAlignment(Element.ALIGN_LEFT);
                
                PdfPCell celdaCantidadProducto = new PdfPCell(new Phrase("Cantidad", negrita));
                PdfPCell celdaDescripcionProducto = new PdfPCell(new Phrase("Descripci�n", negrita));
                PdfPCell celdaPrecioUnitario = new PdfPCell(new Phrase("Precio U.", negrita));
                PdfPCell celdaPrecioTotal = new PdfPCell(new Phrase("Precio T.", negrita));
                
                celdaCantidadProducto.setBorder(0);
                celdaDescripcionProducto.setBorder(0);
                celdaPrecioUnitario.setBorder(0);
                celdaPrecioTotal.setBorder(0);
                
                celdaCantidadProducto.setBackgroundColor(BaseColor.GRAY);
                celdaDescripcionProducto.setBackgroundColor(BaseColor.GRAY);
                celdaPrecioUnitario.setBackgroundColor(BaseColor.GRAY);
                celdaPrecioTotal.setBackgroundColor(BaseColor.GRAY);
                
                tablaProductos.addCell(celdaCantidadProducto);
                tablaProductos.addCell(celdaDescripcionProducto);
                tablaProductos.addCell(celdaPrecioUnitario);
                tablaProductos.addCell(celdaPrecioTotal);
                
                for (int i = 0; i < tableVenta.getRowCount(); i++) {
                    tablaProductos.addCell(tableVenta.getValueAt(i, 2).toString());
                    tablaProductos.addCell(tableVenta.getValueAt(i, 1).toString());
                    tablaProductos.addCell(tableVenta.getValueAt(i, 3).toString());
                    tablaProductos.addCell(tableVenta.getValueAt(i, 4).toString());
                }
                
                documento.add(tablaProductos);
                
                Paragraph info = new Paragraph();
                info.add(Chunk.NEWLINE);
                info.add("Total a Pagar $" + totalPagar);
                info.setAlignment(Element.ALIGN_RIGHT);
                documento.add(info);
                
                Paragraph firma = new Paragraph();
                firma.add(Chunk.NEWLINE);
                firma.add("Cancelaci�n y Firma\n\n");
                firma.add("_______________________");
                firma.setAlignment(Element.ALIGN_CENTER);
                documento.add(firma);
                
                Paragraph mensaje = new Paragraph();
                mensaje.add(Chunk.NEWLINE);
                mensaje.add("Gracias por su compra");
                mensaje.setAlignment(Element.ALIGN_CENTER);
                documento.add(mensaje);
                
                documento.close();
            }
            Desktop.getDesktop().open(chosenFile);
        }
    }
}
