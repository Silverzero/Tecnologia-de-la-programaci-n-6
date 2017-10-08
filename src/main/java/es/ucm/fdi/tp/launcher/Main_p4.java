/**
 * @author Ruben Martin Acebedo
 * @author Marco Desantes Gutierrez
 **/


package es.ucm.fdi.tp.launcher;

import es.ucm.fdi.tp.was.WasState;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.ttt.TttState;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import es.ucm.fdi.tp.base.console.ConsolePlayer;

/**
 * Clase que permite la eleccion entre el lobo y el tres en raya
 */
public class Main_p4 {

	/**
	 * Metodo que se encarga por completo del juego dependiente de todos los demas
	 */
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState,
			List<GamePlayer> players) {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;
		System.out.println(currentState);
		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);

			if (currentState.isFinished()) {
				// game over
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				if (winner == -1) {
					endText += "draw!";
				} else {
					endText += "player " + (winner + 1) + " (" + players.get(winner).getName() + ") won!";
				}
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}
	/**
	 * Metodo para crear a los jugadores segun el tipo
	 * @return delvuelve el jugador correspondiente o null si hay error
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName)
	{
		
		if(playerType.equalsIgnoreCase("smart"))
		{
			return new SmartPlayer(playerName,5);
		}
		else if(playerType.equalsIgnoreCase("console"))
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
	/**
	 * Metodo para inicializar el juego
	 * @return delvuelve el estado por defecto segun el juego o null si hay un error
	 */
	public static GameState<?,?> createInitialState(String gameName)
	{
		if(gameName.equalsIgnoreCase("Was"))
		{
			return new WasState();
		}
		else if(gameName.equalsIgnoreCase("Ttt"))
		{
			return new TttState(3);
		}
		else
			System.out.println("Error al describir el juego");
			System.exit(1);
			return null;
		
	}
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String... args)
	{
		Main_p4 p = new Main_p4();
		//Si el primer argumento falla o el numero es incorrecto salimos
		if(!p.arguments(args)) System.exit(1);
		else
		{
			ArrayList<GamePlayer> listaJugadores= new ArrayList<GamePlayer>();
			for(int i = 1; i < args.length; i++)
				listaJugadores.add(createPlayer(args[0],args[i], "player "+i ));
			
			playGame(createInitialState(args[0]),listaJugadores);
		}
		
	}
	
	public boolean arguments(String... args)
	{
		if(args.length != 3)
		{
			System.out.println("Error, numero de argumentos incorrectos");
			return false;
		}
		else
		{
			if(!args[0].equalsIgnoreCase("Ttt") &&  !args[0].equalsIgnoreCase("Was"))
			{
				System.out.println("Argumento invalido");
				return false;
			}
		}
		return true;
	}

}
