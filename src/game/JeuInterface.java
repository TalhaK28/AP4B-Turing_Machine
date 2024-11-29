package game;

import java.awt.EventQueue;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class JeuInterface extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Jeu controller;

	/**
	 * Create the frame.
	 */
	public JeuInterface(LinkedList<Joueur> Liste, int diff) {
		
		controller = new Jeu(Liste, diff);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
	}

}
