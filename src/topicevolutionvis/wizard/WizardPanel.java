package topicevolutionvis.wizard;

import javax.swing.JPanel;

/**
 *
 * @author Fernando Vieira Paulovich
 */
public abstract class WizardPanel extends JPanel implements Wizard
{
    @Override
	public abstract void refreshData();
    
    @Override
	public abstract boolean canCancel();
    
    @Override
	public abstract void cancel();

    @Override
	public abstract boolean canResetConfiguration();
    
    @Override
	public abstract void resetConfiguration();
    
    @Override
	public abstract boolean isNextStepTerminal();
    
    @Override
	public abstract boolean canGoToNextStep();
    
    @Override
	public abstract boolean hasPreviousStep();

    @Override
	public abstract boolean canGoToPreviousStep();
}
