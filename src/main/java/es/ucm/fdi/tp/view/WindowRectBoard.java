package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public interface WindowRectBoard <S extends GameState<S, A>, A extends GameAction<S,A>> 
{
	public boolean isPosible();
	
	public void makeMove(A a);
	
	public void sendChat(String s);
	
	public String getPlayerType();
}
