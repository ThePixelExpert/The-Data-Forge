import java.util.*;
import java.io.IOException;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.FileReader;

public class Utils {
   public static void generateCombinations(int n, int x, List<Object[]> combinations, DataStructure.InputType outputMode) {
     
        Object[] result = new Object[n];
        generateCombinationsRecursive(result, 0, x, combinations, outputMode);
        
    }

    public static void generateCombinationsRecursive(Object[] result, int level, int max, List<Object[]> combinations, DataStructure.InputType outputMode) {
        switch(outputMode){
            case INT:
                
                if (level == result.length) {
                    // Create a copy of the result and add it to the combinations list
                    Object[] copy = Arrays.copyOf(result, result.length);
                    combinations.add(copy);
                }else {
                    for (int i = 0; i <= max; i++) {
                        result[level] = i;
                        generateCombinationsRecursive(result, level + 1, max, combinations, DataStructure.InputType.INT);
                    }
                }
                break;
            case DOUBLE:
                 if (level == result.length) {
                    // Create a copy of the result and add it to the combinations list
                    Object[] copy = Arrays.copyOf(result, result.length);
                    combinations.add(copy);
                }else {
                    for (int i = 0; i <= max; i++) {
                        result[level] = ((double) i /10);
                        generateCombinationsRecursive(result, level + 1, max, combinations, DataStructure.InputType.DOUBLE);
                    }
                }
                break;
            case BOOLEAN:
                
                if (level == result.length) {
                    // Create a copy of the result and add it to the combinations list
                    Object[] copy = Arrays.copyOf(result, result.length);
                    combinations.add(copy);
                }else {
                    for (int i = 0; i <= 1; i++) {
                        result[level] = i == 1;
                        generateCombinationsRecursive(result, level + 1, 1, combinations, DataStructure.InputType.BOOLEAN);
                    }
                }
            default:
                break;
                
        }
        
    }
    
    public static String buildInputString(String[] inputStrings, Object[] combination) {
        StringBuilder input = new StringBuilder();
        input.append("{");
        for (int i = 0; i < inputStrings.length; i++) {
            input.append(inputStrings[i]).append(": ").append(combination[i]);
            if (i < inputStrings.length - 1) {
                input.append(", ");
            }else{
                input.append("}");
            }
        }
        return input.toString();
    }
    public static String replaceVariables(String formula, String[] inputStrings, Object[] combination) {
        for (int i = 0; i < inputStrings.length; i++) {
            formula = formula.replaceAll(inputStrings[i], String.valueOf(combination[i]));
        }
        return formula;
    }
    
    public static String buildCustomOutputString(DataStructure structure, Object[] combination, ScriptEngine engine) {
        String[] formulas = structure.getOutputFormula();
        String[] inputStrings = structure.getInputStrings();
        
        StringBuilder output = new StringBuilder();
        output.append("{");
        for (int i = 0; i < formulas.length; i++) {
            String formula = replaceVariables(formulas[i], inputStrings, combination);
            
            try {
                Object evalResult = engine.eval(formula);
    
                // Ensure evalResult is a Double before applying formatting
                if (evalResult instanceof Double) {
                    output.append(structure.getOutputStrings()[i]).append(": ").append(String.format("%.2f", (Double) evalResult));
                } else {
                    output.append(structure.getOutputStrings()[i]).append(": ").append(evalResult);
                }
    
                if (i < formulas.length - 1) {
                    output.append(", ");
                } else {
                    output.append("}");
                }
            } catch (ScriptException e) {
                System.out.println("Exception during evaluation: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error evaluating the expression.", e);
            }
        }
        return output.toString(); // Return the result after building for each combination
    }
 
    // parsing output
    public static String[] splitEquation(String equation) {
        // Split the equation using regular expression
        String[] parts = equation.split("\\s*[-+*]\\s*");

        // Remove empty strings from the resulting array
        parts = Arrays.stream(parts)
                      .filter(s -> !s.isEmpty())
                      .toArray(String[]::new);

        return parts;
    }
    
 
}
