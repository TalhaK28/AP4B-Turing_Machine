package game;

import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;


import javax.swing.*;
import java.awt.*;

import java.util.List;
import java.util.ArrayList;


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

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


import java.awt.Dimension;

import java.util.Timer;
import java.util.TimerTask;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.InputStream;
 

import java.util.LinkedList;

public class Menu extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Create the frame.
     */
    public Menu(boolean firstOpen) {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/image/Turing-logo.PNG")));
        // Définir la taille de la fenêtre en plein écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);

        // Masquer la barre de titre de la fenêtre
        setUndecorated(true);

        // Background Image
        ImageIcon backgroundImg = new ImageIcon("src/image/fond_blanc.png");
        contentPane = new JPanel() {
        	 private static final long serialVersionUID = 1L; // Ajoutez ce champ pour résoudre le warning
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
        	 private static final long serialVersionUID = 1L; // Ajoutez ce champ pour résoudre le warning
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
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créez un JDialog personnalisé
                JDialog dialog = new JDialog(Menu.this, "Paramètres de jeu", true);
                
                dialog.setSize(400, 300);
                dialog.setLocationRelativeTo(Menu.this);
                dialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(10, 10, 10, 10);

                // Ajoutez une liste déroulante pour le choix de la difficulté
                JLabel difficultyLabel = new JLabel("Choisissez une difficulté :");
                gbc.gridx = 0;
                gbc.gridy = 0;
                dialog.add(difficultyLabel, gbc);

                String[] difficulties = {"Facile", "Normal", "Difficile"};
                JComboBox<String> difficultyComboBox = new JComboBox<>(difficulties);
                gbc.gridx = 1;
                gbc.gridy = 0;
                dialog.add(difficultyComboBox, gbc);

                // Ajoutez un champ pour sélectionner le nombre de joueurs
                JLabel playerCountLabel = new JLabel("Nombre de joueurs :");
                gbc.gridx = 0;
                gbc.gridy = 1;
                dialog.add(playerCountLabel, gbc);

                JSpinner playerCountSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
                gbc.gridx = 1;
                gbc.gridy = 1;
                dialog.add(playerCountSpinner, gbc);

                // Panel pour afficher les champs des noms des joueurs dynamiquement
                JPanel playerNamesPanel = new JPanel();
                playerNamesPanel.setLayout(new GridLayout(4, 1, 5, 5)); // Max 4 champs pour 4 joueurs
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                dialog.add(playerNamesPanel, gbc);
                
                JTextField playerTextField1 = new JTextField(15);
                try {
                    // Charger la police pour le champ de texte
                    InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
                    if (is != null) {
                        Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f); // Taille 15
                        playerTextField1.setFont(customFont);
                    } else {
                        System.out.println("La ressource de la police n'a pas été trouvée !");
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
                playerNamesPanel.add(playerTextField1);

                // Ajoutez un écouteur pour mettre à jour les champs en fonction du nombre de joueurs
                playerCountSpinner.addChangeListener(e1 -> {
                	 int playerCount = (int) playerCountSpinner.getValue();
                     playerNamesPanel.removeAll();
                     for (int i = 1; i <= playerCount; i++) {
                         JLabel playerLabel = new JLabel("Joueur " + i + " :");
                         try {
                             // Charge la police depuis le dossier "src/font"
                             InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
                             if (is != null) {
                                 Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f); // Taille 15
                                 playerLabel.setFont(customFont);
                             } else {
                                 System.out.println("La ressource de la police n'a pas été trouvée !");
                             }
                         } catch (Exception s) {
                             s.printStackTrace();
                         }
                         playerNamesPanel.add(playerLabel);
                         
                         JTextField playerTextField = new JTextField(15);
                         try {
                             // Charge la police depuis le dossier "src/font"
                             InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
                             if (is != null) {
                                 Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f); // Taille 15
                                 playerTextField.setFont(customFont);
                             } else {
                                 System.out.println("La ressource de la police n'a pas été trouvée !");
                             }
                         } catch (Exception s) {
                             s.printStackTrace();
                         }
                         playerNamesPanel.add(playerTextField);
                    }
                    playerNamesPanel.revalidate();
                    playerNamesPanel.repaint();
                });

                // Ajoutez des boutons OK et Annuler
                JPanel buttonPanel = new JPanel();
                JButton okButton = new JButton("OK") {
                	 private static final long serialVersionUID = 1L; // Ajoutez ce champ pour résoudre le warning
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
                };;
                
                JButton cancelButton = new JButton("Annuler"){
                	 private static final long serialVersionUID = 1L; // Ajoutez ce champ pour résoudre le warning
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
                };;
                buttonPanel.add(okButton);
                buttonPanel.add(cancelButton);
                
                try {
                    // Charge la police depuis le dossier "src/font"
                    InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
                    if (is != null) {
                        Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(15f); // Taille 15
                        okButton.setFont(customFont);
                        cancelButton.setFont(customFont);
                        playerCountLabel.setFont(customFont);
                        difficultyLabel.setFont(customFont);
                        playerCountSpinner.setFont(customFont);
                        difficultyComboBox.setFont(customFont);
                    } else {
                        System.out.println("La ressource de la police n'a pas été trouvée !");
                    }
                } catch (Exception s) {
                    s.printStackTrace();
                }
                
                okButton.setForeground(Color.WHITE);
                okButton.setBackground(new Color(0, 153, 204)); // Bleu clair
                okButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
                okButton.setFocusPainted(false); // Retirer le focus
                

                // Effet de survol
                okButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                    	okButton.setBackground(new Color(0, 102, 153)); // Bleu plus foncé
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    	okButton.setBackground(new Color(0, 153, 204)); // Retour au bleu clair
                    }
                });
                
                
             // Styles de base pour le bouton Exit
                cancelButton.setForeground(Color.WHITE);  // Couleur du texte
                cancelButton.setBackground(new Color(255, 223, 51)); // Jaune clair
                cancelButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
                cancelButton.setFocusPainted(false); // Retirer le focus

                // Effet de survol pour le bouton Exit
                cancelButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                    	cancelButton.setBackground(new Color(255, 204, 0)); // Jaune plus foncé
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    	cancelButton.setBackground(new Color(255, 223, 51)); // Retour au jaune clair
                    }
                });


                gbc.gridx = 0;
                gbc.gridy = 3;
                gbc.gridwidth = 2;
                dialog.add(buttonPanel, gbc);

                // Action pour le bouton OK
                okButton.addActionListener(e1 -> {
                    String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                    int sDifficulty = 0;
                    
                    switch(selectedDifficulty) {
                    case "Facile" :
                    	sDifficulty = 1;
                    	break;
                    case "Normal" :
                    	sDifficulty = 2;
                    	break;
                    case "Difficile" :
                    	sDifficulty = 3;
                    	break;
                    }
                    
                    int playerCount = (int) playerCountSpinner.getValue();

                 // Récupérez les noms des joueurs
                 Component[] components = playerNamesPanel.getComponents();
                 List<String> playerNames = new ArrayList<>();
                 int index = 0;
                 for (Component comp : components) {
                     if (comp instanceof JTextField) {
                         index++;
                         String playerName = ((JTextField) comp).getText().trim();  // Utilisation de .trim() pour enlever les espaces inutiles
                         if (!playerName.isEmpty()) {  // Vérifier si le champ n'est pas vide
                             playerNames.add(playerName);
                         } else {
                             String name = "Agent " + index;
                             playerNames.add(name);
                         }
                     }
                 }

                    
                    LinkedList<Joueur> Jliste = new LinkedList<>();
                    
                    for(int i=0; i<playerCount;i++) {
                    	Joueur player = new Joueur(playerNames.get(i));
                    	Jliste.add(player);
                    }
                    
                    dialog.dispose();
                    
                    if(firstOpen) {
                    playVideo(dialog, Jliste, sDifficulty);
                    
                 // Créer un Timer et une tâche
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            // Action à faire après le délai
                        	
                            dispose();
                        }
                    };

                    // Délai de 2 secondes (2000 millisecondes)
                    timer.schedule(task, 3000); 
                    }else {
                    	JeuInterface frame = new JeuInterface(Jliste, sDifficulty);
                        frame.setVisible(true);
                        dispose();
                    }
                    
                    
                    

                    
                });

                // Action pour le bouton Annuler
                cancelButton.addActionListener(e1 -> dialog.dispose());

                dialog.setVisible(true);
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
        JLabel producedByLabel = new JLabel("Produit par Samuel, Nizar, Gaëlle et Talha");
        producedByLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        producedByLabel.setFont(new Font("Arial", Font.BOLD, 12));
        producedByLabel.setHorizontalAlignment(SwingConstants.CENTER);

        /// Exit button
        JButton exitButton = new JButton("Quitter") {
        	 private static final long serialVersionUID = 1L; // Ajoutez ce champ pour résoudre le warning
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
        
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE) // Image centrée
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addGroup(gl_contentPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
                        .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 369, GroupLayout.PREFERRED_SIZE)
                    )
                )
                .addComponent(producedByLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createSequentialGroup()
                .addGap(46) // Espace au-dessus de l'image
                .addComponent(titleLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE) // Image
                .addGap(30) // Espace en dessous de l'image
                .addComponent(startButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                .addGap(28) // Espace en dessous du bouton Start
                .addComponent(exitButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                .addGap(23) // Espace en dessous du bouton Exit
                .addComponent(producedByLabel)
                .addGap(52) // Espace final
        );
        contentPane.setLayout(gl_contentPane);
    }

    private void playVideo(Dialog dialog, LinkedList<Joueur> Jliste, int sDifficulty) {
        // Lancer JavaFX dans un thread séparé en initialisant le toolkit JavaFX
        SwingUtilities.invokeLater(() -> {
            // Initialiser JavaFX si ce n'est pas encore fait
            JFXPanel jfxPanel = new JFXPanel(); // Cela initialise le toolkit JavaFX

            // Créer le JFrame pour afficher la vidéo
            JFrame videoFrame = new JFrame("Vidéo");
            videoFrame.setSize(800, 600);
            videoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Créer et ajouter la scène JavaFX
            Scene scene = createVideoScene(dialog, Jliste, sDifficulty, videoFrame);
            jfxPanel.setScene(scene);
            videoFrame.add(jfxPanel);

            // Passer en plein écran
            videoFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiser la fenêtre
            videoFrame.setUndecorated(true); // Enlever la barre de titre
            videoFrame.setResizable(false); // Désactiver le redimensionnement de la fenêtre
            videoFrame.setVisible(true);
        });
    }

    private Scene createVideoScene(Dialog dialog, LinkedList<Joueur> Jliste, int sDifficulty, JFrame videoFrame) {
        
        String videoPath = "src/video/intro_Video.mp4"; 
        Media media = new Media(new File(videoPath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Créer un conteneur pour la scène JavaFX
        StackPane root = new StackPane();
        root.getChildren().add(mediaView);  // Ajouter le MediaView dans le conteneur

        // Créer un bouton transparent pour passer à la fin de la vidéo
        Button skipButton = new Button("Passer la vidéo");
        skipButton.setStyle(
        	    "-fx-background-color: transparent; " +
        	    "-fx-text-fill: white; " + 
        	    "-fx-font-family: 'Arial'; " + // Police de caractères
        	    "-fx-font-size: 18px; " + // Taille du texte
        	    "-fx-font-weight: bold; " + // Gras
        	    "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.8), 5, 0, 2, 2); " + // Ombre portée
        	    "-fx-padding: 10px 20px; " + // Marges autour du texte
        	    "-fx-border-width: 2px; " + // Largeur de la bordure
        	    "-fx-border-color: white; " + // Couleur de la bordure
        	    "-fx-border-radius: 10px;" // Bordure arrondie
        	);
        skipButton.setOnAction(e -> skipToEnd(mediaPlayer));  // Passer à la fin de la vidéo

        // Placer le bouton en bas à droite
        StackPane.setAlignment(skipButton, Pos.BOTTOM_RIGHT);
        root.getChildren().add(skipButton);

        // Configurer la lecture automatique
        mediaPlayer.setAutoPlay(true);

        
        JeuInterface frame = new JeuInterface(Jliste, sDifficulty);
        
        // Ajouter un événement pour surveiller la fin de la vidéo
        mediaPlayer.setOnEndOfMedia(() -> {
        	
            Platform.runLater(() -> {
                // Fermer la fenêtre vidéo et afficher le jeu 
                dialog.dispose(); // Fermer le dialogue vidéo
                mediaPlayer.dispose(); // Libère les ressources du MediaPlayer
                // Créer et afficher le jeu
                
                frame.setVisible(true);
                videoFrame.dispose(); // Fermer la fenêtre vidéo

                
            });
        });

        // Retourner la scène avec le conteneur
        return new Scene(root, 800, 600);
    }

    private void skipToEnd(MediaPlayer mediaPlayer) {
        // Avancer la vidéo jusqu'à la fin
        if (mediaPlayer != null) {
            mediaPlayer.seek(mediaPlayer.getMedia().getDuration());
        }
    }



}