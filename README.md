# The Data Forge
### TODO: 

- [ ] Make a gui for ease of use
  - [x] make a tab to add fileStructures, and add files/ file data
  - [ ] be able to fiew the files from within the gui
  - [ ] be able to get certain data from a given file

- [ ] optimize code (make run faster and remove any unnecessary things if it can be done a different easier way)



 ### History:
                This idea first started when I needed to find all of the combinations for motor values given a certain input set.
                The equation for getting the motor values was determined by yaw, lateral, and axial inputs of a joystick.
                I initailly wanted to make a truth table but soon realized that this would be a daunting task and wanted a better way to do it.
                I then realized that I could simply code something that can do it for me.  This turned into about 3 weeks of figuring out the basics 
                of what needed to be done, and polishing it.  As time went on, I made the program more generalized until I realized what I wanted it to become.
                this led to me spending even more time learning things like the file librarys of java and asking ChatGPT for help.  Once I was almost done
                with the console part, I realized that I needed something extra... I needed a GUI.  This is coming in the future and that final product will be 
                my AP CSA class's final project.
### Description:
    you now have the ability to easily create data structures of any kind.  If you have keys that have a specifc value for them,
    and values that have values for them this is the right program for you.  You can manipulate datastructures of any type.
    It works best if you have a {key:value = key2:value2}.  
    
    For example if I have apple and want a value of 1 in 
    apple it would look like this: apple:1, but if I now have a variable tasteFactor that relys on apple's value (for instance using a 
    formula of (apple * 10) ) how would you get that value?  This data manipulator does the heavy lifting and gets you that value
    
    the output of this example would be :  {{apple:1},{tasteFactor:10}}
    
    this program is more useful for complex formulas
    
    for example if we now add bananna and orange to the first set and make the formula for tasteFactor = (bannana * 2) - orange + apple
    the new pair would look like: {{apple:1,bannana:1,orange:1},{tasteFactor:2}}
    
    this essentally makes a dataset of functions and their outputs for given variables.
    This works best for finding all of the possible values for a given data set.
    
    for example, if you wanted to find all of the possible combinations for the apple, bannana, and orange values and their tastFactor 
    you would use this program.
### Instructions:
    create a DataStructure object that contains the name of the file, a String[] for the input keys, a String[] for the output keys,
    the input data Type, output dataType, and if you put "custom" as your outputDataType then you will need a String[] for the formulas.
    
    when making the formulas, each index of the formula coresponds with the index of an output key.
    for example if you have the output keys of {a,b,c,d} and had a formula of {1+2,3+3,2-2,1-5} formula[0] would be for key "a"
    
                
                
