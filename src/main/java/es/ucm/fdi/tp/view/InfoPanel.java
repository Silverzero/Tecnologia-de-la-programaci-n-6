package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.ColorChooser.ColorInterface;

public class InfoPanel<S extends GameState<S, A>, A extends GameAction<S,A>>extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextArea area;
	private ColorInterface controlador;

	public InfoPanel(ColorInterface c) {
		super();
		this.controlador = c;
		initGUI();
	}

	private void initGUI()
	{
		/*Panel SUPERIOR*/
		//***********************************************************************
		//this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder());
		setPreferredSize(new Dimension(230,100));
		
		JPanel PanelSuperior = new JPanel(new BorderLayout());
		PanelSuperior.setMaximumSize(new Dimension(300,600));
		area = new JTextArea(15,20);
		area.setEditable(false);
		JScrollPane StatusMessages = new JScrollPane(area,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		StatusMessages.setBorder(BorderFactory.createTitledBorder("Status Messages: "));
		
		PanelSuperior.add(StatusMessages,BorderLayout.CENTER);
		PanelSuperior.setVisible(true);
		add(PanelSuperior,BorderLayout.NORTH);
		
	
		
		
		/*Panel INFERIOR*/
		//***********************************************************************
		JPanel panelInferior = new JPanel(new BorderLayout()); 
		panelInferior.setBorder(BorderFactory.createTitledBorder("Player Information"));
		panelInferior.setMaximumSize(new Dimension(300,100));
		panelInferior.add(new ColorPanel(this.controlador), BorderLayout.CENTER);
		add(panelInferior,BorderLayout.CENTER);

		
	}
	
	public void showInfoMessage(String message)
	{
		area.append(message);
	}
	
	public void resetInfoMessage()
	{
		area.setText("");
	}
}
