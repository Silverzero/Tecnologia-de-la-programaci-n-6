/**
 * @author Ruben Martin Acebedo
 * @author Marco Desantes Gutierrez
 **/

package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;


public class WasState extends GameState<WasState, WasAction> {

	private static final long serialVersionUID = 1L; //Serial para versiones futuras
	
	private final int turn;
    private final boolean finished; //Variable que indica el final del juego
    private final int[][] board; //Tablero
    private final int winner; //Ganador (0 lobo 1 ovejas)
    private static final int dim = 8;

    final static int EMPTY = -1;
    
    /**
     * Constructora inicial para el S0, Estado inicial.
     */
	public WasState()
	{
		//llamamos a la constructora padre que establece el numero de jugadores a 2.
		super(2);
        //inicializamos el tablero vacio a -1.
        board = new int[WasState.dim][];
        for (int i=0; i<WasState.dim; i++) {
            board[i] = new int[WasState.dim];
            for (int j=0; j<WasState.dim; j++) 
            	this.board[i][j] = EMPTY;
        }        
        //creamos el lobo.
        this.board[WasState.dim-1][0] = 0;
        //creamos ovejas.
        for(int i = 1; i < WasState.dim; i=i+2)
        {
        	this.board[0][i] = 1;
        }
        //inicializamos el primer turno al lobo.
        this.turn = 0;
        //inicializamos sin ganadory que no ha acabado la partida.
        this.winner = -1;
        this.finished = false;
	}
	
	/**
	 * Constructora para estados precedidos por otros estados tras la realizacion de un movimiento
	 */
	public WasState(WasState prev, int[][] board)
	{
    	super(2);
        this.board = board;
        this.turn = (prev.turn + 1) % 2;
        this.winner = isWinner(board,prev.getTurn());
        if(this.winner != -1)
        	this.finished = true;
        else 
        	this.finished = false;
    }    

	/**
	 * Lista de acciones validas por el jugador indicado. 
	 * Puede ser vacia.
	 * @return devuelve la lista de acciones 
	 * */
	@Override
	public List<WasAction> validActions(int playerNumber) {
		ArrayList<WasAction> valid = new ArrayList<WasAction>();
        if (finished)
        {
        	return valid;
        }     
	        for (int i = 0; i < dim; i++)
	        {
	            for (int j = 0; j < dim; j++)
	            {
	                if (getPosicion(i, j) == playerNumber)
	                {
	                    calcularAdyacentes(playerNumber,valid,i,j);
	                }
	            }
	        }
       
    
        return valid;
	}
	private void calcularAdyacentes(int playerNumber, ArrayList<WasAction> valid, int i, int j)
	{
		if(playerNumber == 0)
		{
			for (int x = -1; x < 2; x=x+2)
			{
				for (int y = -1; y < 2; y=y+2)
				{
					if(this.enRango(i+x,j+y) && this.getPosicion(i+x,j+y) == -1 )
					{
						valid.add(new WasAction(playerNumber,i,j,i+x,j+y));
					}
				}
			}
		}
		else if(playerNumber == 1)
		{
			for (int y = -1; y < 2; y=y+2)
			{
				if(isValid(i+1,j+y))
				{
					valid.add(new WasAction(playerNumber,i,j,i+1,j+y));
				}
			}
		}
		
	}
	/**
	 * Getter que devuelve la posicion
	 */
	public int getPosicion(int row, int col)
	{
		return board[row][col];
	}
	
	/**
	 * Calcula que no exceda los limites del tablero
	  */
	private boolean enRango(int i, int j)
	{
		return i < WasState.dim && i >= 0 && j < WasState.dim && j >= 0;
	}

	/**
	 * Metodo que hace la comprobacion sobre si hay un ganador o prosigue el juego
	 * @return devuelve el ganador o un valor -1 que indique que continua
	 */
	public int isWinner(int[][] board, int playerNumber)
	{
		//Si es el lobo
		if(playerNumber == 0)
		{
			//Si el lobo llego a la ultima fila
			for(int i = 0; i < WasState.dim; i++)
			{
				if(board[0][i] == 0)
					return 0;
			}
			//Si las ovejas no tienen movimientos.
			List<WasAction> valida = this.validActions(1);
			if(valida.size()==0) return 1;
			
			//nada
			return -1;
		}
		else if(playerNumber == 1)
		{
			//Si la lista de movimientos del lobo es vacia.
			List<WasAction> valida = this.validActions(0);
			if(valida.size()==0) return 1;
			
			//Si la lista de movimiento de las ovejas es 0;
			List<WasAction> ovejas = this.validActions(1);
			if(ovejas.size()==0) return 0;
			
			return -1;
		}
		else return -1;
	}
	
	//Getter
	@Override
	public int getWinner(){
		return this.winner;
	}
	
	public boolean isFinished(){
		return this.finished;
	}
	
	public int getTurn(){
		return this.turn;
	}
	
	public int at(int row, int col){
		return row >= 0 && row < board.length && col >= 0 && col < board.length ? board[row][col] : -2;
	}
	
	public int[][] getBoard(){
		 int[][] copy = new int[board.length][];
	        for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
	        return copy;
	}
	
	public boolean isValid(int i,int j)
	{
		return this.enRango(i,j) && this.getPosicion(i,j) == -1; 
	}
	
	public boolean ValidMovement(WasAction action, int player)
	{
		/*
		ArrayList<WasAction> lista = (ArrayList<WasAction>) validActions(player);
		return lista.contains(action);
		*/
		
		ArrayList<WasAction> lista = (ArrayList<WasAction>) validActions(player);
		for(WasAction i: lista )
		{
			if(i.getRowOr() == action.getRowOr() && i.getColOr() == action.getColOr() && 
			   i.getRowDestino() == action.getRowDestino() && i.getColDestino() == action.getColDestino()) 
				return true;
		}
		return false;
	}
	
	//toString
	public String toString()
	{
	     StringBuilder sb = new StringBuilder();
	     
	     for(int i = 0; i <=WasState.dim; i++)
	     {
	    	 if(i == 0)
	    	 {
	    		 sb.append(" ");
	    	 }
	    	 else sb.append(" " + (i-1) + " ");
	     }
	     sb.append("\n");
	     
	     for (int i=0; i<this.board.length; i++) {
	            sb.append(i);
	            for (int j=0; j<this.board.length; j++) {
	                if(this.board[i][j] == EMPTY && (((i+j)%2) == 1))
	                	sb.append(" . ");
	                else if (this.board[i][j] == 0)
	                	sb.append(" W ");
	                else if (this.board[i][j] == 1)
	                	sb.append(" S ");
	                else
	                	sb.append("   ");
	            }	
	            sb.append("\n");
	        }
	        return sb.toString();  
	}

	@Override
	public int getDimension() {
		return WasState.dim;
	}
	
	/*
	@Override
	public void setObstacles(int x) {
		
		int nummEmpties = 0;
		for(int i = 0; i < getDimension(); i++)
			for(int j = 0; j < getDimension(); j++)
				if(at(i,j) == -1 && (((i+j)% 2) == 1))
					nummEmpties++;

		if(nummEmpties >= x )
		{
			int row,col;
			int i=0;
			do{
				row = (int) (Math.random() * getDimension());
				col = (int) (Math.random() * getDimension());
				if(at(row, col) == -1 && (((row+col)% 2) == 1))
				{
					board[row][col]= OBSTACLE;
					i++;
				}
			}while(i < x);
		}
		else
		{
			System.out.print("No hay suficientes huecos");
		}
	}*/
}
