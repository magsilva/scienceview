/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.utils.stress;

import java.util.TreeMap;
import org.jfree.data.xy.XYSeriesCollection;

import scienceview.datarepresentation.graph.TemporalGraph;
import scienceview.datarepresentation.graph.Vertex;
import scienceview.utils.distance.Dissimilarity;
import scienceview.utils.matrix.SparseMatrix;

/**
 *
 * @author barbosaa
 */
public abstract class Stress {

    public abstract XYSeriesCollection calculate(SparseMatrix matrix, Dissimilarity diss, TreeMap<Integer, TemporalGraph> graphs);

    public float euclideanDistance(Vertex v1, Vertex v2) {
        return (float) Math.sqrt( Math.pow(v1.getX() - v2.getX(), 2) +  Math.pow(v1.getY() - v2.getY(), 2));
    }

}
