/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scienceview.datamining.clustering;

/**
 *
 * @author USER
 */
public class CompactnessTransition extends InternalTransition {

    public static final int MORE_COMPACT = 0;
    public static final int MORE_DISPERSE = 1;
    public double diff_dispersion;

    public CompactnessTransition(double diff_dispersion) {
        super(InternalTransition.COMPACTNESS_TRANSITION);
        this.diff_dispersion = diff_dispersion;
    }
}
