package es.ucm.fdi.tp.was;

import static org.junit.Assert.*;


import org.junit.Test;

public class WasStateTest {

	@Test
	//Lobo llega a su objetivo, fila 0
	public void testLobo() {
		WasState estado = new WasState();
		int[][] board = estado.getBoard();
		board[0][1] = 0; //Victoria del lobo
        board[7][0] = 1; 
        WasState next = new WasState(estado, board); //Creo un nuevo estado con el lobo en la casilla 0
       int num =  next.isWinner(board, 0); 
       assertEquals(num, 0);
	}
	
	@Test
	//Lobo acorralado
	public void testOvejasWin() {
		WasState estado = new WasState();
		int[][] board = estado.getBoard();
        board[6][1] = 1; //Si la oveja bloquea el unico movimiento del lobo ganan
        WasState next = new WasState(estado, board); //Creo un nuevo estado con el lobo bloqueado
       int num =  next.isWinner(board, 1); 
       assertEquals(num, 1);
	}

	
	@Test
	//Lobo tiene 1 movimiento valido, luego tiene 4
	public void testLoboMovs() {
		WasState estado = new WasState();
		for(int i = 7; i > 1;i--)
		{
			for(int j = 0; j < estado.getBoard().length; j+=2)
			{
				int aux = j;
				int board[][] = estado.getBoard();
				board[7][0] = -1;			
				if(i%2 == 0) aux++; //Si la fila es par
				 board[i][aux] = 0; //Vamos poniendo todos los posibles movimientos del lobo
				WasState estado2 = new WasState(estado, board); //Actualizamos tablero
				//Si esta en el inicio solo puede tener un mov
				if(i == 7 && aux == 0) assertEquals(1, estado2.validActions(0).size());
				//Si esta en la ultima fila solo puee tener 2 movs exceptuando la posicion inicial
				else if (i == 7 && aux != 0) assertEquals(2, estado2.validActions(0).size());
				//Si esta en un lateral solo puede tener 2
				else if( aux == 0 || aux == 7) assertEquals(2, estado2.validActions(0).size());
				//Si esta en cualquier otro lado debe tener 4
				else assertEquals(4, estado2.validActions(0).size());
			}
		}
	}
	
	@Test
	public void testOvejaMovs() {
		WasState estado = new WasState();
		for(int i = 0; i < estado.getBoard().length;i++)
		{
			for(int j = 0; j < estado.getBoard().length; j+=2)
			{
				int aux = j;
				//Actualizo a un tablero sin ovejas ni lobo y voy poniendo una que lo recorra segun el for
				int board[][] = estado.getBoard();
				board[0][1] = -1;
				board[0][3] = -1;
				board[0][5] = -1;
				board[0][7] = -1;
				board[7][0] = -1;			
				if(i == 0 || i%2 == 0) aux++; //Si la fila es par
				 board[i][aux] = 1; //Vamos poniendo todos los posibles movimientos de las ovejas
				WasState estado2 = new WasState(estado, board); //Actualizamos tablero
				//Si esta abajo del todo no tiene que tener movs
				if(i == 7) assertEquals(0, estado2.validActions(1).size());
				//Si esta en un lateral solo puede tener 1
				else if( aux == 0 || aux == 7) assertEquals(1, estado2.validActions(1).size());
				//Si esta en cualquier otro lado debe tener 2
				else assertEquals(2, estado2.validActions(1).size());
			}
		}
	
	}


}
