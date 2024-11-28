package game;

import javax.swing.*;
import java.awt.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;

import javax.swing.border.EmptyBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import javax.swing.GroupLayout.Alignment;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public Menu() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/image/Turing-logo.PNG")));
        // Définir la taille de la fenêtre en plein écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        // Masquer la barre de titre de la fenêtre
        setUndecorated(true);

        // Background Image
        ImageIcon backgroundImg = new ImageIcon("src/image/fond_blanc.png");
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setForeground(Color.WHITE);
        contentPane.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        setContentPane(contentPane);

        // Title label
        JLabel titleLabel = new JLabel("");
        titleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        titleLabel.setIcon(new ImageIcon(Menu.class.getResource("/image/Turing-logo.PNG")));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Old English Text MT", Font.PLAIN, 51));

        // Start button
        JButton startButton = new JButton("Démarrer") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond arrondi
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Texte
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disposeWithoutExiting();
                //GameInterface frame = new GameInterface();
               // frame.setVisible(true);
            }
        });

        try {
            // Charge la police depuis le dossier "src/font"
            InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(35f); // Taille 28
                startButton.setFont(customFont);
            } else {
                System.out.println("La ressource de la police n'a pas été trouvée !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        // Styles de base;
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(0, 153, 204)); // Bleu clair
        startButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        startButton.setFocusPainted(false); // Retirer le focus
        startButton.setOpaque(false); // Fond transparent pour le rendu personnalisé
        startButton.setContentAreaFilled(false); // Empêcher le remplissage par défaut

        // Effet de survol
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                startButton.setBackground(new Color(0, 102, 153)); // Bleu plus foncé
            }

            @Override
            public void mouseExited(MouseEvent e) {
                startButton.setBackground(new Color(0, 153, 204)); // Retour au bleu clair
            }
        });

        // Produced by label
        JLabel producedByLabel = new JLabel("Produit par Samuel, Nizard, Gaëlle et Talha");
        producedByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        producedByLabel.setFont(new Font("Arial", Font.BOLD, 12));
        producedByLabel.setHorizontalAlignment(SwingConstants.CENTER);

        /// Exit button
        JButton exitButton = new JButton("Exit") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond arrondi
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Texte
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        try {
            // Charge la police depuis le dossier "src/font"
            InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
            if (is != null) {
                Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(35f); // Taille 35
                exitButton.setFont(customFont);
                producedByLabel.setFont(customFont);
            } else {
                System.out.println("La ressource de la police n'a pas été trouvée !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Styles de base pour le bouton Exit
        exitButton.setForeground(Color.WHITE);  // Couleur du texte
        exitButton.setBackground(new Color(255, 223, 51)); // Jaune clair
        exitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        exitButton.setFocusPainted(false); // Retirer le focus
        exitButton.setOpaque(false); // Fond transparent pour le rendu personnalisé
        exitButton.setContentAreaFilled(false); // Empêcher le remplissage par défaut

        // Effet de survol pour le bouton Exit
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                exitButton.setBackground(new Color(255, 204, 0)); // Jaune plus foncé
            }

            @Override
            public void mouseExited(MouseEvent e) {
                exitButton.setBackground(new Color(255, 223, 51)); // Retour au jaune clair
            }
        });

        
        
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        // Layout
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap(200, Short.MAX_VALUE)
        			.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 600, Short.MAX_VALUE)
        			.addGap(200))
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap(773, Short.MAX_VALUE)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
        				.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE))
        			.addGap(456))
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(107)
        			.addComponent(producedByLabel, GroupLayout.DEFAULT_SIZE, 1376, Short.MAX_VALUE)
        			.addGap(115))
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addGap(46)
        			.addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, 380, GroupLayout.PREFERRED_SIZE)
        			.addGap(30)
        			.addComponent(startButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
        			.addGap(28)
        			.addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
        			.addGap(23)
        			.addComponent(producedByLabel)
        			.addGap(52))
        );
        contentPane.setLayout(gl_contentPane);
    }

    public void disposeWithoutExiting() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dispose();
    }


}
