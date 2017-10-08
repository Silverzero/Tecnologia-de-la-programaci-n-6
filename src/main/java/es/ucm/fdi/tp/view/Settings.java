package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.view.GameWindow.PlayerMode;


public class Settings extends JToolBar{
 
	private static final long serialVersionUID = 1L;
	private GameViewController controlador;
	
	//Toolbar fields
	private JComboBox<PlayerMode> ComboPlayer;
	private JButton undoButton;
	private JButton smartButton;
	private JButton randomButton;
	private JButton restartButton;
	private JButton offButton;
	private JButton rePlayButton;
	
	//Smart panel fields
	private JLabel brain;
	private JLabel clock;
	private JSpinner spinner;
	private JSpinner timespinner;
	private JButton stop;
	
	

	public Settings(GameViewController controlador)
	{
		super();
		this.controlador = controlador;
		initGUI();
	}

	private void initGUI()
	{
		setOpaque(true);
		
		rePlayButton = new JButton();
		rePlayButton.setIcon(new ImageIcon(Utils.loadImage("pause.png")));
		rePlayButton.setToolTipText("pause/play game.");
		rePlayButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controlador.getActiveGame())
				{
					if(controlador.buttonrePLay())
						rePlayButton.setIcon(new ImageIcon(Utils.loadImage("play.png")));
					else
						rePlayButton.setIcon(new ImageIcon(Utils.loadImage("pause.png")));
				}
			}
		});
		add(rePlayButton);
		
		
		
		
		undoButton = new JButton();
		undoButton.setEnabled(false);
		undoButton.setIcon(new ImageIcon(Utils.loadImage("undo.png")));
		undoButton.setToolTipText("Undo the 2 last actions.");
		undoButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.buttonUndo();
			}
		});
		add(undoButton);
		
		//Boton Random
		randomButton = new JButton();
		randomButton.setIcon(new ImageIcon(Utils.loadImage("random.png")));
		randomButton.setToolTipText("Random Movement");
		randomButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
					controlador.makeRandomMove();
			}	
		});
		add(randomButton);
		
		//Boton Smart
		smartButton = new JButton();
		smartButton.setIcon(new ImageIcon(Utils.loadImage("smart.png")));
		smartButton.setToolTipText("Smart Movement");
		smartButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
					controlador.makeSmartMove();
			}	
		});
		add(smartButton);
		
		//Boton Restart
		restartButton = new JButton();
		restartButton.setIcon(new ImageIcon(Utils.loadImage("restart.png")));
		restartButton.setToolTipText("Restart the game");
		restartButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.buttonRestartGame();
			}	
		});
		add(restartButton);
		
		//Boton Off
		offButton = new JButton();
		offButton.setIcon(new ImageIcon(Utils.loadImage("off.png")));
		offButton.setToolTipText("Close the game");
		offButton.addActionListener(
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame Stop = new JFrame();
				int eleccion = JOptionPane.showOptionDialog(Stop,"¿Estas seguro que quieres salir del juego?",
				"",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
				if(eleccion == JOptionPane.YES_OPTION)
					System.exit(1);
			}
		});
		add(offButton);
		
		//Modo de Juego
		JLabel modo = new JLabel("Player Mode: ");
		add(modo);
		
		//Modo de Juego Combo
		ComboPlayer = new JComboBox<PlayerMode>(new DefaultComboBoxModel<PlayerMode>()){

			private static final long serialVersionUID = 1L;
			public void setSelectedItem(Object o){
				super.setSelectedItem(o);
				controlador.setComboBox((PlayerMode)o);
			}
		};
		ComboPlayer.addItem(PlayerMode.MANUAL);
		ComboPlayer.addItem(PlayerMode.RANDOM);
		ComboPlayer.addItem(PlayerMode.SMART);
		ComboPlayer.setSelectedItem(controlador.getPlayerMode());
		
		ComboPlayer.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				ComboPlayer.getSelectedItem();
			}
			
		});
		add(ComboPlayer);
		
			
		//-------------------------------------------------------------------------------------------------------
		//Smart panel creator
		
		JPanel smartPanel = new JPanel();
		
		smartPanel.setBorder(BorderFactory.createTitledBorder("Smart Moves"));
		brain = new JLabel(new ImageIcon(Utils.loadImage("brain.png")));
		brain.setOpaque(true);
		smartPanel.add(brain);
		spinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
		spinner.addChangeListener((e) -> controlador.setThreads((Integer)spinner.getValue()));
		smartPanel.add(spinner);
		smartPanel.add(new JLabel("thread/s"));
		clock = new JLabel(new ImageIcon(Utils.loadImage("timer.png")));
		smartPanel.add(clock);
		timespinner = new JSpinner(new SpinnerNumberModel(500,500,5000,500));
		
		timespinner.addChangeListener((e) -> controlador.setTimeOut((Integer)timespinner.getValue()));
		smartPanel.add(timespinner);
		smartPanel.add(new JLabel(" ms"));
		stop = new JButton();
		stop.setIcon(new ImageIcon(Utils.loadImage("stop.png")));
		stop.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.cancelSmartMove();		
			}
		});
		stop.setEnabled(false);
		smartPanel.add(this.stop);
		add(smartPanel);
	}
	
	public void statusButtons(boolean p)
	{
		smartButton.setEnabled(p);
		randomButton.setEnabled(p);
		undoButton.setEnabled(p);
	}
	
	public void setThinking(boolean thinking) 
	{
		brain.setBackground(thinking ? Color.YELLOW : getBackground());
		stop.setEnabled(thinking);	
	}

	public void notYet() {
		undoButton.setEnabled(false);
	}
}