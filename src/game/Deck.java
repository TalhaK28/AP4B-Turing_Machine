package game;

import java.io.*;
import java.util.*;
import java.util.LinkedList;

public class Deck {

	LinkedList<Carte> Liste;
	int longueur;
	
	
	 // Constructeur qui prend en charge le fichier "nbDeck.txt"
    Deck(int nbDeck) {
        this.Liste = new LinkedList<Carte>();
        chargerDeckDepuisFichier(nbDeck);
    }

    // Méthode pour charger le deck depuis le fichier
    private void chargerDeckDepuisFichier(int nbDeck) {
    	
    	String fileName = "../" + nbDeck + ".txt";
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int idCarte = 1;  // ID initial pour les cartes
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;  // Ignorer les lignes vides

                // Lecture de la ligne pour conditionAction
                String conditionAction = line.trim();

                // Lire la ligne suivante pour les conditionChamps
                line = reader.readLine();
                if (line != null && !line.trim().isEmpty()) {
                    String[] champs = line.trim().split(",");
                    int[] conditionChamps = new int[champs.length];
                    
                    // Convertir chaque champ en entier
                    for (int i = 0; i < champs.length; i++) {
                        conditionChamps[i] = Integer.parseInt(champs[i].trim());
                    }

                    // Créer une nouvelle carte et l'ajouter au deck
                    Carte carte = new Carte("Carte " + idCarte, idCarte, conditionAction, conditionChamps);
                    Liste.add(carte);
                    idCarte++;  // Incrémenter l'ID pour chaque carte
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
