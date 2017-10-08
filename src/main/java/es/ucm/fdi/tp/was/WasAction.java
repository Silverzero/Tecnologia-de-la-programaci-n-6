package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * Clase que permite el desplazamiento de una casilla a otra
 * */
public class WasAction implements GameAction<WasState,WasAction>
{
	
	private static final long serialVersionUID = 1L; // Serial para alguna version de la practica superior
	
	public int getRowDestino() {
		return rowDestino;
	}

	public int getColDestino() {
		return colDestino;
	}

	public int getRowOr() {
		return rowOr;
	}

	public int getColOr() {
		return colOr;
	}


	private int player; //Lobo = 0, ovejas = 1
    private int rowDestino;
    private int colDestino;
    private int rowOr;
    private int colOr;
    
   /**
    * Constructora por parametros, no hay por defecto porque no tiene sentido hacer un movimiento predeterminado
    * */
    public WasAction(int player, int rowOr, int colOr,int rowFinal, int colFinal)
    {
    	this.player = player;
    	this.rowOr = rowOr;
    	this.colOr = colOr;
    	this.colDestino = colFinal;
    	this.rowDestino = rowFinal;
    }
    
	@Override
	public int getPlayerNumber() {
		return this.player; //Devuelve el jugador actual
	}

	@Override
	/**
	 * Metodo mas importante, efectua el movimiento
	 * @return devuelve el nuevo estado tras la realizacion del movimiento (tipo WasState)
	 * */
	public WasState applyTo(WasState state) {
		  if (player != state.getTurn()) {
	            throw new IllegalArgumentException("Not the turn of this player");
	        }
		//cargo el tablero
	        int[][] board = state.getBoard();
	        board[this.rowDestino][this.colDestino] = player; //Movemos a la nueva posicion
	        board[this.rowOr][this.colOr] = -1; //Inicializamos la antigua.
	        //Si ha llegado hasta aqui es que la lisa de movimientos no es vacia, y que el mov es valido
	        //Solo queda ver si el lobo ha ganado o seguira el juego
	        WasState next = new WasState(state, board);
	        return next;
	}
	
	
	 public String toString() {
	     return "Player " + player + " move for  (" + rowOr + ", " + colOr + ") to (" + rowDestino + ", " + colDestino + ")";
	 }


	
}
