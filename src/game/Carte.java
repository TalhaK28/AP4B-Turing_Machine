package game;

import java.io.*;
import java.util.*;
import javax.script.*;

public class Carte {
	
	String descriptif;
	String condition;
	private Map<String, String> conditionsMap = new HashMap<>();

    public Carte(String description, String condition) {
    	this.descriptif = description;
        this.condition = condition;      
        loadConditions("src/info/conditions");
    }

    private void loadConditions(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    conditionsMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean verificateur(int triangle, int carree, int rond) {
        if (this.condition == null || !conditionsMap.containsKey(this.condition)) {
            return false;
        }

        String expression = conditionsMap.get(this.condition);

        // Préparer les variables pour l'évaluation
        Map<String, Object> variables = new HashMap<>();
        variables.put("triangle", triangle);
        variables.put("carree", carree);
        variables.put("rond", rond);
        variables.put("pairs", (triangle % 2 == 0 ? 1 : 0) + (carree % 2 == 0 ? 1 : 0) + (rond % 2 == 0 ? 1 : 0));
        variables.put("impairs", 3 - (int) variables.get("pairs"));

        return evaluateExpression(expression, variables);
    }

    private boolean evaluateExpression(String expression, Map<String, Object> variables) {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

            // Injecter les variables dans le moteur d'évaluation
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                engine.put(entry.getKey(), entry.getValue());
            }

            // Évaluer l'expression
            Object result = engine.eval(expression);
            if (result instanceof Boolean) {
                return (Boolean) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
