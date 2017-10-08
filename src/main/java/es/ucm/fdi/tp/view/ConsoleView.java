package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView <S extends GameState<S,A>, A extends GameAction<S,A>> implements GameObserver<S,A>{

	public ConsoleView(GameObservable<S,A> gameTable)
	{
		gameTable.addObserver(this);
	}


	@Override
	public void notifyEvent(GameEvent<S, A> event){
		
	if(event.getType() != GameEvent.EventType.Stop)
	{
		
		if(event.getType() == GameEvent.EventType.Change && event.getAction() != null )
			System.out.println(event.getAction());
		
		System.out.println("Current State: ");
		System.out.println(event.getState());
		System.out.println(event);
		
	}
	else
	{
		System.out.println(event);
		String endText = "The game ended: ";
		int winner = event.getState().getWinner();
		if (winner == -1)
		{
			endText += "draw!";
		}
		else
		{
			endText += "player " + winner + " won!";
		}
		System.out.println(endText);
	}
}
	
}