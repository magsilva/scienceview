package scienceview.utils.distance;

import scienceview.utils.matrix.SparseVector;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public interface Dissimilarity {

    public double calculate(SparseVector v1, SparseVector v2);

}
