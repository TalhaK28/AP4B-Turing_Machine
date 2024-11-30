package game;

public class Joueur {

	String pseudo;
	boolean tryFinalProp;//essayer de donner une valeur
	int triangle; //première valeur entrée par le joueur
	int carree; //deuxième valeur entrée par le joueur
	int rond;//troisième valeur entrée par le joueur
	int nbQuestion;
	String note; //les notes (informations) gagnés aux cours des manches précédentes
	int proposition;
	
	
	
	
    // Constructeur
    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.tryFinalProp = false;
        this.triangle = 0;
        this.carree = 0;
        this.rond = 0;
        this.nbQuestion = 0;
        this.note = "";
    }

    // Getter et Setter pour pseudo
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // Getter et Setter pour tryFinalProp
    public boolean isTryFinalProp() {
        return tryFinalProp;
    }

    public void setTryFinalProp(boolean tryFinalProp) {
        this.tryFinalProp = tryFinalProp;
    }

    // Getter et Setter pour triangle
    public int getTriangle() {
        return triangle;
    }

    public void setTriangle(int triangle) {
        this.triangle = triangle;
    }

    // Getter et Setter pour carree
    public int getCarree() {
        return carree;
    }

    public void setCarree(int carree) {
        this.carree = carree;
    }

    // Getter et Setter pour rond
    public int getRond() {
        return rond;
    }

    public void setRond(int rond) {
        this.rond = rond;
    }

    // Getter et Setter pour nbQuestion
    public int getNbQuestion() {
        return nbQuestion;
    }

    public void setNbQuestion(int nbQuestion) {
        this.nbQuestion = nbQuestion;
    }

    // Getter et Setter pour note
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
	
    public void setProposition(int p) {
    	this.proposition=p;
    }
	
}
