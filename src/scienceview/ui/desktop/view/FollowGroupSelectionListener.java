/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view;

import gnu.trove.list.array.TIntArrayList;
import scienceview.datarepresentation.graph.TemporalGraph;

/**
 *
 * @author Aretha
 */
public class FollowGroupSelectionListener extends VertexSelectionListener {

    /** Creates a new instance of ViewContentSelectionListener */
    public FollowGroupSelectionListener(TemporalProjectionViewer panel) {
        super(panel);
        this.color = java.awt.Color.DARK_GRAY;
    }

    @Override
    public void vertexSelected(TemporalGraph graph, Object param, TIntArrayList vertex) {
        TemporalProjectionViewer viewer = panel;
        viewer.cleanSelection(false);
        viewer.selectVertices();
    }
}
