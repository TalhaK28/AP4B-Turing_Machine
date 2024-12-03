package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        

        public JeuInterface(LinkedList<Joueur> Liste, int diff) {
        

            // Configurer l'icône de la fenêtre
            setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/image/Turing-logo.PNG")));

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

            // ** Panneau supérieur : Manche et joueur actif **
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(2, 1));
            topPanel.setOpaque(false);

            mancheLabel = new JLabel("Manche : " + manche, SwingConstants.CENTER);
            mancheLabel.setFont(new Font("Hexaplex", Font.BOLD, 32)); // Utilisation de la police Hexaplex;
            topPanel.add(mancheLabel);
            
            int indexPlayer = currentPlayerIndex+ 1;

            playerTurnLabel = new JLabel("Tour du joueur " + indexPlayer + " : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo(), SwingConstants.CENTER);
            playerTurnLabel.setFont(new Font("Hexaplex", Font.BOLD, 24)); // Utilisation de la police Hexaplex;
            topPanel.add(playerTurnLabel);

            contentPane.add(topPanel, BorderLayout.NORTH);

         // ** Panneau central : Cartes avec boutons "Tester" **
            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new GridLayout(0, controller.listeRegle.getSize(), 20, 20)); // Espacement plus large entre les cartes
            cardsPanel.setOpaque(false);

            for (int i = 0; i < controller.listeRegle.getSize(); i++) {
                Carte carte = controller.listeRegle.Liste.get(i);

                String descriptifHtml = carte.descriptif
                        .replace("triangle", "<img src='file:src/image/triangle.png' width='40' height='40'>") // Images plus grandes
                        .replace("carree", "<img src='file:src/image/carree.png' width='40' height='40'>")
                        .replace("rond", "<img src='file:src/image/rond.png' width='40' height='40'>")
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
                cardLabel.setFont(new Font("Hexaplex", Font.BOLD, 14)); // Utilisation de la police Hexaplex;
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

            // ** Bouton Valider **
            JButton validateButton = new JButton("Valider");
            validateButton.setPreferredSize(new Dimension(150, 50)); // Bouton plus grand
            validateButton.setBackground(new Color(0, 102, 204)); // Bleu foncé pour attirer l'attention
            validateButton.setForeground(Color.WHITE); // Texte blanc
            validateButton.setFont(new Font("Hexaplex", Font.BOLD, 24)); // Utilisation de la police Hexaplex;
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
            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false);
            buttonPanel.add(validateButton);
            
            
            bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
        }


        private JPanel createFigurePanel(String imagePath, String labelText, String index) {
        	JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setOpaque(false);

            // Créer l'image avec JLabel
            JLabel imageLabel = new JLabel(new ImageIcon(imagePath));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imageLabel, BorderLayout.CENTER);

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
        	
        	resetAllCards();
        	resetTextFields();
            

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
        int i = 0;
        LinkedList<Joueur> joueursAyantPropose = new LinkedList<>();
        Iterator<Joueur> iterator = controller.getListeActJoueur().iterator();
        
        while (iterator.hasNext()) {
            Joueur joueur = iterator.next();
            int option = JOptionPane.showConfirmDialog(this,
                    "Joueur " + joueur.getPseudo() + ", souhaitez-vous tenter une proposition ?",
                    "Proposition",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                int proposition = Integer.parseInt(JOptionPane.showInputDialog(this, "Entrez votre proposition :"));
                controller.setPropositionFinal(proposition);

                if (controller.testProp()) {
                    joueursAyantPropose.add(joueur);
                    i++;
                } else {
                    iterator.remove();  // Supprime l'élément de la liste pendant l'itération
                    i--;
                	
                }
            }
        }
        
    

        if (joueursAyantPropose.isEmpty()) {
            manche++;
            essaisParJoueur = new int[controller.getListeActJoueur().size()];
            cartesTestees.clear();

            // Réinitialiser l'affichage et commencer une nouvelle manche
            
            mancheLabel.setText("Manche : " + manche);
            currentPlayerIndex = 0;
            int indexPlayer = currentPlayerIndex + 1;
            playerTurnLabel.setText("Tour du joueur "+ indexPlayer +  " :" + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo());

        } else {
            Optional<Joueur> gagnant = joueursAyantPropose.stream()
                    .min((j1, j2) -> Integer.compare(j1.getNbQuestion(), j2.getNbQuestion()));

            if (gagnant.isPresent()) {
                JOptionPane.showMessageDialog(this,
                        "Félicitations " + gagnant.get().getPseudo() + ", vous avez gagné avec " + gagnant.get().getNbQuestion() + " demandes !",
                        "Fin de la partie",
                        JOptionPane.INFORMATION_MESSAGE);
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

        // Tester la carte et obtenir le résultat
        boolean result = controller.testCarte(index);
        String message = result ? "Test réussi !" : "Test échoué.";
        JOptionPane.showMessageDialog(JeuInterface.this, message, "Résultat", JOptionPane.INFORMATION_MESSAGE);

        // Mettre à jour le descriptif HTML de la carte avec l'icône correspondante
        JPanel cardPanel = (JPanel) ((JButton) e.getSource()).getParent();
        JLabel cardLabel = (JLabel) cardPanel.getComponent(0); // Supposant que le JLabel descriptif est le premier composant du panneau

        String iconHtml = result
                ? "<div style='text-align: center;'><img src='file:src/image/valid.png' width='100' height='100'></div>"
                : "<div style='text-align: center;'><img src='file:src/image/invalid.png' width='100' height='100'></div>";

        String currentHtml = cardLabel.getText();
        currentHtml = currentHtml.replace("</html>", "<br>" + iconHtml + "</html>");

        cardLabel.setText(currentHtml);

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
	            else if (text.matches("\\d") && fb.getDocument().getLength() < 1) {
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




    }

