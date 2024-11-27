package game;

public class Carte {
	
	String descriptif;
	int id;
	String conditionAction;
	int[] conditionChamps;
	
	Carte(){
		this.descriptif = "";
		this.id = 0;
		this.conditionAction = "";
		this.conditionChamps = null;
	}
	
	Carte(String d, int id, String conditionAction, int[] conditionChamps){
		this.descriptif = d;
		this.id = id;
		this.conditionAction = conditionAction;
		this.conditionChamps = conditionChamps;
	}
	
		public boolean verificateur(int triangle, int carree, int rectangle) {
			
			int[] proposition = {triangle, carree, rectangle};
		
		switch(this.conditionAction) {
		case ">=1" : 
			return proposition[this.conditionChamps[0]]>=1;
			break;
		case 
		}
		
	}
	
	
	
	
	  // Getter pour descriptif
    public String getDescriptif() {
        return descriptif;
    }

    // Setter pour descriptif
    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    // Getter pour id
    public int getId() {
        return id;
    }

    // Setter pour id
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour condition
    public String getConditionAction() {
        return conditionAction;
    }

    // Setter pour condition
    public void setConditionAction(String condition) {
        this.conditionAction = condition;
    }
	
    // Getter pour condition
    public int[] getConditionChamps() {
        return conditionChamps;
    }

    // Setter pour condition
    public void setConditionChamps(int[] condition) {
        this.conditionChamps = condition;
    }
	
	
	
	
}
