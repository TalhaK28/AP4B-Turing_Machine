package game;

import java.io.*;
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
    	
    	String fileName = "../infoDeck" + nbDeck + ".txt";
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        	String line;
            int idCarte = 1; // ID initial pour les cartes

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Ignorer les lignes vides

                // Lecture de la conditionAction
                String conditionAction = line.trim();

                // Lecture de la description complète
                StringBuilder descriptionBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                    if (line.trim().equals("---")) {
                        descriptionBuilder.append("\n"); // Ajouter une nouvelle ligne pour les parties
                    } else {
                        descriptionBuilder.append(line.trim()).append(" ");
                    }
                }

                // Supprimer l'espace en trop à la fin
                String description = descriptionBuilder.toString().trim();

                // Créer une nouvelle carte et l'ajouter au deck
                Carte carte = new Carte(description, idCarte, conditionAction);
                Liste.add(carte);
                idCarte++; // Incrémenter l'ID pour chaque carte
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
