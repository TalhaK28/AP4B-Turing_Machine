package game;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.ImageIcon;
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

		setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/image/Turing-logo.PNG")));
	       
		 // Définir la taille de la fenêtre en plein écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        // Masquer la barre de titre de la fenêtre
        setUndecorated(true);


		controller = new Jeu(Liste, diff);
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		
		// Background Image
        ImageIcon backgroundImg = new ImageIcon("src/image/fond_blanc.png");
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        setContentPane(contentPane);
	}
	

}
