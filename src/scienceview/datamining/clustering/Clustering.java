package scienceview.datamining.clustering;

import gnu.trove.list.array.TIntArrayList;
import scienceview.utils.distance.Dissimilarity;
import scienceview.utils.matrix.SparseMatrix;

import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class Clustering {

    public Clustering(int nrclusters) {
        this.nrclusters = nrclusters;
    }

    public abstract ArrayList<TIntArrayList> execute(Dissimilarity diss, SparseMatrix matrix) throws IOException;

    protected int nrclusters;
}
