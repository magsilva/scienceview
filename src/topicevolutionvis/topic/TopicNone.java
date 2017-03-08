package topicevolutionvis.topic;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;

import gnu.trove.list.array.TIntArrayList;
import topicevolutionvis.graph.TemporalGraph;
import topicevolutionvis.projection.temporal.TemporalProjection;

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
