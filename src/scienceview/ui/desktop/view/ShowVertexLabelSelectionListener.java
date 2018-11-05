/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view;

import gnu.trove.list.array.TIntArrayList;
import scienceview.datarepresentation.graph.TemporalGraph;

/**
 *
 * @author barbosaa
 */
public class ShowVertexLabelSelectionListener extends VertexSelectionListener {

    public ShowVertexLabelSelectionListener(TemporalProjectionViewer panel) {
        super(panel);
        this.color = java.awt.Color.BLUE;
    }

    @Override
    public void vertexSelected(TemporalGraph graph, Object param, TIntArrayList vertex) {
        for (int i = 0; i < vertex.size(); i++) {
            graph.getVertexById(vertex.get(i)).setShowLabel(true);
        }
    }
}
