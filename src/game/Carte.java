package game;

public class Carte {
	
	private String descriptif;
	private String condition;
	
	Carte(){
		this.descriptif = "";
		this.condition = "";
	}
	
	Carte(String d, String condition){
		this.descriptif = d;
		this.condition = condition;
	}
	
		public boolean verificateur(int triangle, int carree, int rond) {
		
			
			if (this.condition == null) {
			    return false;
			}
	
			
		switch(this.condition) {
			//4 : Carré supérieure, égale ou inférieure à 3
			case ">3" : 
				return carree>3;
				
			//9 : Le vérificateur vérifie qu’il y seulement une fois la valeur « 1 » dans la proposition	
			case "1" :
				return (triangle==1 ^ carree==1 ^ rond==1 );
				
			//11 : Triangle supérieure, égale ou inférieure à Carré. | C > T	
			case "&lt;" :	
				return (triangle<carree);
				
			//14 : Le vérificateur vérifie si Rond est strictement plus petit que Carré et Triangle	
			case "&lt;&lt;"  :
				return (rond<carree && rond<triangle);
				
			//19 : Triangle + Carré supérieure, égale ou inférieure à 6 | T+C < 6	
			case "{T+C}&lt;6" :
				return ((triangle + carree) < 6);
				
			//24 : Le vérificateur vérifie s’il y a une séquence croissante consécutive ou NON |
			//C != T+1 || R != C+1
			case "->" :
				return (carree!=triangle + 1 || rond!=carree+1);
				
				//30 : Rond supérieure, égale ou inférieure à 4 | R = 4
			case "==4" :
				return (rond == 4);
				
			//31 : Carre supérieure, égale ou inférieure à 1 | C > 1
			case ">1" : 
				return (carree >1);
				
			//38 : Triangle + Rond supérieure, égale ou inférieure à 6 | T+R = 6
			case "{T+R}=6" :
				return ((triangle + rond) == 6);
				
			//2 : Triangle supérieure, égale ou inférieure à 3. | T < 3 
			case "T&lt;3" :
				return triangle<3;
				
			//7 : Le vérificateur vérifie si Rond est impaire (1,3,5). | R %2 == 1
			case "R%2" :
				return rond %2 ==1;
				
			//12 : Rond supérieure, égale ou inférieure à Triangle. | R > T
			case "R>T" :
				return rond > triangle;
				
			//16 : Le vérificateur vérifie qu’il y a soit plus de valeurs paires soit impaires| paires > impaire
			case "Pair>Impair" :
				int pairs = 0, impairs = 0;
				if (triangle % 2 == 0) pairs++; else impairs++;
				if (carree % 2 == 0) pairs++; else impairs++;
				if (rond % 2 == 0) pairs++; else impairs++;
				return pairs > impairs;		
				
			//19 : Triangle + Carré supérieure, égale ou inférieure à 6 | T+C = 6
			case "T+C=6" :
				return ((triangle + carree) == 6);
				
			//22 : Le vérificateur vérifie que les trois valeurs sont en ordre soit croissant,
				//soit décroissant, soit aucun des deux.| C >= T || C <= R
			case "OrdreC" :
				return carree >= triangle || carree <= rond;
				
			default :
				return false;
		}
	}
		
		public String getCondition() {
			return this.condition;
		}
		
		public String getDescriptif() {
			return this.descriptif;
		}
	
}
