package game;

import java.util.LinkedHashMap;
import java.util.Map;

public class Joueur {

	private String pseudo;
	private int nbQuestion;
	private Map<Integer, String> note; //les notes (informations) gagnés aux cours des manches précédentes

	
	
    // Constructeur
    public Joueur(String pseudo) {
        this.pseudo = pseudo;
        this.nbQuestion = 0;
        this.note = new LinkedHashMap<>();
    }
    
    // Constructeur par copie
    public Joueur(Joueur j) {
        this.pseudo = j.pseudo; // Copie du pseudo
        this.nbQuestion = j.nbQuestion; // Copie du nombre de questions

        // Copie profonde de la Map note
        this.note = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> entry : j.note.entrySet()) {
            this.note.put(entry.getKey(), entry.getValue());
        }
    }

    // Getter et Setter pour pseudo
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    // Getter et Setter pour nbQuestion
    public int getNbQuestion() {
        return nbQuestion;
    }

    public void setNbQuestion(int nbQuestion) {
        this.nbQuestion = nbQuestion;
    }

    // Getter et Setter pour note
    public Map<Integer, String> getNote() {
        return this.note;
    }

    public void setNote(Map<Integer, String> note) {
        this.note = note;
    }
    
    public void addNote(int proposition, String tests) {
        // Générer une clé unique en concaténant proposition avec la taille actuelle de la Map
        int uniqueKey = Integer.parseInt(proposition + String.valueOf(this.note.size()));
        this.note.put(uniqueKey, tests);
    }
    
    public String generateHtmlNoteTable() {

    StringBuilder htmlTable = new StringBuilder();

    // Début du tableau HTML
    htmlTable.append("<table border='4' style='border-collapse:collapse; text-align:center; width: 80%; margin: 20px auto; border-radius: 10px; box-shadow: 0px 4px 6px rgba(0,0,0,0.1);'>");

    // Ajouter les titres des colonnes
    htmlTable.append("<thead style='background-color: #f2f2f2;'>");
    htmlTable.append("<tr style='height: 50px;'>");
    htmlTable.append("<th style='padding: 10px;'>triangle</th>");
    htmlTable.append("<th style='padding: 10px;'>carree</th>");
    htmlTable.append("<th style='padding: 10px;'>rond</th>");

    // Calcul du nombre maximum de colonnes
    int maxColumns = this.note.values().stream().mapToInt(String::length).max().orElse(0);
    for (int n = 1; n <= maxColumns; n++) {
        htmlTable.append("<th style='padding: 10px;'>").append(n).append("</th>");
    }
    htmlTable.append("</tr></thead>");

    // Ajouter les données des lignes
    htmlTable.append("<tbody>");
    for (Map.Entry<Integer, String> entry : this.note.entrySet()) {
        String key = String.valueOf(entry.getKey()); // Convertir la clé en chaîne
        String value = entry.getValue(); // Récupérer la chaîne associée

        // Extraire les valeurs triangle, carré et rond
        String triangle = key.length() > 0 ? String.valueOf(key.charAt(0)) : "";
        String carree = key.length() > 1 ? String.valueOf(key.charAt(1)) : "";
        String rond = key.length() > 2 ? String.valueOf(key.charAt(2)) : "";

        // Début de la ligne
        htmlTable.append("<tr style='height: 60px;'>");
        htmlTable.append("<td style='padding: 10px;'>").append(triangle).append("</td>");
        htmlTable.append("<td style='padding: 10px;'>").append(carree).append("</td>");
        htmlTable.append("<td style='padding: 10px;'>").append(rond).append("</td>");

        // Ajouter les colonnes en fonction de la valeur
        for (int i = 0; i < maxColumns; i++) {
            if (i < value.length()) {
                char c = value.charAt(i);
                switch (c) {
                    case '1' -> htmlTable.append("<td style='padding: 10px;'><img src='file:src/image/valid.png' width='60' height='60'></td>");
                    case '0' -> htmlTable.append("<td style='padding: 10px;'><img src='file:src/image/invalid.png' width='60' height='60'></td>");
                    default -> htmlTable.append("<td style='padding: 10px;'></td>"); // Cellule vide pour les autres caractères
                }
            } else {
                htmlTable.append("<td style='padding: 10px;'></td>"); // Cellule vide si aucune valeur
            }
        }

        // Fin de la ligne
        htmlTable.append("</tr>");
    }
    htmlTable.append("</tbody>");
    htmlTable.append("</table>");

    return htmlTable.toString();
}
    
    public void resetStats() {
        this.nbQuestion = 0;
        this.note.clear(); // Si vous avez une liste de notes ou de scores
    }


	
	
}
