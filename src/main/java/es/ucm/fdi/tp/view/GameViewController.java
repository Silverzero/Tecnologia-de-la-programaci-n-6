package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.view.GameWindow.PlayerMode;

/** Connect GameWindow -> Settings*/
public interface GameViewController {

	public void makeRandomMove();
	public void makeSmartMove();
	public void buttonStop();
	public void buttonRestartGame();
	public void setComboBox(PlayerMode playermode);
	public void buttonUndo();
	public void cancelSmartMove();
	public boolean buttonrePLay();
	public PlayerMode getPlayerMode();
	public void setTimeOut(Integer value);
	public void setThreads(Integer value);
	public boolean getActiveGame();
}
