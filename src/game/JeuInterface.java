package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;

import javax.swing.text.*;


import javax.swing.event.*;
import java.io.InputStream;

    public class JeuInterface extends JFrame {

        private static final long serialVersionUID = 1L;
        private JPanel contentPane;
        private Jeu controller;
        private int manche = 1;
        private int currentPlayerIndex = 0;

        private JLabel mancheLabel;
        private JLabel playerTurnLabel;
        
        
        private int[] essaisParJoueur; // Taille basée sur la liste de joueurs
        private Set<Integer> cartesTestees; // Ensemble des cartes déjà testées cette manche

        private boolean triangle;
        private boolean carree;
        private boolean rond;
        
     // Créer une HashMap pour stocker les descriptifs HTML initiaux
        private Map<JLabel, String> initialHtmlMap = new HashMap<>();
        private char[] testResults;
        
        private JLabel[] lifeLabels;
        private String[] lifeStates = new String[3]; // "dispo" ou "utilise"


        public JeuInterface(LinkedList<Joueur> Liste, int diff) {
        

            // Configurer l'icône de la fenêtre
            setIconImage(Toolkit.getDefaultToolkit().getImage(JeuInterface.class.getResource("/image/Turing-logo.PNG")));

            // Définir la taille de la fenêtre en plein écran
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds(0, 0, screenSize.width, screenSize.height);

            // Masquer la barre de titre de la fenêtre
            setUndecorated(true);

            // Initialiser le contrôleur
            controller = new Jeu(Liste, diff);
            
            essaisParJoueur = new int[controller.getListeActJoueur().size()];
        	cartesTestees = new HashSet<>();
        	
        	this.triangle = false;
        	this.carree = false;
        	this.rond = false;
        	
        	this.testResults = new char[controller.getNombreDeCartes()];
        	for(int i=0;i<diff; i++) {
        		testResults[i]='2';
        	}

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Ajouter une image de fond
            ImageIcon backgroundImg = new ImageIcon("src/image/fond.png");
            contentPane = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            };
            contentPane.setLayout(new BorderLayout());
            setContentPane(contentPane);

            try {
                // Charge la police personnalisée depuis le dossier "src/font"
                InputStream is = Menu.class.getResourceAsStream("/font/Hexaplex.otf");
                if (is != null) {
                    Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(18f); // Taille par défaut
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(customFont); // Enregistre la police dans l'environnement graphique

                    // Applique la police globalement
                    UIManager.put("Button.font", customFont);
                    UIManager.put("Label.font", customFont);
	                    UIManager.put("Panel.font", customFont);
	                    UIManager.put("TextField.font", customFont);
	                } else {
	                    System.out.println("La ressource de la police n'a pas été trouvée !");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Redessiner tous les composants existants pour appliquer la nouvelle police
            SwingUtilities.updateComponentTreeUI(contentPane);

         // ** Panneau supérieur : Manche, joueur actif, et vies **
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS)); // Organisation verticale
            topPanel.setOpaque(false);

            // Panneau pour les labels (Manche et Joueur actif)
            JPanel labelsPanel = new JPanel(new GridLayout(2, 1)); // 2 lignes, 1 colonne
            labelsPanel.setOpaque(false);

            // Manche
            mancheLabel = new JLabel("Manche : " + manche, SwingConstants.CENTER);
            mancheLabel.setFont(new Font("Hexaplex", Font.BOLD, 32));
            labelsPanel.add(mancheLabel);

            // Tour du joueur
            int indexPlayer = currentPlayerIndex + 1;
            playerTurnLabel = new JLabel("Tour du joueur " + indexPlayer + " : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo(), SwingConstants.CENTER);
            playerTurnLabel.setFont(new Font("Hexaplex", Font.BOLD, 24));
            labelsPanel.add(playerTurnLabel);

            // Ajouter les labels au panneau supérieur
            topPanel.add(labelsPanel);

         // Panneau des vies
            JPanel livesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            livesPanel.setOpaque(false);

            // Ajouter 3 JLabels pour les vies avec redimensionnement
            lifeLabels = new JLabel[3];
            lifeStates = new String[3];  // Initialisation des états des vies
            int iconWidth = 70; // Largeur de l'icône
            int iconHeight = 70; // Hauteur de l'icône

            for (int i = 0; i < 3; i++) {
                // Initialiser l'état de chaque vie
                lifeStates[i] = "dispo";  // "dispo" signifie que la vie est disponible
                
                ImageIcon originalIcon = new ImageIcon("src/image/testDispo.png");
                originalIcon.setDescription("testDispo");  // Ajout de la description
                Image resizedImage = originalIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH);
                lifeLabels[i] = new JLabel(new ImageIcon(resizedImage));
                
                livesPanel.add(lifeLabels[i]);
            
            }

            // Ajouter le panneau des vies au panneau supérieur
            topPanel.add(livesPanel, BorderLayout.EAST);

            // Ajouter le panneau supérieur au contentPane
            contentPane.add(topPanel, BorderLayout.NORTH);


         // ** Panneau central : Cartes avec boutons "Tester" **
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new GridLayout(0, controller.getListeRegle().getSize(), 20, 20)); // Espacement plus large entre les cartes
            cardsPanel.setOpaque(false);

            for (int i = 0; i < controller.getListeRegle().getSize(); i++) {
                Carte carte = controller.getListeRegle().getCarteIndex(i);

                String descriptifHtml = carte.getDescriptif()
                        .replace("triangle", "<img src='file:src/image/triangle.png' width='25' height='25'>") // Images plus grandes
                        .replace("carree", "<img src='file:src/image/carree.png' width='25' height='25'>")
                        .replace("rond", "<img src='file:src/image/rond.png' width='25' height='25'>")
                        .replace("\n", "<br>");
                
                

                // Création du panneau de la carte
                JPanel cardPanel = new JPanel();
                cardPanel.setLayout(new BorderLayout());
                cardPanel.setOpaque(false);
                cardPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 255), 2)); // Bordure de carte légère en bleu clair

                // Ajout d'une ombre portée
                cardPanel.setBackground(new Color(255, 255, 255)); // Fond blanc pour le panneau
                cardPanel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 2)); // Bordure légère et discrète
                cardPanel.setBorder(BorderFactory.createCompoundBorder(cardPanel.getBorder(), 
                        BorderFactory.createEmptyBorder(11, 11, 11, 11))); // Espacement interne

                // Label pour la description de la carte
                JLabel cardLabel = new JLabel("<html><div style='text-align: center;'>" + descriptifHtml + "</div></html>");
                cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
                cardLabel.setFont(new Font("Hexaplex", Font.BOLD, 22)); // Utilisation de la police Hexaplex;
                cardLabel.setForeground(new Color(50, 50, 50)); // Couleur de texte plus douce
                cardLabel.setBackground(new Color(240, 240, 240)); // Fond léger pour mettre en valeur le texte
                cardLabel.setOpaque(true); // Nécessaire pour afficher l'arrière-plan
                cardPanel.add(cardLabel, BorderLayout.CENTER);
                
                // Stocker le texte HTML initial dans la HashMap
                initialHtmlMap.put(cardLabel, cardLabel.getText());
                
                
             // ** Bouton "Tester" **
                JButton testButton = new JButton("Tester");
                testButton.setFont(new Font("Hexaplex", Font.BOLD, 16)); // Utilisation de la police Hexaplex
                testButton.setBackground(new Color(0, 153, 204)); // Bleu clair pour le fond
                testButton.setForeground(Color.WHITE); // Texte en blanc
                testButton.setPreferredSize(new Dimension(150, 40)); // Taille du bouton

                // Bordure arrondie et effets visuels
                testButton.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
                testButton.setFocusPainted(false); // Retirer l'effet de focus sur le bouton
                testButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur main lors du survol

             // Effet de survol (hover) pour le bouton
                testButton.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        testButton.setBackground(new Color(0, 204, 255)); // Couleur survolée
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        testButton.setBackground(new Color(0, 153, 204)); // Retour à la couleur d'origine
                    }
                });

                // Ajouter un écouteur d'action pour le bouton
                testButton.addActionListener(new TestButtonActionListener(i));

                // Ajouter le bouton au bas de la carte
                cardPanel.add(testButton, BorderLayout.SOUTH);

                // Ajouter la carte au panneau principal
                cardsPanel.add(cardPanel);
            }

            contentPane.add(cardsPanel, BorderLayout.CENTER);


         // ** Panneau inférieur : Formes et bouton Valider **
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BorderLayout());
            bottomPanel.setOpaque(false);

            // ** Panneau des figures **
            JPanel figuresPanel = new JPanel();
            figuresPanel.setLayout(new GridLayout(1, 3, 20, 20)); // Espacement plus large
            figuresPanel.setOpaque(false);

            // Création des panneaux pour chaque forme
            JPanel trianglePanel = createFigurePanel("src/image/triangle.png", "Triangle", "1");
            JPanel carreePanel = createFigurePanel("src/image/carree.png", "Carré", "2");
            JPanel rondPanel = createFigurePanel("src/image/rond.png", "Rond", "3");

            // Ajout des panneaux au panneau des figures
            figuresPanel.add(trianglePanel);
            figuresPanel.add(carreePanel);
            figuresPanel.add(rondPanel);

            // Ajout des formes au bas du panneau
            bottomPanel.add(figuresPanel, BorderLayout.CENTER);

            // ** Panneau des boutons : Show Menu et Valider Mon Tour **
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            buttonPanel.setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour un agencement plus flexible

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(0, 20, 0, 20); // Espacement horizontal pour les boutons

            // ** Bouton "Show Menu" **
            JButton showMenuButton = new JButton("Afficher le Menu");
            showMenuButton.setPreferredSize(new Dimension(150, 50));
            showMenuButton.setBackground(new Color(255, 99, 71)); // Couleur pour attirer l'attention
            showMenuButton.setForeground(Color.WHITE);
            showMenuButton.setFont(new Font("Hexaplex", Font.BOLD, 18));
            showMenuButton.setBorder(BorderFactory.createLineBorder(new Color(255, 99, 71), 2));
            showMenuButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur main au survol

            // Effet de survol pour le bouton "Show Menu"
            showMenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    showMenuButton.setBackground(new Color(255, 69, 0)); // Couleur au survol
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    showMenuButton.setBackground(new Color(255, 99, 71)); // Retour à la couleur initiale
                }
            });

            showMenuButton.addActionListener(e -> showMenu()); // Implémentez showMenu()

            // Ajouter le bouton "Show Menu" à la grille
            gbc.gridx = 0; // Positionnement à gauche
            buttonPanel.add(showMenuButton, gbc);

            // ** Bouton "Valider Mon Tour" **
            JButton validateButton = new JButton("Valider Mon Tour");
            validateButton.setPreferredSize(new Dimension(250, 50)); // Bouton plus grand
            validateButton.setBackground(new Color(0, 102, 204)); // Bleu foncé pour attirer l'attention
            validateButton.setForeground(Color.WHITE);
            validateButton.setFont(new Font("Hexaplex", Font.BOLD, 24)); // Utilisation de la police Hexaplex
            validateButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); // Bordure arrondie
            validateButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Curseur main au survol

            // Effet de survol pour le bouton "Valider"
            validateButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    validateButton.setBackground(new Color(0, 122, 204)); // Couleur au survol
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    validateButton.setBackground(new Color(0, 102, 204)); // Retour à la couleur initiale
                }
            });

            validateButton.addActionListener(e -> validateTurn());

            // Ajouter le bouton "Valider Mon Tour" à la grille, centré
            gbc.gridx = 1; // Positionnement au centre
            gbc.gridwidth = 2; // Le bouton prendra 2 colonnes pour être centré
            buttonPanel.add(validateButton, gbc);

            // Ajouter le panneau des boutons au panneau inférieur
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

            // Ajouter le panneau inférieur au contentPane
            contentPane.add(bottomPanel, BorderLayout.SOUTH);

        }


        private JPanel createFigurePanel(String imagePath, String labelText, String index) {
        	JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setOpaque(false);

           
            // Créer l'image avec JLabel
            ImageIcon newIcon = new ImageIcon(imagePath); // Charger l'image depuis le chemin
            Image resizedImage = newIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH); // Redimensionner l'image
            ImageIcon resizedIcon = new ImageIcon(resizedImage); // Créer une ImageIcon à partir de l'image redimensionnée
            JLabel imageLabel = new JLabel(resizedIcon); // Ajouter l'ImageIcon au JLabel
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrer l'image horizontalement
            panel.add(imageLabel, BorderLayout.CENTER); // Ajouter le JLabel au panel


            // Créer le JTextField
            JTextField textField = new JTextField();
            textField.setHorizontalAlignment(SwingConstants.CENTER);
            textField.setToolTipText("Entrez la valeur pour " + labelText);
            panel.add(textField, BorderLayout.SOUTH);
            textField.setName(index);

            // Limiter l'entrée à un seul chiffre
            limitToOneDigit(textField);

            // Ajouter un DocumentListener pour détecter les changements
            addTextFieldListener(textField);

            
            return panel;
        }
        
        

        private void updateDigitStatus(JTextField textField, boolean isInserted) {
    switch (textField.getName()) {
        case "1":
            this.triangle = isInserted;
            break;
        case "2":
            this.carree = isInserted;
            break;
        case "3":
            this.rond = isInserted;
            break;
    }
}

        
        
        private void addTextFieldListener(JTextField textField) {
            DocumentListener listener = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateDigitStatus(textField, true);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateDigitStatus(textField, false);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    // No style updates required
                }
            };

            textField.getDocument().addDocumentListener(listener);
            
        }

    
        


    private void validateTurn() {
        try {
        	
        	resetLives();
        	resetAllCards();
        	resetTextFields();
        	cartesTestees.clear();
        	

        	// Convertir le tableau char[] en String lisible
        	String readableResults = String.valueOf(this.testResults);

        	
        	if(controller.getCarree()!=0 && controller.getTriangle()!=0 &&controller.getRond()!=0 ) {
        	controller.getListeActJoueur().get(currentPlayerIndex).addNote(controller.getVal(),readableResults);
        	}
        	
        	// 	Réinitialiser le suivi des résultats des tests
        	for(int i=0;i<this.testResults.length; i++) {
        		testResults[i]='2';
        	}
        	controller.resetVal();
        	

            if (++currentPlayerIndex >= controller.getListeActJoueur().size()) {
                currentPlayerIndex = 0;
                finDeManche();
            } else {
            	int indexPlayer = currentPlayerIndex + 1;
                playerTurnLabel.setText("Tour du joueur "+ indexPlayer + " : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    
    

    private void finDeManche() {
        
        LinkedList<Joueur> joueursAyantPropose = new LinkedList<>();
        LinkedList<String> pseudoPropositionRatee = new LinkedList<>();
        Iterator<Joueur> iterator = controller.getListeActJoueur().iterator();
        
        while (iterator.hasNext()) {
            Joueur joueur = iterator.next();
            int option = JOptionPane.showConfirmDialog(this,
                    "Joueur " + joueur.getPseudo() + ", souhaitez-vous tenter une proposition ?",
                    "Proposition",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                String input = null;
                while (true) {
                    input = JOptionPane.showInputDialog(this, "Entrez votre proposition (exactement 3 chiffres, chacun entre 1 et 5) :");

                    // Si l'utilisateur clique sur "Annuler", passer au joueur suivant
                    if (input == null) {
                        JOptionPane.showMessageDialog(this, "Vous avez choisi de ne pas faire de proposition.", "Information", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }

                    // Vérification de l'entrée
                    if (input.matches("[1-5]{3}")) {
                        // Utiliser la valeur validée
                        controller.setPropositionFinal(Integer.parseInt(input));

                        if (controller.testProp()) {
                            joueursAyantPropose.add(joueur);
                        } else {
                            pseudoPropositionRatee.add(joueur.getPseudo());
                            iterator.remove(); // Supprime l'élément de la liste pendant l'itération
                        }
                        break; // Sortir de la boucle si l'entrée est valide
                    } else {
                        JOptionPane.showMessageDialog(this, "Veuillez entrer exactement 3 chiffres entre 1 et 5.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

    

        if (joueursAyantPropose.isEmpty()) {
        	
        	if(!pseudoPropositionRatee.isEmpty()) {
        		
        		for(int i1=0; i1<pseudoPropositionRatee.size(); i1++) {
        		JOptionPane.showMessageDialog(JeuInterface.this, "Le joueur " + pseudoPropositionRatee.get(i1)+ " s'est trompé" , "Erreur", JOptionPane.ERROR_MESSAGE);
        		}
        		
        		if(controller.getListeActJoueur().isEmpty()) {
            		JOptionPane.showMessageDialog(this,
            	            "Agents ! L'UTBM est perdu, vous n'avez pas été à la hauteur de votre mission",
            	            "Fin de la partie",
            	            JOptionPane.ERROR_MESSAGE);
            	    Menu frame = new Menu(false);
            	    frame.setVisible(true);
            	    dispose();
            	    return;
            	}
        	}
            manche++;
            essaisParJoueur = new int[controller.getListeActJoueur().size()];
            cartesTestees.clear();

            // Réinitialiser l'affichage et commencer une nouvelle manche
            
            mancheLabel.setText("Manche : " + manche);
            currentPlayerIndex = 0;
            int indexPlayer = currentPlayerIndex + 1;
            playerTurnLabel.setText("Tour du joueur "+ indexPlayer +  " :" + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo());

        } else {
        	OptionalInt minNbQuestion = joueursAyantPropose.stream()
        	        .mapToInt(Joueur::getNbQuestion)
        	        .min();

        	if (minNbQuestion.isPresent()) {
        	    int minValue = minNbQuestion.getAsInt();

        	    // Trouver tous les joueurs ayant le nombre minimal de questions
        	    List<Joueur> gagnants = joueursAyantPropose.stream()
        	            .filter(j -> j.getNbQuestion() == minValue)
        	            .toList();

        	    // Construire une chaîne de pseudos pour les gagnants
        	    String pseudosGagnants = gagnants.stream()
        	            .map(Joueur::getPseudo)
        	            .collect(Collectors.joining(", "));

        	    // Afficher les gagnants
        	    JOptionPane.showMessageDialog(this,
        	            "Félicitations vous avez sauvé l'UTBM, agents : " + pseudosGagnants + " avec " + minValue + " demandes !",
        	            "Fin de la partie",
        	            JOptionPane.INFORMATION_MESSAGE);
        	    Menu frame = new Menu(false);
        	    frame.setVisible(true);
        	    dispose();
        	}

        }
    }

    
   private class TestButtonActionListener implements ActionListener {
    private final int index;

    public TestButtonActionListener(int index) {
        this.index = index;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (essaisParJoueur[currentPlayerIndex] >= 3) {
            JOptionPane.showMessageDialog(JeuInterface.this, "Vous avez atteint la limite de 3 essais.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cartesTestees.contains(index)) {
            JOptionPane.showMessageDialog(JeuInterface.this, "Cette carte a déjà été testée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Vérifier si toutes les valeurs sont saisies
        if (!triangle || !carree || !rond) {
            JOptionPane.showMessageDialog(JeuInterface.this, "Vous devez entrer 3 valeurs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setFiguresValues();

        cartesTestees.add(index);
        essaisParJoueur[currentPlayerIndex]++;
        
        
        controller.getListeActJoueur().get(currentPlayerIndex).upNbQuestion();
     

        // Tester la carte et obtenir le résultat
        boolean result = controller.testCarte(index);
        

        updateTestResults(index,result);
        
        // Mettre à jour le descriptif HTML de la carte avec l'icône correspondante
        JPanel cardPanel = (JPanel) ((JButton) e.getSource()).getParent();
        JLabel cardLabel = (JLabel) cardPanel.getComponent(0); // Supposant que le JLabel descriptif est le premier composant du panneau

        String iconHtml = result
                ? "<div style='text-align: center;'><img src='file:src/image/valid.png' width='60' height='60'></div>"
                : "<div style='text-align: center;'><img src='file:src/image/invalid.png' width='60' height='60'></div>";

        String currentHtml = cardLabel.getText();
        currentHtml = currentHtml.replace("</html>", "<br>" + iconHtml + "</html>");

        cardLabel.setText(currentHtml);

        //utiliser une vie
        utiliseTest();
        
        // Bloquer les champs texte
        disableTextFields();
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void disableTextFields() {
        for (Component comp : getAllComponents(getContentPane())) {
            if (comp instanceof JTextField) {
                ((JTextField)comp).setEditable(false);
            }
        }
    }
    

}

   private void updateTestResults(int index, boolean valid) {
	   
	   if(valid) {
       	this.testResults[index] = '1';
       }else {
       	this.testResults[index] = '0';
       }
   }


	

   
   private void limitToOneDigit(JTextField textField) {
	    ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
	        @Override
	        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
	                throws BadLocationException {
	            // Permet de vider le champ de texte
	            if (text.isEmpty()) {
	                super.replace(fb, offset, length, text, attrs);
	            }
	            // Permet d'ajouter un seul chiffre (si le texte n'est pas vide et contient un chiffre)
	            else if (text.matches("[1-5]") && fb.getDocument().getLength() < 1) {
	                super.replace(fb, offset, length, text, attrs);
	            }
	        }
	    });
	}

    
    private void setFiguresValues() {
    int triangle = 0;
    int carree = 0;
    int rond = 0;

    for (Component comp : getAllComponents(getContentPane())) {
        if (comp instanceof JTextField) {
            switch (comp.getName()) {
                case "1":
                    triangle = Integer.parseInt(((JTextField) comp).getText().trim());
                    break;
                case "2":
                    carree = Integer.parseInt(((JTextField) comp).getText().trim());
                    break;
                case "3":
                    rond = Integer.parseInt(((JTextField) comp).getText().trim());
                    break;
                default:
                    break;
            }
        }
    }

    //System.out.println("triangle = " + triangle + ", carree = " + carree + ", rond = " + rond);
    controller.setVal(triangle, carree, rond);
}

// Méthode récursive pour récupérer tous les composants
private java.util.List<Component> getAllComponents(Container container) {
    java.util.List<Component> components = new java.util.ArrayList<>();
    for (Component comp : container.getComponents()) {
        components.add(comp);
        if (comp instanceof Container) {
            components.addAll(getAllComponents((Container) comp));
        }
    }
    return components;
}

//Méthode pour réinitialiser toutes les cartes à leur texte HTML initial
private void resetAllCards() {
 for (Map.Entry<JLabel, String> entry : initialHtmlMap.entrySet()) {
     JLabel cardLabel = entry.getKey();
     String initialHtml = entry.getValue();
     cardLabel.setText(initialHtml);
 } 
}

private void resetTextFields() {
    for (Component comp : getAllComponents(getContentPane())) {
        if (comp instanceof JTextField) {
            JTextField textField = (JTextField) comp;


            // Reset field
            ((JTextField)comp).setEditable(true);
            textField.setText("");
            

            // Revalidate and repaint to update the UI
            textField.revalidate();
            textField.repaint();

        }
    }
}

//Utiliser une vie
private void utiliseTest() {
 for (int i = 0; i < lifeLabels.length; i++) {
     if ("dispo".equals(lifeStates[i])) {  // Vérifie si la vie est disponible
         lifeStates[i] = "utilise";  // Modifie l'état de la vie à "utilise"
         
         // Met à jour l'icône de la vie
         ImageIcon newIcon = new ImageIcon("src/image/testUtilise.png");
         newIcon.setDescription("testUtilise");
         Image resizedImage = newIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
         lifeLabels[i].setIcon(new ImageIcon(resizedImage));
         break;  // On ne modifie qu'une vie à la fois
     }
 }
}

//Réinitialiser les vies
private void resetLives() {
 for (int i = 0; i < lifeLabels.length; i++) {
     lifeStates[i] = "dispo";  // Remet l'état à "dispo"
     
     // Remet l'icône de la vie à son état initial
     ImageIcon newIcon = new ImageIcon("src/image/testDispo.png");
     newIcon.setDescription("testDispo");
     Image resizedImage = newIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
     lifeLabels[i].setIcon(new ImageIcon(resizedImage));
 }
}


private void showMenu() {
	// Création du panneau de menu
	JPanel menuPanel = new JPanel();
	menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

	// Ajouter un texte HTML dans le menu
	String htmlText = this.controller.getListeActJoueur().get(currentPlayerIndex).generateHtmlNoteTable()
	        .replace("triangle", "<img src='file:src/image/triangle.png' width='25' height='25'>") // Images plus grandes
	        .replace("carree", "<img src='file:src/image/carree.png' width='25' height='25'>")
	        .replace("rond", "<img src='file:src/image/rond.png' width='25' height='25'>")
	        .replace("\n", "<br>");

	JLabel htmlLabel = new JLabel("<html><div style='text-align: center;'>" + htmlText + "</div></html>");
	htmlLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT); // Centrer horizontalement le texte
	menuPanel.add(htmlLabel);

	// Créer un sous-panneau pour les boutons
	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centrer les boutons et ajouter un espace entre eux

	// Ajouter le bouton "Quitter la Partie" avec le style du bouton "Annuler"
	JButton quitButton = new JButton("Quitter la Partie") {
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

	quitButton.setForeground(Color.WHITE); // Couleur du texte
	quitButton.setBackground(new Color(255, 223, 51)); // Jaune clair
	quitButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
	quitButton.setFocusPainted(false); // Retirer le focus

	// Effet de survol pour le bouton "Quitter la Partie"
	quitButton.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
	        quitButton.setBackground(new Color(255, 204, 0)); // Jaune plus foncé
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	        quitButton.setBackground(new Color(255, 223, 51)); // Retour au jaune clair
	    }
	});


	buttonPanel.add(quitButton);

	// Ajouter le bouton "Recommencer la Partie" avec le style du bouton "OK"
	JButton restartButton = new JButton("Recommencer") {
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

	restartButton.setForeground(Color.WHITE); // Couleur du texte
	restartButton.setBackground(new Color(0, 153, 204)); // Bleu clair
	restartButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
	restartButton.setFocusPainted(false); // Retirer le focus

	// Effet de survol pour le bouton "Recommencer la Partie"
	restartButton.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseEntered(MouseEvent e) {
	        restartButton.setBackground(new Color(0, 102, 153)); // Bleu plus foncé
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	        restartButton.setBackground(new Color(0, 153, 204)); // Retour au bleu clair
	    }
	});

	restartButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        //restartGame();
	    }
	});
	buttonPanel.add(restartButton);

	// Ajouter le sous-panneau des boutons au menuPanel
	buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT); // Centrer horizontalement le sous-panneau
	menuPanel.add(buttonPanel);

	
	
	 
	// Créer un JOptionPane personnalisé
	JOptionPane optionPane = new JOptionPane(menuPanel, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

	// Créer un JDialog à partir du JOptionPane
	JDialog dialog = optionPane.createDialog("Menu de la Partie");
	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // Fermer correctement
	dialog.setModal(true);
	
	ImageIcon icon = new ImageIcon("src/image/Turing-logo.png"); // Charger l'image
	dialog.setIconImage(icon.getImage()); // Définir l'icône pour le JDialog

	// Ajouter des actions de fermeture aux boutons
	quitButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	
	        dialog.dispose(); // Fermer la fenêtre
	        Menu frame = new Menu(false);
    	    frame.setVisible(true);
    	    dispose();
	    }
	});

	restartButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        dialog.dispose(); // Fermer la fenêtre
	        
	     // Autres réinitialisations nécessaires pour l'interface
	        resetLives();
	        resetAllCards();
	        resetTextFields();

	        
	        controller.setListeActJoueur(new LinkedList<>(controller.getListeDefJoueur()));
	        
	        
	        // Réinitialiser les variables de suivi de la partie
	        manche = 1;
	        currentPlayerIndex = 0;
	        essaisParJoueur = new int[controller.getListeActJoueur().size()];
	        cartesTestees.clear();
	        for (Joueur joueur : controller.getListeActJoueur()) {
	            joueur.resetStats(); // Créez une méthode resetStats() dans la classe Joueur pour réinitialiser les stats du joueur
	        }
	        
	        // Réinitialiser les résultats des tests
	        for (int i = 0; i < testResults.length; i++) {
	            testResults[i] = '2';
	        }

	        // Réinitialiser l'affichage
	        mancheLabel.setText("Manche : " + manche);
	        int indexPlayer = currentPlayerIndex + 1;
	        playerTurnLabel.setText("Tour du joueur " + indexPlayer + " : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo());
	        
	    }
	});

	// Afficher la fenêtre
	dialog.setVisible(true);
	
}




    }

