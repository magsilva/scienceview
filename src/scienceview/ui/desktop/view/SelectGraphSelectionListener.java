/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view;

import gnu.trove.list.array.TIntArrayList;
import scienceview.datarepresentation.graph.TemporalGraph;
import scienceview.datarepresentation.graph.Vertex;

import java.util.ArrayList;

/**
 *
 * @author USER
 */
class SelectGraphSelectionListener extends VertexSelectionListener {

    /**
     * Creates a new instance of SelectGraphSelectionListener
     */
    public SelectGraphSelectionListener(TemporalProjectionViewer panel) {
        super(panel);
        this.color = java.awt.Color.BLUE;
    }

    @Override
    public void vertexSelected(TemporalGraph graph, Object param, TIntArrayList vertex) {
        ArrayList<Vertex> aux = new ArrayList<>();
        for (int i = 0; i < vertex.size(); i++) {
            aux.add(graph.getVertexById(vertex.get(i)));
        }
        panel.selectVertices(aux);
    }
}
