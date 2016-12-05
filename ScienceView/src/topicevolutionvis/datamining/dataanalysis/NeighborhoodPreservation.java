/*
 * Copyright 2015 Aretha.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package topicevolutionvis.datamining.dataanalysis;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeriesCollection;
import topicevolutionvis.graph.TemporalGraph;
import topicevolutionvis.matrix.SparseMatrix;
import topicevolutionvis.projection.distance.DistanceMatrix;
import topicevolutionvis.projection.distance.Euclidean;
import topicevolutionvis.util.KNN;
import topicevolutionvis.util.Pair;
import topicevolutionvis.util.Utils;

/**
 *
 * @author Aretha
 */
public class NeighborhoodPreservation extends javax.swing.JDialog {

    private JFreeChart freechart;
    private ChartPanel panel;

    /**
     * Creates new form NeighborhoodPreservation
     */
    public NeighborhoodPreservation(javax.swing.JDialog parent) {
        super(parent);
        initComponents();
    }

    public static NeighborhoodPreservation getInstance(javax.swing.JDialog parent) {
        return new NeighborhoodPreservation(parent);
    }

    private JFreeChart createChart(XYDataset xydataset) {
        JFreeChart chart = ChartFactory.createXYLineChart("Neighborhood Preservation",
                "Number Neighbors", "Precision", xydataset, PlotOrientation.VERTICAL,
                true, true, false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot xyplot = (XYPlot) chart.getPlot();
        NumberAxis numberaxis = (NumberAxis) xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);

        xyplot.setDomainGridlinePaint(Color.BLACK);
        xyplot.setRangeGridlinePaint(Color.BLACK);

        xyplot.setOutlinePaint(Color.BLACK);
        xyplot.setOutlineStroke(new BasicStroke(1.0f));
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);

        xyplot.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA,
                    Color.CYAN, Color.ORANGE, Color.BLACK, Color.DARK_GRAY, Color.GRAY,
                    Color.LIGHT_GRAY, Color.YELLOW
                }, DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE));

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        xylineandshaperenderer.setBaseShapesFilled(true);
        xylineandshaperenderer.setDrawOutlines(true);

        return chart;
    }

     private XYSeriesCollection createAllSeries(Pair[][] ndata, final TreeMap<Integer, ArrayList<TemporalGraph>> graphs,
            int maxneigh) throws IOException {
        NeighborhoodPreservationEngine npe = new NeighborhoodPreservationEngine();
        YIntervalSeriesCollection mean_sd_collection = new YIntervalSeriesCollection();
        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        for (Entry<Integer, ArrayList<TemporalGraph>> entry : graphs.entrySet()) {
            TemporalGraph graph = entry.getValue().get(entry.getValue().size() - 1);
            System.out.println("Year "+graph.getYear());
            int n_vertex = graph.getVertex().keys().length;
            //MELHORAR ESSE CALCULO POSTERIORMENTE
            if (n_vertex > 2) {
                int neigh = maxneigh;
                if (n_vertex <= maxneigh) {
                    neigh = n_vertex - 1;
                }
                System.out.println("Nro de vizinhos: " + neigh);
                double[] values = npe.neighborhood(ndata, graph, neigh);
                XYSeries xyseries = this.createSerie("Year " + graph.getYear(), values);
                xyseriescollection.addSeries(xyseries);
            }
        }
//        Double mean[] = new Double[maxneigh];
//        Double standard_deviation[] = new Double[maxneigh];
//        int counter[] = new int[maxneigh];
//        Arrays.fill(counter, 0);
//        Arrays.fill(mean, 0);
//        Arrays.fill(standard_deviation, 0);
//        for (int i = 0; i < xyseriescollection.getSeriesCount(); i++) {
//            XYSeries serie = xyseriescollection.getSeries(i);
//            for (int j = 0; j < serie.getItemCount(); j++) {
//                Double y = (Double) serie.getY(j);
//                mean[j] = mean[j] + y;
//                counter[j] = counter[j] + 1;
//            }
//        }
//        for (int j = 0; j < mean.length; j++) {
//            mean[j] = mean[j] / counter[j];
//            //  standard_deviation[j] = Math.sqrt(j)
//            for (int i = 0; i < xyseriescollection.getSeriesCount(); i++) {
//                XYSeries serie = xyseriescollection.getSeries(i);
//                Double y = (Double) serie.getY(j);
//                if (y != null) {
//                    standard_deviation[j] = standard_deviation[j] + Math.pow(y - mean[j], 2);
//                }
//            }
//        }
//        for (int j = 0; j < standard_deviation.length; j++) {
//            standard_deviation[j] = Math.sqrt((1 * counter[j]) * standard_deviation[j]);
//        }
        return xyseriescollection;
    }

    public void createChart(final Pair[][] ndata, final TreeMap<Integer, ArrayList<TemporalGraph>> graphs, final int maxneigh) {

        try {
            freechart = createChart(createAllSeries(ndata, graphs, maxneigh));
            panel = new ChartPanel(freechart);
            dataPanel.add(panel, BorderLayout.CENTER);
            
            setPreferredSize(new Dimension(650, 400));
            setSize(new Dimension(650, 400));
            setLocationRelativeTo(getParent());
            this.setVisible(true);
            
        } catch (IOException ex) {
            Logger.getLogger(NeighborhoodPreservation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private XYSeries createSerie(String name, double[] values) {
        XYSeries xyseries = new XYSeries(name);

        for (int i = 0; i < values.length; i++) {
            xyseries.add(i + 1, values[i]);
        }

        return xyseries;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        dataPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Neighborhood Preservation");
        setMinimumSize(new java.awt.Dimension(163, 100));
        setModal(true);
        setPreferredSize(new java.awt.Dimension(163, 100));

        jPanel1.setMinimumSize(new java.awt.Dimension(250, 100));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel1.add(jScrollPane1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.LINE_START);

        dataPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dataPanel.setLayout(new java.awt.BorderLayout());
        getContentPane().add(dataPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setMinimumSize(new java.awt.Dimension(163, 33));
        buttonPanel.setPreferredSize(new java.awt.Dimension(163, 33));

        saveButton.setText("Save Image");
        buttonPanel.add(saveButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(closeButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    public static class Serie {

        public Serie(String name, String filename) {
            this.name = name;
            this.filename = filename;
        }

        public String name;
        public String filename;
    }

    public static class NeighborhoodPreservationEngine {

        public double[] neighborhood(Pair[][] ndata, TemporalGraph graph, int maxneigh) throws IOException {
            double[] values = new double[maxneigh];
            SparseMatrix proj = null;

            proj = Utils.exportProjection(graph);
            DistanceMatrix dmatproj = new DistanceMatrix(proj, new Euclidean());

            KNN knnproj = new KNN(maxneigh);
            Pair[][] nproj = knnproj.execute(dmatproj);

            for (int n = 0; n < maxneigh; n++) {
                float percentage = 0.0f;

                for (int i = 0; i < dmatproj.getElementCount(); i++) {
                    float total = 0.0f;

                    for (int j = 0; j < n + 1; j++) {
                        if (this.contains(nproj[i], n + 1, ndata[i][j].index)) {
                            total++;
                        }
                    }

                    percentage += total / (n + 1);
                }
                values[n] = percentage / dmatproj.getElementCount();
            }

            return values;
        }

        private boolean contains(Pair[] neighbors, int length, int index) {
            for (int i = 0; i < length; i++) {
                if (neighbors[i].index == index) {
                    return true;
                }
            }
            return false;
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
