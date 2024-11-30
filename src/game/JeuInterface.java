package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Optional;

public class JeuInterface extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Jeu controller;
    private int manche = 1;
    private int currentPlayerIndex = 0;

    private JLabel mancheLabel;
    private JLabel playerTurnLabel;

    /**
     * Create the frame.
     */
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

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ajouter une image de fond
        ImageIcon backgroundImg = new ImageIcon("src/image/fond_blanc.png");
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(new BorderLayout());
        setContentPane(contentPane);

        // ** Panneau supérieur : Affichage du numéro de manche et joueur actif **
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setOpaque(false);

        mancheLabel = new JLabel("Manche : " + manche, SwingConstants.CENTER);
        mancheLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(mancheLabel, BorderLayout.NORTH);

        playerTurnLabel = new JLabel("Tour du joueur : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo(), SwingConstants.CENTER);
        playerTurnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        topPanel.add(playerTurnLabel, BorderLayout.SOUTH);

        contentPane.add(topPanel, BorderLayout.NORTH);

        // ** Panneau central : Figures avec zones de texte cliquables **
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 3, 20, 20));
        centerPanel.setOpaque(false);

        JPanel trianglePanel = createFigurePanel("src/image/triangle.png", "Triangle");
        JPanel carreePanel = createFigurePanel("src/image/carree.png", "Carré");
        JPanel rondPanel = createFigurePanel("src/image/rond.png", "Rond");

        centerPanel.add(trianglePanel);
        centerPanel.add(carreePanel);
        centerPanel.add(rondPanel);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        // ** Panneau inférieur : Bouton Valider **
        JButton validateButton = new JButton("Valider");
        validateButton.setBackground(new Color(0, 102, 153));
        validateButton.setForeground(Color.WHITE);
        validateButton.setFocusPainted(false);
        validateButton.setPreferredSize(new Dimension(100, 40)); // Taille réduite
        validateButton.addActionListener(e -> validateTurn(trianglePanel, carreePanel, rondPanel));
        contentPane.add(validateButton, BorderLayout.SOUTH);

        // ** Panneau supérieur : Cartes **
        JPanel cardsPanel = new JPanel();
        cardsPanel.setLayout(new GridLayout(1, controller.listeRegle.getSize(), 10, 10));
        cardsPanel.setOpaque(false);

        for (int i = 0; i < controller.listeRegle.getSize(); i++) {
            Carte carte = controller.listeRegle.Liste.get(i);

            // Remplacer les mots clés par des images
            String descriptifHtml = carte.descriptif
                    .replace("triangle", "<img src='file:src/image/triangle.png' width='20' height='20'>")
                    .replace("carree", "<img src='file:src/image/carree.png' width='20' height='20'>")
                    .replace("rond", "<img src='file:src/image/rond.png' width='20' height='20'>")
                    .replace("\n", "<br>"); // Gérer les sauts de ligne

            JPanel cardPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(220, 220, 220, 200)); // Fond semi-transparent
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Fond arrondi
                }
            };
            cardPanel.setLayout(new BorderLayout());
            cardPanel.setOpaque(false);
            cardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel cardLabel = new JLabel("<html><div style='text-align: center;'>" + descriptifHtml + "</div></html>");
            cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
            cardPanel.add(cardLabel, BorderLayout.CENTER);

            JButton testButton = new JButton("Tester");
            testButton.setBackground(new Color(0, 153, 204));
            testButton.setForeground(Color.WHITE);
            testButton.setFocusPainted(false);
            testButton.addActionListener(new TestButtonActionListener(i));
            cardPanel.add(testButton, BorderLayout.SOUTH);

            cardsPanel.add(cardPanel);
        }

        contentPane.add(cardsPanel, BorderLayout.NORTH);
    }

    private void validateTurn(JPanel trianglePanel, JPanel carreePanel, JPanel rondPanel) {
        try {
            int triangle = Integer.parseInt(((JTextField) trianglePanel.getComponent(1)).getText());
            int carree = Integer.parseInt(((JTextField) carreePanel.getComponent(1)).getText());
            int rond = Integer.parseInt(((JTextField) rondPanel.getComponent(1)).getText());

            controller.setVal(triangle, carree, rond);

            if (++currentPlayerIndex >= controller.getListeActJoueur().size()) {
                currentPlayerIndex = 0;
                finDeManche();
            } else {
                playerTurnLabel.setText("Tour du joueur : " + controller.getListeActJoueur().get(currentPlayerIndex).getPseudo());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finDeManche() {
        LinkedList<Joueur> joueursAyantPropose = new LinkedList<>();
        for (Joueur joueur : controller.getListeActJoueur()) {
            int option = JOptionPane.showConfirmDialog(this,
                    "Joueur " + joueur.getPseudo() + ", souhaitez-vous tenter une proposition ?",
                    "Proposition",
                    JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                int proposition = Integer.parseInt(JOptionPane.showInputDialog(this, "Entrez votre proposition :"));
                joueur.setProposition(proposition);

                if (controller.testProp()) {
                    joueursAyantPropose.add(joueur);
                }
            }
        }

        if (joueursAyantPropose.isEmpty()) {
            manche++;
            mancheLabel.setText("Manche : " + manche);
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

    private JPanel createFigurePanel(String imagePath, String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        JLabel imageLabel = new JLabel(new ImageIcon(imagePath));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel, BorderLayout.CENTER);

        JTextField textField = new JTextField();
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setToolTipText("Entrez la valeur pour " + labelText);
        panel.add(textField, BorderLayout.SOUTH);

        return panel;
    }
    
    private class TestButtonActionListener implements ActionListener {
        private final int index;

        public TestButtonActionListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean result = controller.testCarte(index);
            String message = result ? "Test réussi !" : "Test échoué.";
            JOptionPane.showMessageDialog(JeuInterface.this, message, "Résultat", JOptionPane.INFORMATION_MESSAGE);
        }
    }

}

