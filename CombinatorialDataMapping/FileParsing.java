import java.util.*;
import java.text.DecimalFormat;
import javax.script.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import java.nio.file.Path;




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
        
        // add more default things like "random" for varying outputs
        
        // make a gui for ease of use
        // -make a tab to add fileStrucutres, and add files / file data
        // - be able to view the files from within the gui
        // - be able to get certain data from a given file
        // - make a debug console tab if the console does not print anything out anymore
        
        String[] roboticsInput = {"axial","lateral","yaw"};
        String[] roboticsOutput = {"Left Front Power", "Right Front Power","Left Back Power","Right Back Power"}; 
        String[] roboticsFormula = {"axial +  lateral +  yaw","axial - lateral - yaw",
                                    " axial -  lateral + yaw", "axial +  lateral - yaw"};
                                    
        String[] customTestInput = {"a","b","c"};
        String[] customTestOutput = {"d","e","f","g"};
        String[] formulas = {"a+b+1","a-b+1","b-a+1","c+a+1"};
        
        String[] test2Input = {"test1","test2","test3"};
        String[] test2Output = {"test4","test5"};
        

        DataStructure robotics = new DataStructure("robotics", roboticsInput, roboticsOutput,"double","custom",roboticsFormula);
        DataStructure customTest = new DataStructure("custom",customTestInput,customTestOutput,"int","random");
        DataStructure Test2 = new DataStructure("test2",test2Input,test2Output, "int","random");
        

        persistCombinations(robotics);
        persistCombinations(customTest);
        persistCombinations(Test2);
        
        Double[] var1 = {1.0,1.0,1.0};
        Integer[] var2 = {1,1,1};
        
        //getFromFile(robotics,var1);    
        //getFromFile(customTest,var2);
        //getFromFile(Test2,var2);
      
    }
    
    //putting all values to map and then to file
    public static void persistCombinations(DataStructure structure){
        HashMap<String, String> map = new HashMap<String,String>();
        
        if(isInConfig(structure)){
            return;
        }else{
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
            printToFile(map, structure);
        }
    }
    
    public static void deleteDataStructure(String structureName){
        try(Scanner configFile = new Scanner(new File("config.txt"))){
            ArrayList<String> fullFile = new ArrayList<String>();
            while(configFile.hasNext()){
                String line = configFile.nextLine();
                fullFile.add(line);
            }
            for(String str : fullFile){
                if(str.equals(structureName + ".txt")){
                    fullFile.remove(str);
                    break;
                }
            }
            try(PrintWriter newConfigFile = new PrintWriter("config.txt")){
                for(String str:fullFile){
                    newConfigFile.println(str);
                }
            }catch(IOException e){
                System.out.print(e);
            }
            
            File file = new File(structureName + ".txt");
            file.delete();
        }catch(IOException e){
            System.out.println("config.txt does not exist");
        }
        System.out.println("successfully removed: " + structureName);
        
    }
    public static void getFromFile(DataStructure structure,Object[] values){
    
        try(Scanner fileIn = new Scanner(new File(structure.getFileName() + ".txt"))){
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
      
            String str1 = line.substring(index1 -1, index2 + index1-1);
            System.out.print(str1);
            fileIn.close();
        }catch(IOException e){
            System.out.println("There is no file");
        }
                
    }
    
    public static void printToFile(HashMap<String, String> map, DataStructure structure) {
        try (PrintWriter fileOut = new PrintWriter(structure.getFileName() + ".txt")) {
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
        addToConfig(structure);
        
    }
    
    public static void addToConfig(DataStructure structure) {
        // Write the updated content to config.txt
        try (PrintWriter newConfigFile = new PrintWriter(new FileWriter("config.txt",true))) {
                newConfigFile.println(structure.getFileName() + ".txt");
        } catch (IOException e) {
            System.out.print(e);
        }

    }

    public static boolean isInConfig(DataStructure structure){
        String fileName = structure.getFileName() + ".txt";
        try(PrintWriter fileOut = new PrintWriter(new FileWriter(fileName,true))){
            try(Scanner configFile = new Scanner(new File("config.txt"))){
                ArrayList<String> data = new ArrayList<String>();
                if(!configFile.hasNext()){
                    configFile.close();
                    return false;
                }
                while (configFile.hasNext()) 
                {
                    // Reads the entire line
                    String lineIn = configFile.nextLine(); 
                    // Output the line
                    data.add(lineIn);
                }
                for(String str: data){
                    if(str.equals(fileName)){
                        configFile.close();
                        return true;
                    }
                }
                configFile.close();
                return false;
                
            }catch(IOException e){
                System.out.println("There is no config file!");
                return false;
            }
        }catch(IOException e){
            System.out.println("File was not created!");
            return false;
        }
    }
}
