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
package scienceview.ui.desktop.view;

import flanagan.io.FileTypeFilter;
import scienceview.datamining.clustering.KNN;
import scienceview.datarepresentation.graph.TemporalGraph;
import scienceview.utils.Utils;
import scienceview.utils.datastructures.Pair;
import scienceview.utils.distance.DistanceMatrix;
import scienceview.utils.distance.Euclidean;
import scienceview.utils.matrix.SparseMatrix;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StatisticalLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeriesCollection;

/**
 *
 * @author Aretha
 */
public class NeighborhoodPreservation extends javax.swing.JDialog implements ListSelectionListener {

    private JFreeChart freechart;
    private JFreeChart freechart2;
    private ChartPanel panel;
    private ChartPanel panel2;
    private XYSeriesCollection xyseriescollection;
    private XYSeriesCollection backup_xyseriescollection;
    private List listaControle;
    double mean[];
    double standard_deviation[];

    DefaultListModel lista = new DefaultListModel();

    /**
     * Creates new form NeighborhoodPreservation
     */
    public NeighborhoodPreservation(javax.swing.JDialog parent) {
        super(parent);
        initComponents();

        ListSelectionModel lsm;
        lsm = jList1.getSelectionModel();
        lsm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lsm.addListSelectionListener(this);

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
        xyseriescollection = new XYSeriesCollection();
        this.backup_xyseriescollection = new XYSeriesCollection();
        for (Entry<Integer, ArrayList<TemporalGraph>> entry : graphs.entrySet()) {
            TemporalGraph graph = entry.getValue().get(entry.getValue().size() - 1);
            int n_vertex = graph.getVertex().keys().length;
            // TODO: improve calculation
            if (n_vertex > 2) {
                int neigh = maxneigh;
                if (n_vertex <= maxneigh) {
                    neigh = n_vertex - 1;
                }
                System.out.println("Nro de vizinhos: " + neigh);
                double[] values = npe.neighborhood(ndata, graph, neigh);
                XYSeries xyseries = this.createSerie("" + graph.getYear(), values);
                xyseriescollection.addSeries(xyseries);
                this.backup_xyseriescollection.addSeries(xyseries);

                String ano = ""  + graph.getYear();
                lista.addElement(ano);
                this.jList1.setModel(lista);

                int lastIndex = lista.size() - 1;
                this.jList1.getSelectionModel().setSelectionInterval(0, lastIndex);
            }
        }
        mean = new double[maxneigh];
        standard_deviation = new double[maxneigh];
        int counter[] = new int[maxneigh];
        Arrays.fill(counter, 0);
        Arrays.fill(mean, 0);
        Arrays.fill(standard_deviation, 0);
//        for (int i = 0; i < xyseriescollection.getSeriesCount(); i++) {
//            XYSeries serie = xyseriescollection.getSeries(i);
//            for (int j = 0; j < serie.getItemCount(); j++) {
//                double y = (Double) serie.getY(j);
//                mean[j] = mean[j] + y;
//                counter[j] = counter[j] + 1;
//                if (j == 0) {
//                    System.out.println(y);
//                }
//            }
//
//        }
         for (int i = 0; i < xyseriescollection.getSeriesCount(); i++) {
             XYSeries serie = xyseriescollection.getSeries(i);
             
            for(int j=0; j< serie.getItemCount();j++){
                XYDataItem item =serie.getDataItem(j);
                int x = (int)item.getXValue()-1;
                double y = item.getYValue();
                mean[x] = mean[x] + y;
                counter[x] = counter[x] + 1;
            }
         }
             
         
        
        
        for (int i = 0; i < maxneigh; i++) {
            mean[i] = mean[i] / counter[i];
        }

        for (int i = 0; i < xyseriescollection.getSeriesCount(); i++) {
            XYSeries serie = xyseriescollection.getSeries(i);
            for (int a = 0; a < serie.getItemCount(); a++) {
                Double y = (Double) serie.getY(a);
                standard_deviation[a] = standard_deviation[a] + Math.pow(y - mean[a], 2);

            }
        }

        for (int j = 0; j < standard_deviation.length; j++) {
            standard_deviation[j] = Math.sqrt(standard_deviation[j] / (counter[j] - 1));
             System.out.println("Average ["+ j+"]:" + mean[j]);
             System.out.println("Standard deviation: ["+ j+"]:" + standard_deviation[j]);
             System.out.println("");
        }
       
        return xyseriescollection;
    }

    private CategoryDataset createDataset(double mean[], double standard_deviation[]) {
        
        DefaultStatisticalCategoryDataset dataset = new DefaultStatisticalCategoryDataset();
   
        for (int i = 0; i < mean.length ; i++) {
            dataset.add(mean[i], standard_deviation[i], "Average and standard deviation", Integer.toString(i+1));           
        }
        return dataset;

    }

    private JFreeChart createStatistical(CategoryDataset dataset) {
        
        JFreeChart chart2= ChartFactory.createLineChart(
                "Statistical Line Chart Demo 1", // chart title
                "Type", // domain axis label
                "Value", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips
                false // urls

        );
        
        CategoryPlot plot = (CategoryPlot) chart2.getPlot();
        plot.setRangePannable(true);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setUpperMargin(0.0);
        domainAxis.setLowerMargin(0.0);

                 
        // customise the range axis...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
       // rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);
        
        
        StatisticalLineAndShapeRenderer renderer = new StatisticalLineAndShapeRenderer(true, false);
        renderer.setUseSeriesOffset(true);
        plot.setRenderer(renderer);
        
        
        
        
        return chart2;
    }

    public void createChartStatistical(double mean[], double standard_deviation[]) {
       
        freechart2 = createStatistical(createDataset(mean, standard_deviation));
        panel2 = new ChartPanel(freechart2);
        JPMedia.add(panel2, BorderLayout.CENTER);
        
        panel2.setPreferredSize(new Dimension(600, 300));
        setSize(new Dimension(650, 400));
        setLocationRelativeTo(getParent());
       // panel2.setMouseWheelEnabled(true);
       // setContentPane(panel2);
        this.setVisible(true);       
     
      
        

    }

    public void createChart(final Pair[][] ndata, final TreeMap<Integer, ArrayList<TemporalGraph>> graphs, final int maxneigh) {

        try {
            freechart = createChart(createAllSeries(ndata, graphs, maxneigh));
            panel = new ChartPanel(freechart);
            dataPanel.add(panel, BorderLayout.CENTER);

            setPreferredSize(new Dimension(650, 400));
            setSize(new Dimension(650, 400));
            setLocationRelativeTo(getParent());
           
            this.createChartStatistical(mean, standard_deviation);
           // this.setVisible(true);
            
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        aba1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        dataPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        SelectAll = new javax.swing.JCheckBox();
        buttonPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        aba2 = new javax.swing.JPanel();
        JPMedia = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Neighborhood Preservation");
        setMinimumSize(new java.awt.Dimension(163, 100));
        setModal(true);

        aba1.setLayout(new java.awt.BorderLayout());

        jPanel1.setMinimumSize(new java.awt.Dimension(250, 100));
        aba1.add(jPanel1, java.awt.BorderLayout.LINE_START);

        dataPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        dataPanel.setMinimumSize(new java.awt.Dimension(25, 25));
        dataPanel.setLayout(new java.awt.BorderLayout());

        jList1.setMaximumSize(new java.awt.Dimension(5, 25));
        jList1.setMinimumSize(new java.awt.Dimension(5, 25));
        jScrollPane1.setViewportView(jList1);

        dataPanel.add(jScrollPane1, java.awt.BorderLayout.LINE_START);

        SelectAll.setSelected(true);
        SelectAll.setText("Select All");
        SelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectAllActionPerformed(evt);
            }
        });
        dataPanel.add(SelectAll, java.awt.BorderLayout.PAGE_START);

        aba1.add(dataPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setMinimumSize(new java.awt.Dimension(163, 33));
        buttonPanel.setPreferredSize(new java.awt.Dimension(163, 33));

        saveButton.setText("Save Image");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(saveButton);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(closeButton);

        aba1.add(buttonPanel, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("All Curves", aba1);

        aba2.setMinimumSize(new java.awt.Dimension(275, 133));
        aba2.setPreferredSize(new java.awt.Dimension(270, 188));
        aba2.setLayout(new java.awt.BorderLayout());

        JPMedia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        JPMedia.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        JPMedia.setMinimumSize(new java.awt.Dimension(25, 25));
        JPMedia.setPreferredSize(new java.awt.Dimension(260, 155));
        JPMedia.setLayout(new java.awt.BorderLayout());
        aba2.add(JPMedia, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Resume", aba2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser("c:\\");
        // fc.setFileFilter(new FileTypeFilter("jpg","Text File"));
        // FileFilter filtro = new FileNameExtensionFilter ( "jpg", "jpeg");
        fc.addChoosableFileFilter(new FileNameExtensionFilter("JPEG image (*.jpg)", "jpg"));
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(fc.getChoosableFileFilters()[0]);
        if (evt.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    boolean test = file.getCanonicalPath().endsWith("jpg");
                    if (!test) {
                        file = new File(file.getCanonicalPath() + "." + ((FileNameExtensionFilter) fc.getFileFilter()).getExtensions()[0]);

                    }

                    //This is where a real application would save the file.
                    ChartUtilities.saveChartAsPNG(file, freechart, 1000, 500);

                } catch (IOException ex) {
                    Logger.getLogger(NeighborhoodPreservation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }//GEN-LAST:event_saveButtonActionPerformed
    }
    private void SelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectAllActionPerformed

        int lastIndex = lista.size() - 1;
        this.jList1.getSelectionModel().setSelectionInterval(0, lastIndex);
        if (!SelectAll.isSelected()) {
            this.jList1.getSelectionModel().clearSelection();

        }
        //        for (int i = 0; i < lastIndex; i++) {
        //            xyseriescollection.addSeries(backup_xyseriescollection.getSeries(i));
        //
        //        }
    }//GEN-LAST:event_SelectAllActionPerformed

    @Override
    public void valueChanged(ListSelectionEvent e) {

        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        boolean isAdjusting = e.getValueIsAdjusting();
        if (!isAdjusting) {
//                System.out.println("Event for indexes "
//                        + firstIndex + "-" + lastIndex
//                        + ";isAdjusting is " + isAdjusting
//                        + ";selected indexes: \n");

            if (lsm.isSelectionEmpty()) {
                //  System.out.println(" <none>");
                xyseriescollection.removeAllSeries();

            } else {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                xyseriescollection.removeAllSeries();
                int quant = 0;
                for (int i = minIndex; i <= maxIndex; i++) {
                    SelectAll.setSelected(false);
                    if (lsm.isSelectedIndex(i)) {
                        // System.out.println(" " + i);
                        //System.out.println(lista.get(i));

                        //  System.out.println(backup_xyseriescollection.getSeries(i));
                        xyseriescollection.addSeries(backup_xyseriescollection.getSeries(i));
                        quant++;

                    }

                }
                if (quant == (lista.size())) {
                    SelectAll.setSelected(true);
                }
            }
        }

    }

    //
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
    private javax.swing.JPanel JPMedia;
    private javax.swing.JCheckBox SelectAll;
    private javax.swing.JPanel aba1;
    private javax.swing.JPanel aba2;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}
