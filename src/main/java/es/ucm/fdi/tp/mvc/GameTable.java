package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

    protected S state;
    protected S preState;
    protected S InitialState;
    protected ArrayList<GameObserver<S,A>> ObserverList;
    protected boolean isStopped;
	private S pre2State;

    public GameTable(S initState)
    {
    	if(initState == null)
    		throw new GameError("Initial state cannot be null");
    	else{
    		this.InitialState = initState;
    		this.state = initState;
    		this.preState = this.state;
    		this.ObserverList = new ArrayList<GameObserver<S,A>>();
    	}
    }
    
    /** */
    public void start(){
        this.state = this.InitialState;
        this.isStopped = false;
        //reiniciamos tambien la lista de observers?;
        this.NotifyObservers(new GameEvent<S,A>(EventType.Start,null,state,null,"A game has been started/reset"));
    }
    
    public void undo(){
        this.state = this.pre2State;
        this.isStopped = false;
        this.NotifyObservers(new GameEvent<S,A>(EventType.Undo,null,state,null,"The action has been applied"));
    }
    
    /** */
    public void stop(){
       
    	if(!this.isStopped){
        	this.isStopped = true;
        	this.NotifyObservers(new GameEvent<S,A>(EventType.Stop,null,state,null,"The game has been Stopped"));
        }
        else{
        	GameError Error = new GameError("The game is already stopped");
        	this.NotifyObservers(new GameEvent<S,A>(EventType.Error,null,state,Error,"The game is already stopped"));
        	throw Error;
        }
    }
    
    /** */
    public void execute(A action){
        if(!this.isStopped && !state.isFinished())
        {
	        if(state.getTurn() == action.getPlayerNumber()){
	        	this.pre2State = this.preState;
	        	this.preState = this.state;
	        	
	        	state = action.applyTo(state);
	        	this.NotifyObservers(new GameEvent<S,A>(EventType.Change,action,state,null,"The action has been applied"));
	        	if(state.isFinished())
	            	this.stop();
	            
	        }
	        else{
	        	GameError Error = new GameError("The action cannot be applied");
	        	this.NotifyObservers(new GameEvent<S,A>(EventType.Error,action,state,Error,"The action cannot be applied"));
	        }
        }
    }
    
    /** Getter of State field */
    public S getState(){
		return this.state;
    }
    
    /** Add an Observer to ArrayList of Observers */
    public void addObserver(GameObserver<S, A> o){
       this.ObserverList.add(o);
    }
    
    /** Remove an Observer to ArrayList of Observers */
    public void removeObserver(GameObserver<S, A> o) {
        this.ObserverList.remove(o);
    }
    
    /** */
    protected void NotifyObservers(GameEvent<S,A> event){
    	for(GameObserver<S,A> observer : this.ObserverList)
    		observer.notifyEvent(event);
    }
    
    public void sendChat(String msg){
    	NotifyObservers(new GameEvent<S,A>(EventType.Info,null,state,null,msg));
    }
    
	public void replay() {
		this.isStopped = false;
        this.NotifyObservers(new GameEvent<S,A>(EventType.Replay,null,state,null,"A game has been started/reset"));
	}

}
