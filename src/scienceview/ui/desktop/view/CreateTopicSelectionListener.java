/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.ui.desktop.view;

import gnu.trove.list.array.TIntArrayList;
import scienceview.datamining.topicmodeling.Topic;
import scienceview.datamining.topicmodeling.TopicFactory;
import scienceview.datamining.topicmodeling.TopicNone;
import scienceview.datarepresentation.graph.TemporalGraph;
import scienceview.projection.temporal.TemporalProjection;

/**
 *
 * @author Aretha
 */
public class CreateTopicSelectionListener extends VertexSelectionListener {

    private Topic topic = null;

    public CreateTopicSelectionListener(TemporalProjectionViewer panel) {
        super(panel);
        this.color = java.awt.Color.YELLOW;
    }

    @Override
    public void vertexSelected(TemporalGraph graph, Object param, TIntArrayList vertex) {
        if (vertex.size() > 0) {
            TemporalProjection projection = panel.getTemporalProjection();
            if (graph == null) {
                graph = panel.getGraph();
            }
            this.topic = TopicFactory.getInstance(projection, graph, vertex);
            if (! (this.topic instanceof TopicNone)) {
            	this.topic.calcPolygon();
            	this.topic.createTopic();
            	graph.addTopic(topic);
            	ScienceViewMainFrame.getInstance().updateTopicsTree();
            	panel.updateImage();
            }
        }
    }

    public Topic getLastTopic() {
        return this.topic;
    }
}
