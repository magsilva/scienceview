package scienceview.datamining.topicmodeling;


import gnu.trove.list.array.TIntArrayList;
import scienceview.datarepresentation.graph.TemporalGraph;
import scienceview.projection.temporal.TemporalProjection;

public class TopicNone extends Topic {

	public TopicNone(TIntArrayList vertex, TemporalProjection tprojection,
			TemporalGraph graph) {
		super(vertex, tprojection, graph);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createTopic() {
		// TODO Auto-generated method stub

	}

}
