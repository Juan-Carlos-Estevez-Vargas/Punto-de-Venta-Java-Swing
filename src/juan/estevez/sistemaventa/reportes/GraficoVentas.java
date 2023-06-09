package juan.estevez.sistemaventa.reportes;

import java.sql.*;
import juan.estevez.sistemaventa.utils.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase para generar gr�ficos de ventas utilizando la librer�a JFreeChart. Los
 * gr�ficos se generan a partir de los datos de ventas filtrados por fecha.
 * Requiere la librer�a JFreeChart en el classpath.
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class GraficoVentas {

    /**
     * Genera y muestra un gr�fico de torta a partir de los datos de ventas
     * filtrados por fecha.
     *
     * @param fecha la fecha utilizada para filtrar los datos de ventas.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public static void graficar(String fecha) throws SQLException {
        String sql = "SELECT TOTAL FROM VENTAS WHERE FECHA = ?";

        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setString(1, fecha);
            try (ResultSet rs = pst.executeQuery()) {
                DefaultPieDataset dataset = new DefaultPieDataset();

                while (rs.next()) {
                    dataset.setValue(rs.getString("TOTAL"), rs.getDouble("TOTAL"));
                }

                JFreeChart chart = ChartFactory.createPieChart("Reporte de Venta", dataset);
                ChartFrame frame = new ChartFrame("Total de ventas por d�a", chart);
                frame.setSize(1000, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al realizar el gr�fico de ventas", e);
        }
    }
}
