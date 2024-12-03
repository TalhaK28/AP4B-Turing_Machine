package game;

import java.io.*;
import java.util.LinkedList;

public class Deck {

	private int solution;
	private LinkedList<Carte> Liste;
	
	
	
	 // Constructeur qui prend en charge le fichier "infoDeck.txt"
    Deck(int nbDeck) {
    	
    	switch(nbDeck) {
    	case 1:
    		this.solution = 241;
    		break;
    	case 2 : 
    		this.solution = 224;
    		break;
    	case 3 : this.solution = 243;
    		break;
    	}
    	
    	
        this.Liste = new LinkedList<Carte>();
        chargerDeckDepuisFichier(nbDeck);
    }

    private void chargerDeckDepuisFichier(int nbDeck) {
    	String fileName = "src/info/infoDeck" + nbDeck + ".txt";
    
    	try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
    		String line;

    		while ((line = reader.readLine()) != null) {
    			if (line.trim().isEmpty()) continue; // Ignorer les lignes vides

    			// Lecture de la conditionAction
    			String conditionAction = line.trim();

    			// Lecture de la description complète
    			StringBuilder descriptionBuilder = new StringBuilder();

    			boolean firstPart = true; // Pour ne pas ajouter un saut de ligne au début de la description

    			while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
    				line = line.trim(); // Enlever les espaces avant et après chaque ligne

    				if (line.equals("---")) {
    					// Ajouter un seul saut de ligne entre les sections, mais pas au début
    					if (!firstPart) {
    						descriptionBuilder.append("\n");
    					}
    				} else {
    					// Si ce n'est pas un délimiteur, ajouter la ligne à la description
    					descriptionBuilder.append(line).append(" "); // Ajouter la ligne avec un espace à la fin
    					firstPart = false; // Après le premier ajout, autoriser l'ajout de sauts de ligne
    				}
    			}

    			// Supprimer l'espace en trop à la fin (après la dernière ligne)
    			String description = descriptionBuilder.toString().trim();

    			// Créer une nouvelle carte et l'ajouter au deck
    			Carte carte = new Carte(description, conditionAction);
    			Liste.add(carte);
    		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }

    public Carte getCarteIndex(int index) {
    	return this.Liste.get(index);
    }


    public int getSolution() {
    	return this.solution;
    }
    
    public int getSize() {
    	return Liste.size();
    }
    
    public boolean testCarte(int index, int triangle, int carree, int rond) {
    	return Liste.get(index).verificateur(triangle, carree, rond);
    }
    
    
}
