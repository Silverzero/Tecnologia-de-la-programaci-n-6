package es.ucm.fdi.tp.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.view.ColorChooser.ColorInterface;
import es.ucm.fdi.tp.view.JBoard.Shape;




public abstract class RectBoardGameView< S extends GameState<S,A>, A extends GameAction<S,A>> extends GameView<S,A> implements ColorInterface {

	private static final long serialVersionUID = 1L;
	protected S state;
	private JBoard tablero;
	private InfoPanel<S,A> infoPanel;
	protected int playerID;
	protected WindowRectBoard<S,A> controlador;
	protected Map<Integer, Color> playerColors;
	protected JTextArea chat;
	protected JButton submit;
	Iterator<Color> colorsIter;

	public RectBoardGameView(int player)
	{
		super();
		this.playerID = player;
		this.colorsIter = Utils.colorsGenerator();
		this.playerColors = new HashMap<>();
		this.initGUI();
	}

	private void initGUI()
	{
		setLayout(new BorderLayout());
		/* TABLERO SIN PANEL PARA QUE SE REDIMENSIONE*/
		
		
		tablero = new JBoard(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void keyTyped(int keyCode){
				//nada		
			}

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				RectBoardGameView.this.mouseClicked(row, col, clickCount, mouseButton);
				
			}
			
			@Override
			protected Shape getShape(int player) {
				return RectBoardGameView.this.getShape(player);
			}


			@Override
			protected Color getColor(int player) {
				return RectBoardGameView.this.getColor(player);
			}
			
			@Override
			protected Color getBackground(int row, int col) {
				return RectBoardGameView.this.getBackground(row, col);
			}

			@Override
			protected int getNumRows() {
				return RectBoardGameView.this.getNumRows();
			}

			@Override
			protected int getNumCols() {
				return RectBoardGameView.this.getNumCols();
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return RectBoardGameView.this.getPosition(row, col);
			}

			@Override
			protected Image getImage(int row, int col) {
				return RectBoardGameView.this.getImage(row, col);
			}};
			
			
		add(tablero,BorderLayout.CENTER);
				
		JPanel chatpanel = new JPanel(new BorderLayout());
		chatpanel.setBorder(BorderFactory.createTitledBorder("Chat"));
		chat = new JTextArea(1,1);
		chatpanel.add(chat,BorderLayout.CENTER);
		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = "* Player " + playerID + " says:\n"
				+ " - " + chat.getText() + "\n";
				chat.setText("");
				controlador.sendChat(s);
				
			}
			
		});
		chatpanel.add(submit,BorderLayout.EAST);
	
		/* Añado el panel derecho implementado en otra clase*/
		infoPanel = new InfoPanel<S,A>(this);
		infoPanel.add(chatpanel,BorderLayout.SOUTH);
		add(this.infoPanel,BorderLayout.EAST);
		
	}
	
	protected Shape getShape(int player) {
		return player < state.getPlayerCount() ? Shape.CIRCLE : Shape.RECTANGLE;
	}
	
	protected Color getColor(int player) {
			return getPlayerColor(player);
	}
	
	final protected Color getPlayerColor(Integer p) {
		Color color = playerColors.get(p);
		if (color == null) {
			color = assignColor(p);
		}
		return color;
	}
	
	private Color assignColor(int p) {
		Color c = colorsIter.next();
		playerColors.put(p, c);
		return c;
	}
	
	protected Color getBackground(int row, int col) {
		return Color.LIGHT_GRAY;
	}

	
	protected int getSepPixels() {
		return 2;
	}
	
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract Integer getPosition(int row, int col);
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	
	public void showInfoMessage(String message)	{
		infoPanel.showInfoMessage(message);
		infoPanel.repaint();
	}
	
	public void resetInfoMessage()	{
		infoPanel.resetInfoMessage();
		infoPanel.repaint();
	}
	
	public void changePlayerColor(int row, Color  c){
        playerColors.put(row,c);
        repaint();
        tablero.repaint();
	}

	public Color PaintColor(int player) {
		return getColor(player);
	}
	
	@Override
	public void update(S s) {
		state = s;
		tablero.repaint();
		infoPanel.repaint();
	}
	
	@Override
	public void setViewControl(WindowRectBoard<S,A> controlador)
	{
		this.controlador = controlador;
	}
	
	@Override
	public String getPlayerType() {
		return controlador.getPlayerType();
	}
	
	@Override
	public int getPlayerID() {
			return this.playerID;
	}
	
	protected Image getImage(int row, int col) {
		return null;
	}
	
}
