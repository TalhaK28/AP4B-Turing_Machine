package game;

import java.util.LinkedList;

public class Jeu {
	
	private Deck listeRegle;

	//fields pour stocker temporairement la proposition du joueur
	private int triangle;
	private int carree;
	private int rond;
	
	//nb de vérificateur vérifié durant une manche 
	private int nbDemande;
	
	//montre si au moins un joueur veut tester sa proposition
	private boolean testProp;
	
	//contient l'essaie du joueur
	private int propositionFinal;
	
	
	private LinkedList<Joueur> listeDefJoueur; //liste non modifié du début à la fin
	private LinkedList<Joueur> listeActJoueur; //les joueurs ayant tenté leur chance sont enlevé de cette liste
	
	//int diffulte;
	
	private int tour;
	
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
		this.propositionFinal = 0;
		
	}
	
	public boolean testCarte(int index) {
		
		this.nbDemande++;
		return this.listeRegle.testCarte(index, this.triangle, this.carree, this.rond);
		
	}
	
	
	public boolean testProp(){
		return this.propositionFinal==this.listeRegle.getSolution();
	}
	
	// Getters and Setters

    public LinkedList<Joueur> getListeDefJoueur() {
        return listeDefJoueur;
    }

    public void setListeDefJoueur(LinkedList<Joueur> listeDefJoueur) {
        this.listeDefJoueur = listeDefJoueur;
    }

    public LinkedList<Joueur> getListeActJoueur() {
        return listeActJoueur;
    }

    public void setListeActJoueur(LinkedList<Joueur> listeActJoueur) {
        this.listeActJoueur = listeActJoueur;
    }
    
    public void removeListeActJoueurIndex(int index) {
    	this.listeActJoueur.remove(index);
    }

    public int getTriangle() {
        return triangle;
    }

    public int getCarree() {
        return carree;
    }

    public int getRond() {
        return rond;
    }


    public void setVal(int triangle, int carree, int rond) {
        this.triangle = triangle;
        this.carree = carree;
        this.rond = rond;
    }
    
    public int getVal() {
        String concatenated = String.valueOf(triangle) + String.valueOf(carree) + String.valueOf(rond);
        return Integer.parseInt(concatenated);
    }

    public int getNbDemande() {
        return nbDemande;
    }

    public void setNbDemande(int nbDemande) {
        this.nbDemande = nbDemande;
    }

    public boolean isTestProp() {
        return testProp;
    }

    public void setTestProp(boolean testProp) {
        this.testProp = testProp;
    }

    public int getPropositionFinal() {
        return propositionFinal;
    }

    public void setPropositionFinal(int propositionFinal) {
        this.propositionFinal = propositionFinal;
    }

    public int getTour() {
        return tour;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }
    
    public void enleverJoueur(int index) {
    	listeActJoueur.remove(index);
    }
	
    public Deck getListeRegle() {
    	return listeRegle;
    }

	public int getNombreDeCartes() {
		
		return listeRegle.getSize();
	}
	
	
}
