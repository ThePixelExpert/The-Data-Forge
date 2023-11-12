import java.util.*;
import java.text.DecimalFormat;
import java.io.IOException;

public class DataStructure {
    private String[] inputStrings;
    private String[] outputStrings;
    private String inputType;
    private String outputType;
    private String structureName;
    private String fileName;
    private String[] customOutputFormula;
    
    // the purpose of this class is to create a structure for the file, inputs, and outputs of a map and given output function
    // the main use of this for now would be to make random values and combinations of such and get outputs based on those values.
    // it for right now only has combinational inputs (every combination of a given max value for n amount of index's) but can be used in other ways in the future
    public DataStructure(String dataStructureName, String fileName, String[] inputs, String[] outputs, String inputType, String outputType){
        this.structureName = dataStructureName;
        this.inputStrings = inputs;
        this.outputStrings = outputs;
        this.inputType = inputType;
        this.outputType = outputType;
        this.fileName = fileName;
    }
    public DataStructure(String dataStructureName, String fileName, String[] inputs, String[] outputs, String inputType, String outputType, String[] customOutput){
        this.structureName = dataStructureName;
        this.inputStrings = inputs;
        this.outputStrings = outputs;
        this.inputType = inputType;
        this.outputType = outputType;
        this.fileName = fileName;
        //it would be put into an array as follows (demonstrated with what it would look like for the robotics one):
        // axial + lateral - yaw, lateral - axial - yaw ...;
        // if it contains a variable from the input such as axial, when splitting check for that name
        // each position of an equation has to match the position of the output string that it is assigned to
        // for example "axial + lateral - yaw" would be in pos[0] and in pos[0] for robotics output would be LeftFrontPower
        
        this.customOutputFormula = customOutput;
    }
    
    public void setInputStrings(String[] inputs){
        this.inputStrings = inputs;
    }
    
    public String[] getInputStrings(){
        return inputStrings;
    }
    
    public void setOutputStrings(String[] outputs){
        this.outputStrings = outputs;
    }
    
    public String[] getOutputStrings(){
        return outputStrings;
    }
    
    public String getStructureName(){
        return structureName;
    }
    public String getInputType(){
        return inputType;
    }
    public String getOutputType(){
        return outputType;
    }
    public String getFileName(){
        return fileName;
    }
    public String[] getOutputFormula(){
        return customOutputFormula;
    }
    public void setOutputFormula(String[] formula){
        this.customOutputFormula = formula;
    }
    public int getIndexForInputString(String key) {
        for (int i = 0; i < inputStrings.length; i++) {
            if (inputStrings[i].equals(key)) {
                return i;
            }
        }
        return -1; // Return -1 if the input string is not found
    }
}