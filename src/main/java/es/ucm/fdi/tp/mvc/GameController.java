package es.ucm.fdi.tp.mvc;

import java.util.List;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;

public class GameController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {

private List<GamePlayer> playerslist;
private GameTable<S,A> gametable;

public GameController(List<GamePlayer> players, GameTable<S,A> game){
	this.playerslist = players;
	this.gametable = game;
}

public void start(){
	gametable.start();
}

public void undo(){
	gametable.undo();
}

public void stop(){
	gametable.stop();
}

public void makeMove(A action){
	gametable.execute(action);
}

public void sendChat(String msg){
	gametable.sendChat(msg);
}


public void replay(){
	gametable.replay();
}


public void run()
{
	int playerCount = 0;
	for (GamePlayer p : this.playerslist) {
		p.join(playerCount++); // welcome each player, and assign
								// playerNumber
	}
	start();
	
	while (!gametable.getState().isFinished()) {
		// request move
		A action = playerslist.get(gametable.getState().getTurn()).requestAction(gametable.getState());
		makeMove(action);
	}
	
	stop();
}


}


