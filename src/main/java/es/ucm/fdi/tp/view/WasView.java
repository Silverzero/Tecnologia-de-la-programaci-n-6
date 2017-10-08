package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.was.*;

public class WasView extends RectBoardGameView<WasState, WasAction> {

	private static final long serialVersionUID = -8721858828945561318L;

	protected boolean firstClickRecieved;
	private HashMap<Integer,Image> Figuras = new HashMap<Integer,Image>();
	int origRow;
	int origCol;
	
	public WasView(int player) {
		super(player);
		initialHashMap();
	}
	
	private void initialHashMap()
	{
		Figuras.put(1,Utils.loadImage("cabra2.png"));
		Figuras.put(0,Utils.loadImage("lobomarco.png"));
	}
	

	@Override
	protected int getNumCols() {
		return 8;
	}

	@Override
	protected int getNumRows() {
		return 8;
	}

	@Override
	protected Integer getPosition(int row, int col) {
		return state != null && state.at(row, col) != -1 ? state.at(row, col) : null;
	}

	@Override
	protected Color getBackground(int row, int col) {
		return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
	}

	@Override
	protected int getSepPixels() {
		return 2;
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		
			if (!firstClickRecieved && controlador.isPosible()) {
				
				if( playerID == state.at(row, col)){
					origRow = row;
					origCol = col;
					showInfoMessage("* Selected (" + origRow + "," + origCol + ") \n");
					firstClickRecieved = true;
					//pintarCasillas(origRow,origCol,playerID);
				}
				else 
					showInfoMessage("* Empty cell or Not your token \n");
			}
			else {
				if(!firstClickRecieved)
					showInfoMessage("* Its not your turn \n");
				else
				{ 
						
					if ( controlador.isPosible() && -1 == state.at(row, col)
					&& state.isValid(row,col)){
						
						WasAction action = new WasAction(state.getTurn(), origRow, origCol, row, col);
						if(state.ValidMovement(action,playerID))
							controlador.makeMove(action);
						else 
							this.showInfoMessage("* Wrong movement \n");
					}
					else 
						this.showInfoMessage("* Action canceled \n");
					
					firstClickRecieved = false;
				}
			}
		}
	
	@Override
	protected Image getImage(int row, int col)	{
		return Figuras.get(state.at(row, col));
	}

	@Override
	public String getPlayerType() {
		return controlador.getPlayerType();
	}


}