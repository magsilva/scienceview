package topicevolutionvis.wizard;

public interface Wizard {

	void refreshData();

	boolean canCancel();

	void cancel();

	boolean canResetConfiguration();

	void resetConfiguration();

	boolean isNextStepTerminal();

	boolean canGoToNextStep();

	boolean hasPreviousStep();

	boolean canGoToPreviousStep();

}