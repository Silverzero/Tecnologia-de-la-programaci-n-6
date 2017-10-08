package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import es.ucm.fdi.tp.mvc.*;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import javax.swing.*;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;

public class GameWindow <S extends GameState<S, A>, A extends GameAction<S,A>> extends JFrame implements GameObserver<S,A>, GameViewController, WindowRectBoard<S,A>
{
	private static final long serialVersionUID = 1L;
	
	enum PlayerMode	{
		MANUAL("Manual"),RANDOM("Random"),SMART("Smart");
		private String name;
		PlayerMode(String name){this.name = name;}	
		public String toString(){ return name;}
	}
	
	public interface GameCheck	{
		public void isPosible();
	}
	
	private GameController<S,A> controlador;
	private int playerID;
	private GameView<S,A> juego;
	private Settings settings;
	private S estado;
	GamePlayer random;
	ConcurrentAiPlayer smart;
	PlayerMode playermode;
	private int currentTurn;
	private boolean activeGame;
	private boolean isPaused = false;
	private Thread smartThread = null;
	private Integer smartTimeOut = 500;
	private Integer smartNThreads = 1;
	private int count = 0;
	
	
	

	public GameWindow(int i, GamePlayer random, ConcurrentAiPlayer smart, GameView<S,A> vistaJuego,GameController<S, A> controlador, GameObservable<S, A> game, String playerModes) 
	{
		this.controlador = controlador;
		this.random = random;
		this.smart = smart;
		this.juego = vistaJuego;
		this.playermode = playerModeParser(playerModes);
		this.settings = new Settings(this);
		this.playerID = i;
		this.initGUI();
		game.addObserver(this);
	}

	private PlayerMode playerModeParser(String playerModes) {
		
		String p = playerModes.toLowerCase();
		switch(p)
		{
		case "manual":
			return PlayerMode.MANUAL;
		case "random":
			return PlayerMode.RANDOM;
		case "smart":
			return PlayerMode.SMART;
		default:
			return null;
		}
	}

	private void initGUI()
	{
		JPanel mainpanel = new JPanel(new BorderLayout());
		
		//Panel Norte
		JPanel panelnorte = new JPanel(new BorderLayout());
		panelnorte.add(settings,BorderLayout.WEST);
		mainpanel.add(panelnorte,BorderLayout.PAGE_START);
		
		
		mainpanel.add(juego, BorderLayout.CENTER);
		mainpanel.setVisible(true);
		
		setContentPane(mainpanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(730,600));
		setMaximumSize(new Dimension(975,750));
		pack();
		juego.setViewControl(this);
		setVisible(true);
	}
	
	
	@Override
	public void notifyEvent(final GameEvent<S, A> e) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				handleEvent(e);
			}
		});

	}

	public void handleEvent(GameEvent<S, A> e) {
		
		estado = e.getState();
		currentTurn = estado.getTurn();
		activeGame = !estado.isFinished() || e.getType() != EventType.Stop;
		
		if(activeGame && currentTurn == playerID)
			settings.statusButtons(true);
		else
			settings.statusButtons(false);
		
		if(count <= 1)
		{
			settings.notYet();
			count++;
		}
		
		switch (e.getType()) {
		case Start:
			
			setTitle("View for Player" + playerID);
			juego.update(estado);
			juego.resetInfoMessage();
			juego.showInfoMessage("* " + e.toString() + "\n");
			if(estado.getTurn() == playerID)
				this.juego.showInfoMessage("* Your turn.\n");
			decideAutomaticMakeMove();
			break;
		case Change:
			juego.update(estado);
			decideAutomaticMakeMove();
			juego.showInfoMessage("* "+ e.getAction().toString()+"\n");
			if(estado.getTurn() == playerID)
				juego.showInfoMessage("* Your turn.\n");
			break;
		case Error:
			juego.update(estado);
			juego.showInfoMessage("* " + e.toString() +"\n");
			break;
		case Stop:
			juego.showInfoMessage("* " + e.toString() +"\n");
			juego.update(estado);
			break;
		case Undo:
			juego.update(estado);
			count= 0;
			juego.showInfoMessage("* Movement undone. \n");
			break;
		case Replay:
			juego.update(estado);
			juego.showInfoMessage("* You can continue the game \n");
			decideAutomaticMakeMove();
			break;
		case Info:
			juego.showInfoMessage(e.toString());
			juego.update(estado);
			break;
		default:
			break;
		}
		
		if(!isPaused && this.currentTurn != this.playerID && EventType.Stop == e.getType())
			juego.showInfoMessage("* Player " + this.currentTurn + " wins. \n");
		else if(!isPaused && this.currentTurn == this.playerID && EventType.Stop == e.getType())
			juego.showInfoMessage("* You wins. \n");
	}
	
	public void decideAutomaticMakeMove()	{
		switch(playermode)
		{
		case RANDOM: SwingUtilities.invokeLater(new Runnable(){
			
			@Override
			public void run() {
				makeRandomMove();
			}
			
		});break;
		case SMART:
			makeSmartMove(); break;
		default:
			break;
		}
	}

	@Override
	public void makeRandomMove() {
		if(activeGame && currentTurn == playerID)
			controlador.makeMove(random.requestAction(estado));
	}

	@Override
	public void makeSmartMove() {
		
		if(isPosible())
		{
			if (smartThread == null)
			{
					smartThread = new Thread(() -> {
					SwingUtilities.invokeLater(() -> settings.setThinking(true));
				
					long time0 = System.currentTimeMillis();
					smart.setMaxThreads(smartNThreads);	
					smart.setTimeout(smartTimeOut);
				
						A action = smart.requestAction(estado);
				
					long time1 = System.currentTimeMillis();
				
					if(action != null)
					{
						SwingUtilities.invokeLater(() ->
						{
							settings.setThinking(false);
							juego.showInfoMessage("* " + smart.getEvaluationCount() 
							+ " nodes in " + (time1-time0) + " ms. \n  Value = " 
							+ String.format("%.5f",smart.getValue()) + "\n");
						});
					
						SwingUtilities.invokeLater(()-> 
							{
								controlador.makeMove(action);
							});
					}//action != null
					
					try{
					SwingUtilities.invokeAndWait(() -> GameWindow.this.smartThread = null);
					}catch(Exception e){}
			});
			
			}//smartThread == null
			smartThread.start();
		}//if posible
	}

	@Override
	public void buttonStop() {
		controlador.stop();
	}
	
	public void buttonUndo(){
		controlador.undo();
	}

	@Override
	public void buttonRestartGame()	{
		controlador.start();
	}
	

	@Override
	public void setComboBox(PlayerMode playermode)	{
		this.playermode = playermode;
		decideAutomaticMakeMove();
	}

	@Override
	public void cancelSmartMove(){
		juego.showInfoMessage("* Smart move has been cancelled");
		if(smartThread != null ||smartThread.isAlive())
			smartThread.interrupt();
		settings.setThinking(false);
	}

	@Override
	public boolean isPosible(){
		return activeGame && currentTurn == playerID;
	}

	@Override
	public void makeMove(A a)
	{
		controlador.makeMove(a);
	}

	@Override
	public boolean buttonrePLay() {
			if(!isPaused){
				isPaused = true;
				controlador.stop();
			}
			else
			{
				isPaused = false;
				controlador.replay();
			}
			return isPaused;
	}
	
	@Override
	public void sendChat(String s){
		controlador.sendChat(s);
	}

	@Override
	public PlayerMode getPlayerMode() {
		return playermode;
	}

	@Override
	public String getPlayerType() {
		return this.playermode.toString();
	}

	@Override
	public void setTimeOut(Integer value) {
		this.smartTimeOut = value;
	}

	@Override
	public void setThreads(Integer value) {
		this.smartNThreads = value;
	}

	@Override
	public boolean getActiveGame() {
		return this.activeGame;
	}
}

