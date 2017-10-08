package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.chess.*;
import es.ucm.fdi.tp.chess.ChessBoard.Piece;
import es.ucm.fdi.tp.view.JBoard.Shape;


public class ChessView extends RectBoardGameView<ChessState, ChessAction> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer,Image> Figuras = new HashMap<Integer,Image>();
	private int origRow;
	private int origCol;
	private boolean firstClickRecieved;
	
	public ChessView(int PlayerID){
		super(PlayerID);
		initialHashMap();
	}
	
	private void initialHashMap()
	{
		for(Piece p : Piece.values())
		{
			if(p != Piece.Empty && p != Piece.Outside)
			{
				byte code = p.white();
				Figuras.put((Integer)(int)code,Utils.loadImage(Piece.iconName(code)));
				code = p.black();
				Figuras.put((Integer)(int)code,Utils.loadImage(Piece.iconName(code)));
			}
		}
	}

	@Override
	protected int getNumCols() {
		return state != null ? state.getDimension() : -1;
	}

	@Override
	protected int getNumRows() {
		return state != null ? state.getDimension() : -1;
	}
	
	@Override
	protected Shape getShape(int player){
		return Shape.CIRCLE;
	}
	
	@Override
	protected Color getColor(int player)
	{
		Color Wcolor =  getPlayerColor(0);
		Color Bcolor =	getPlayerColor(1);
		
		if(player >= 1 && player <= 8)
		{
			return Wcolor;
		}
		else
			return Bcolor;
	}
	
	@Override
	protected Image getImage(int row, int col){
		return Figuras.get(state.at(row, col));
	}
	
	@Override
	protected Color getBackground(int row, int col) {
		return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.DARK_GRAY;
	}
	@Override
	protected Integer getPosition(int row, int col) {
		return state != null && !ChessBoard.empty((byte)state.at(row, col)) ? state.at(row, col) : null;
	}
	
	protected int convertToPlayer(int i){
		if(i >= 1 && i <= 6)
			return 0;
		else if(i >= 9 && i <= 14)
			return 1;
		else 
			return -1;
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton)
	{
			if(!firstClickRecieved && controlador.isPosible())
			{
				if(convertToPlayer(state.at(row, col)) == playerID)
				{
					origRow = row;
					origCol = col;
					showInfoMessage("* Selected (" + origRow + "," + origCol + ") \n");
					firstClickRecieved = true;
				}
				else
				{
					showInfoMessage("* Empty cell or Not your token \n");
				}
			}
			else
			{
				if(!firstClickRecieved)
					showInfoMessage("* Its not your turn \n");
				else
				{
					ChessAction action = new ChessAction(state.getTurn(),origRow,origCol,row,col);
					if(state.isValid(action))
					{
						controlador.makeMove(action);
					}
				}
				firstClickRecieved = false;
			}
				
	}
}
