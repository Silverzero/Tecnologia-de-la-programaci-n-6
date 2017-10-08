//Marco Desante Gutierrez
//Ruben Martin Acebedo

package es.ucm.fdi.tp.launcher;


import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.*;
import es.ucm.fdi.tp.base.player.*;
import es.ucm.fdi.tp.chess.*;
import es.ucm.fdi.tp.mvc.*;
import es.ucm.fdi.tp.ttt.*;
import es.ucm.fdi.tp.view.*;
import es.ucm.fdi.tp.was.*;

public class Main {
	

private static GameTable<?,?> createGame(String gType) {
			
		switch (gType) {
		case "ttt":
			return new GameTable<TttState, TttAction>(new TttState(3));
		case "was":
			return new GameTable<WasState, WasAction>(new WasState());
		case "chess":
			return new GameTable<ChessState,ChessAction>(new ChessState());
		default : return null;
		}
	}

private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startConsoleMode(String gType, GameTable<S, A> game, String playerModes[])
{
	ArrayList<GamePlayer> playerlist = new ArrayList<GamePlayer>();
	for(int i = 0; i < playerModes.length; i++)
		playerlist.add(createPlayer(gType,playerModes[i],"player "+i));
	new ConsoleView<S,A>(game);
	new GameController<S,A>(playerlist,game).run();
}

private static GamePlayer createPlayer(String gameName, String playerType, String playerName)
{
	
	if(playerType.equalsIgnoreCase("smart"))
	{
		return new SmartPlayer(playerName,5);
	}
	else if(playerType.equalsIgnoreCase("manual"))
	{
		Scanner buffer = new Scanner(System.in);
		return new ConsolePlayer(playerName,buffer);
	}
	else if(playerType.equalsIgnoreCase("random"))
	{
		return new RandomPlayer(playerName);
	}
	else
	{
		System.exit(1);
		return null;
	}
}

private static <S extends GameState<S, A>, A extends GameAction<S,A>> void startGUIMode(final String gType, final GameTable<S, A> game, String playerModes[])
{
		
		final GameController<S,A> controlador = new GameController<S,A>(null,game);
		//final RectBoardGameView<S,A> vistaJuego = Main.createGameView(gType,game.getState());
		
		for(int i = 0; i < game.getState().getPlayerCount(); i++)
		{
			final GamePlayer random = new RandomPlayer("Player - " + i); 
			ConcurrentAiPlayer smart = new ConcurrentAiPlayer("Concurrent Player");
			random.join(i);
			smart.join(i);
			final int j = i;
			try {
				SwingUtilities.invokeAndWait(new Runnable(){
					@SuppressWarnings("unchecked")
					public void run()
					{
						new GameWindow<S,A>(j,random,smart,(GameView<S,A>)createGameView(gType,j),controlador,game,playerModes[j]);
					}
				});
			} catch (InvocationTargetException | InterruptedException e) {
				System.err.print("Some error occurred while creating the view ...");
				System.exit(1);
			}
		}
		
		SwingUtilities.invokeLater(
		new Runnable(){
			public void run()
			{
				controlador.start();
			}
		});
}


private static GameView<?,?> createGameView(String gType,int i) {
	
	switch (gType) {
	case "ttt":
		return new TttView(i);
	case "was":
		return new WasView(i);
	case "chess":
		return new ChessView(i);
	default : return null;
	}
	
}

private static void usage()
{
	System.out.println("The GameEmulator has at least 4 parameters");
	System.out.println("1º- Game to play (Was, Chess or Ttt)");
	System.out.println("2º- Execution mode (Console or GUI)");
	System.out.println("3º- Player 0-N (Manual, Random or Smart)");
	System.out.println("For example (Was Gui Manual Smart)");
}

public static void main(String[] args)
{
	if (args.length < 2)
	{
		usage();
		System.exit(1);
	}
	GameTable<?, ?> game = createGame(args[0]);
	
	if (game == null)
	{
		System.err.println("Invalid game");
		usage();
		System.exit(1);
	}
	String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
	switch (args[1]) {
	case "console":
		startConsoleMode(args[0],game,otherArgs);
		break;
	case "gui":
		startGUIMode(args[0],game,otherArgs);
		break;
	default:
		System.err.println("Invalid view mode: "+args[1]);
		usage();
		System.exit(1);
	}
}
}
