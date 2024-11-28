package game;

import java.util.LinkedList;

public class Jeu {
	
	Deck listeRegle;

	//fields pour stocker temporairement la proposition du joueur
	int triangle;
	int carree;
	int rond;
	
	//nb de vérificateur vérifié durant une manche 
	int nbDemande;
	
	//montre si au moins un joueur veut tester sa proposition
	boolean testProp;
	
	//contient l'essaie du joueur
	int proprositionFinal;
	
	
	LinkedList<Joueur> listeDefJoueur; //liste non modifié du début à la fin
	LinkedList<Joueur> listeActJoueur; //les joueurs ayant tenté leur chance sont enlevé de cette liste
	
	//int diffulte;
	
	int tour;
	
	//int modeJeu;
	
	Jeu(LinkedList<Joueur> Liste, int diff){
	
		this.listeRegle = new Deck(diff);
		
		this.triangle=0;
		this.carree=0;
		this.rond=0;
		
		this.nbDemande=0;
		
		this.listeActJoueur=Liste;
		this.listeDefJoueur=Liste;
		
		this.testProp = false;
		this.proprositionFinal = 0;
		
	}
	
	public boolean testCarte(int index) {
		
		this.nbDemande++;
		return this.listeRegle.testCarte(index, this.triangle, this.carree, this.rond);
		
	}
	
	
	public boolean testProp() {
		this.testProp = true;
		return this.proprositionFinal==this.listeRegle.getSolution();
	}
	
	
	
	
	
	
}
