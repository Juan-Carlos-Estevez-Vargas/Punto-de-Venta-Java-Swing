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
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import juan.estevez.sistemaventa.daos.VentaDAO;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class ReporteVentaPDF {

    VentaDAO ventaDao = new VentaDAO();

    /**
     * Genera reporte en pdf de la venta realizada.
     *
     * @param txtRutEmpresa
     * @param txtNombreEmpresa
     * @param txtTelefonoEmpresa
     * @param txtDireccionEmpresa
     * @param txtRazonSocialEmpresa
     * @param txtDniRutVenta
     * @param txtNombreClienteVenta
     * @param txtTelefonoClienteVenta
     * @param txtDireccionClienteVenta
     * @param tableVenta
     * @param totalPagar
     */
    public void pdf(String txtRutEmpresa, String txtNombreEmpresa, String txtTelefonoEmpresa,
            String txtDireccionEmpresa, String txtRazonSocialEmpresa, String txtDniRutVenta,
            String txtNombreClienteVenta, String txtTelefonoClienteVenta, String txtDireccionClienteVenta,
            JTable tableVenta, double totalPagar) {

        try {
            int idVenta = ventaDao.idVenta();
            FileOutputStream archivo;
            File file = new File("src/juan/estevez/sistemaventa/reportes/reporteVenta" + idVenta + ".pdf");
            archivo = new FileOutputStream(file);

            Document documento = new Document();
            PdfWriter.getInstance(documento, archivo);
            documento.open();
            Image img = Image.getInstance("src/juan/estevez/sistemaventa/img/logo_pdf.png");

            Paragraph fecha = new Paragraph();
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            fecha.add(Chunk.NEWLINE);
            Date date = new Date();
            fecha.add("Factura N" + idVenta + "\n" + "Fecha: " + new SimpleDateFormat("dd-MM-yyyy").format(date) + "\n\n");

            PdfPTable encabezado = new PdfPTable(4);
            encabezado.setWidthPercentage(100);
            encabezado.getDefaultCell().setBorder(0);
            float[] columnaEncabezado = new float[]{20f, 40f, 70f, 40f};
            encabezado.setWidths(columnaEncabezado);
            encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
            encabezado.addCell(img);
            String Rut = txtRutEmpresa;
            String nombre = txtNombreEmpresa;
            String telefono = txtTelefonoEmpresa;
            String direccion = txtDireccionEmpresa;
            String razpnSocial = txtRazonSocialEmpresa;
            encabezado.addCell("");
            encabezado.addCell("Rut: " + Rut + "\nNombre: " + nombre + "\nTeléfono: " + telefono + "\nDirección: " + direccion + "\nRazón Social: " + razpnSocial);
            encabezado.addCell(fecha);
            documento.add(encabezado);

            Paragraph paragraphCliente = new Paragraph();
            paragraphCliente.add(Chunk.NEWLINE);
            paragraphCliente.add("Datos de los clientes \n\n");
            documento.add(paragraphCliente);

            PdfPTable tablaClientes = new PdfPTable(4);
            tablaClientes.setWidthPercentage(100);
            tablaClientes.getDefaultCell().setBorder(0);
            float[] columnaCliente = new float[]{20f, 50f, 30f, 40f};
            tablaClientes.setWidths(columnaCliente);
            tablaClientes.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell celdaRutCliente = new PdfPCell(new Phrase("DNI/RUT", negrita));
            PdfPCell celdaNombreCliente = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell celdaTelefonoCliente = new PdfPCell(new Phrase("Teléfono", negrita));
            PdfPCell celdaDireccionCliente = new PdfPCell(new Phrase("Dirección", negrita));

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
            PdfPCell celdaDescripciónProducto = new PdfPCell(new Phrase("Descripción", negrita));
            PdfPCell celdaPrecioUnitario = new PdfPCell(new Phrase("Precio U.", negrita));
            PdfPCell celdaPrecioTotal = new PdfPCell(new Phrase("Precio T.", negrita));

            celdaCantidadProducto.setBorder(0);
            celdaDescripciónProducto.setBorder(0);
            celdaPrecioUnitario.setBorder(0);
            celdaPrecioTotal.setBorder(0);

            celdaCantidadProducto.setBackgroundColor(BaseColor.GRAY);
            celdaDescripciónProducto.setBackgroundColor(BaseColor.GRAY);
            celdaPrecioUnitario.setBackgroundColor(BaseColor.GRAY);
            celdaPrecioTotal.setBackgroundColor(BaseColor.GRAY);

            tablaProductos.addCell(celdaCantidadProducto);
            tablaProductos.addCell(celdaDescripciónProducto);
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
            info.add("Total a Pagar " + totalPagar);
            info.setAlignment(Element.ALIGN_RIGHT);
            documento.add(info);

            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.add("Cancelación y Firma\n\n");
            firma.add("_______________________");
            firma.setAlignment(Element.ALIGN_CENTER);
            documento.add(firma);

            Paragraph mensaje = new Paragraph();
            mensaje.add(Chunk.NEWLINE);
            mensaje.add("Gracias por su compra");
            mensaje.setAlignment(Element.ALIGN_CENTER);
            documento.add(mensaje);

            documento.close();
            archivo.close();
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            System.err.println("Error al generar reporte de la venta en PDf " + e.toString());
        }
    }
}
