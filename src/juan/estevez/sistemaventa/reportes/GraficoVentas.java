package juan.estevez.sistemaventa.reportes;

import java.sql.*;
import juan.estevez.sistemaventa.modelo.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class GraficoVentas {

    /**
     * Grafica el gráfico de torta usando la librería externa JFree.
     *
     * @param fecha con la cuál se filtrará la información a graficar.
     */
    public static void graficar(String fecha) {

        Connection cn;
        PreparedStatement pst;
        ResultSet rs;

        try {
            String sql = "SELECT TOTAL FROM VENTAS WHERE FECHA = ?";
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, fecha);
            rs = pst.executeQuery();
            DefaultPieDataset dataset = new DefaultPieDataset();

            while (rs.next()) {
                dataset.setValue(rs.getString("TOTAL"), rs.getDouble("TOTAL"));
            }

            JFreeChart jfree = ChartFactory.createPieChart("Reporte de Venta", dataset);
            ChartFrame cf = new ChartFrame("Total de ventas por día", jfree);
            cf.setSize(1000, 500);
            cf.setLocationRelativeTo(null);
            cf.setVisible(true);
        } catch (SQLException e) {
            System.err.println("Error al realizar el gráfico de ventas. " + e.toString());
        }
    }
}
