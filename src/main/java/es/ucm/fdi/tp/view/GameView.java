package es.ucm.fdi.tp.view;

import javax.swing.JComponent;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;



public abstract class GameView<S extends GameState<S, A>, A extends GameAction<S,A>>  extends JComponent {

private static final long serialVersionUID = 8971247618260421832L;
public abstract void setViewControl(WindowRectBoard<S,A> controlador);
public abstract void showInfoMessage(String msg);
public abstract void resetInfoMessage();
public abstract void update(S s);
}
