import java.util.*;
import java.text.DecimalFormat;
import javax.script.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class FileParsing
{
    static final DecimalFormat df = new DecimalFormat("0.00");
    public enum OutputType{
        ROBOTICS, OTHER;
    }
    // impliment custom functions for output data
    public static void main(String[] args)
    {
        // ax,lat,yaw : leftFront,rightFront,leftBack,rightBack;
       
        String[] roboticsInput = {"axial","lateral","yaw"};
        String[] roboticsOutput = {"Left Front Power", "Right Front Power","Left Back Power","Right Back Power"}; 
        String[] roboticsFormula = {"axial +  lateral +  yaw","axial - lateral - yaw",
                                    " axial -  lateral + yaw", "axial +  lateral - yaw"};
                                    
        String[] customTestInput = {"a","b","c"};
        String[] customTestOutput = {"d","e","f","g"};
        String[] formulas = {"a+b+1","a-b+1","b-a+1","c+a+1"};
        DataStructure robotics = new DataStructure("Robotics", "robotics", roboticsInput, roboticsOutput,"double","custom",roboticsFormula);
        DataStructure customTest = new DataStructure("Custom","custom",customTestInput,customTestOutput,"int","random");
        
        //persistCombinations(robotics);
        Double[] var1 = {1.0,1.0,1.0};
        getFromFile(robotics,var1);    
        
        
    }
    
    //putting all values to map and then to file
    public static void persistCombinations(DataStructure structure){
        
        HashMap<String, String> map = new HashMap<String,String>();
        
        // key format : "axial: #, lateral: #, yaw: #"
        // value format : "Left Front Power: #, Right Front Power: #, Left Back Power: #, Right Back Power: #"
        
        int n = 3; // Number of recursions
        int x = 10; // Maximum value for each position
        List<Object[]> combinations = new ArrayList<>();
        Utils.generateCombinations(n,x, combinations, structure.getInputType());
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        
        switch(structure.getOutputType()){
            case "custom" :
                
                for (Object[] combination : combinations) {
                    String input = Utils.buildInputString(structure.getInputStrings(), combination);
                    String output = Utils.buildCustomOutputString(structure,combination, engine);
                    
                    map.put(input, output);
                }
                        
                break;
               
             
            case "random":
                
                for (Object[] combination : combinations) {
                    String input = Utils.buildInputString(structure.getInputStrings(),combination);
                    String output = "{";
                    
                    for(int i = 0; i < structure.getOutputStrings().length ; i++){ 
                        int rnd = (int) (Math.random() * 100) + 1;
                        
                        if(i == structure.getOutputStrings().length - 1){
                            output += structure.getOutputStrings()[i] + ": " + rnd + "}";
                        }else{
                            output += structure.getOutputStrings()[i] + ": " + rnd + ", ";
                        }
                        
                    }
                    map.put(input,output);
                }
                break;
            default:
                for (Object[] combination : combinations) {
                    
                    String output = "";
                    String input = Utils.buildInputString(structure.getInputStrings(),combination);
                    for(int i = 0; i < structure.getOutputStrings().length ; i++){ 
                        if(i == structure.getOutputStrings().length - 1){
                            output += structure.getOutputStrings()[i] + ": " + combination[i] + "|";
                        }else{
                            output += structure.getOutputStrings()[i] + ": " + combination[i] + ", ";
                        }
                    }
                    map.put(input,output);
                }
                break;
        }
        
        
        printToFile(map, structure.getFileName());
        
    }
    
  
    public static void getFromFile(DataStructure structure,Object[] values){
        try{
            Scanner fileIn = new Scanner(new File(structure.getFileName() + ".txt"));
            
            String line = fileIn.nextLine();
            String wantedValues = "";
            for(int i = 0; i < structure.getInputStrings().length ; i++){ 
                if(i == structure.getInputStrings().length - 1){
                    wantedValues += structure.getInputStrings()[i] + ": " + values[i];
                }else{
                    wantedValues += structure.getInputStrings()[i] + ": " + values[i] + ", ";
                }
                
            }
            int index1 = line.indexOf(wantedValues);
            int index2 = line.indexOf("},");
            String str1 = line.substring(index1 -1, index2 + index1 -1);
            System.out.print(str1);
            
        }catch(IOException e){
            System.out.println(e);
        }
        
    }
    public static void printToFile(HashMap<String, String> map, String textName) {
        try (PrintWriter fileOut = new PrintWriter(textName + ".txt")) {
            fileOut.print("{");
    
            int count = 0;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                fileOut.print(entry.getKey() + ":" + entry.getValue());
    
                count++;
                if (count < map.size()) {
                    fileOut.print(",");
                }
            }
    
            fileOut.print("}");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
