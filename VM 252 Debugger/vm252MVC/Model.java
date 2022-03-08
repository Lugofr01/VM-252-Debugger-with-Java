/*
// Contributers: Uddam Chea, Frank Lugola, Lonyjera Okal, and Benjamin Hamilton
// Class: cs252
// Assignment: Phase 10
// Date: 12/13/2021
//
// This holds the model of our program and the Functionality of the program.
*/ 

//Imports
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import vm252utilities.VM252Utilities;
import vm252architecturespecifications.VM252ArchitectureSpecifications;
import java.util.Scanner;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


// See Documentation for PropertyChangeListener and PropertyChangeSupport
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeListener.html
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeSupport.html


public class Model {
    private PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);
    private short accumulator;
    private int programCounter;
    private String objFileName;
    private Boolean start;
    private byte[] memory;
	private Boolean[] memoryBreakpoint;
    private Boolean pause;
    private Boolean stepState;
    private final String helpmsg = 
    "Load - This button provides a pop up selector that allows the user to select the files from the finder to be executed by the software\n" +
    "Start - This button allows the user to begin the execution of the program\n" +
    "Pause- This allows the user to temporary halt the program\n" +
    "Resume- This button allows the user to proceed with the program after pressing the pause button\n" +
    "Help - This button allows the user to see how different buttons of the program work and explains the features of the program\n" +
    "Step - This button allows the user to step through the program\n" +
    "Stop Stepping - Reverses the instructions from the step button\n" +
    "Run Next- This button allows the user to execute a single line of code at a time\n" +
    "Quit - This buttons allow the user to end the session of the program and closes the application\n" +
    "Help - This button allows the user to see how different buttons of the program work and explains the features of the program\n";
    private List<String> operandStrList;
    private List<String> opcodeStrList;
    private List<String> opcodeTypeList;
    private List<String> memoryList;
    private List<String> labelList;
	private List<Boolean> breakpointList;
    private int pauseTime = 0;
    public static final int LOAD_OPCODE = 0;
    public static final int STORE_OPCODE = 1;
    public static final int ADD_OPCODE = 2;
    public static final int SUBTRACT_OPCODE = 3;
    public static final int JUMP_OPCODE = 4;
    public static final int JUMP_ON_ZERO_OPCODE = 5;
    public static final int JUMP_ON_POSITIVE_OPCODE = 6;
    public static final int SET_OPCODE = 14;
    public static final int INPUT_OPCODE = 60;
    public static final int OUTPUT_OPCODE = 61;
    public static final int NO_OP_OPCODE = 62;
    public static final int STOP_OPCODE = 63;
    public static final int MAX_LENGTH = 8192;
    private static Map<Byte, String> opCodeToOpString;
	
	// Map intruction/data(statement) List indices to objectCode index
	private  Map<Integer, Integer> programCounterToStatment = new HashMap<>();
	private  Map<Integer, Integer> statmentToFirstProgramCounter = new HashMap<>();
    private  Map<Integer, String> StatementToLabel = new HashMap<>();
    // Populating the static map
    static
    {
        opCodeToOpString = new HashMap<>();
        opCodeToOpString.put((byte)0, "LOAD");
        opCodeToOpString.put((byte)1, "STORE");
        opCodeToOpString.put((byte)2, "ADD");
        opCodeToOpString.put((byte)3, "SUB");
        opCodeToOpString.put((byte)4, "JUMP");
        opCodeToOpString.put((byte)5, "JUMPZ");
        opCodeToOpString.put((byte)6, "JUMPP");
        opCodeToOpString.put((byte)14, "SET");
        opCodeToOpString.put((byte)60, "INPUT");
        opCodeToOpString.put((byte)61, "OUTPUT");
        opCodeToOpString.put((byte)62, "NOOP");
        opCodeToOpString.put((byte)63, "STOP"); 
    }


    private byte [] objectCode = null;
    private byte [] sourceFileInformation = null;
    private byte [] executableSourceLineMap = null;
    private byte [] symbolicAddressInformation = null;
    private byte [] byteContentMap = null;

// getters and setters for class variables.
    public byte[] getObjectCode(){
        return this.objectCode;
    }
	
    public void setObjectCode(byte[] subject){
        this.objectCode = subject;
    }

    public byte[] getSourceFileInformation(){
        return this.sourceFileInformation;
    }

    public void setSourceFileInformation(byte[] subject){
        this.sourceFileInformation = subject;
    }
    
    public byte[] getExecutableSourceLineMap(){
        return this.executableSourceLineMap;
    }

    public void setExecutableSourceLineMap(byte[] subject){
        this.executableSourceLineMap = subject;
    }

    public byte[] getSymbolicAddressInformation(){
        return this.symbolicAddressInformation;
    }

    public void setSymbolicAddressInformation(byte[] subject){
        this.symbolicAddressInformation = subject;
    }

    public byte[] getByteContentMap(){
        return this.byteContentMap;
    }

    public void setByteContentMap(byte[] subject){
        this.byteContentMap = subject;
    }

    public Map<Integer, Integer> getStatmentToFirstProgramCounter(){
        return this.statmentToFirstProgramCounter;
    }

    public void setStatmentToFirstProgramCounter(Map<Integer, Integer> subject){
        this.statmentToFirstProgramCounter = subject;
    }

    public Map<Integer, Integer> getProgramCounterToStatment(){
        return this.programCounterToStatment;
    }

    public void setProgramCounterToStatment(Map<Integer, Integer> subject){
        this.programCounterToStatment = subject;
    }

    public String getHelpmsg(){
         return this.helpmsg;
     }


    
	// Constructor
    public Model(short ACC, int PC) {
		this.accumulator = ACC;
		this.programCounter = PC;
		this.pause = false;
		this.stepState = false;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	    pcSupport.removePropertyChangeListener(listener);
    }
    public short getAccumaltor() {
    return this.accumulator;
    }
	
	//
    //public void setAccumaltor(short ACC)
    //
    // Purpose:
    //     Sets the value of Accumulator as the program executes.
    //    
    // Formals:
    //     short ACC
    //
    // Pre-conditions:
	//		accumulator exists.
	//		
    // Post-conditions:
    //     New accumulator value is set to ACC
    //
    // Returns:
    //     void- returns nothing.
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //
    public void setAccumaltor(short ACC) {
        short oldValue = this.accumulator;
        this.accumulator = ACC;
        pcSupport.firePropertyChange("ACC", oldValue, this.accumulator);

    }
    
    public int getProgramCounter() {
    return this.programCounter;
    }

    //
    //public void setProgramCounter(int PC)
    //
    // Purpose:
    //     Sets the value of Program Counter as the program executes.
    //    
    // Formals:
    //     int PC
    //
    // Pre-conditions:
	//		programCounter exists.
	//		
    // Post-conditions:
    //     New programCounter value is set to PC
    //
    // Returns:
    //     void- returns nothing.
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //

    public void setProgramCounter(int PC) {
        int oldValue = this.programCounter;
        this.programCounter = PC;
        
        pcSupport.firePropertyChange("PC", oldValue, PC);
    }
	
	//
    //public Boolean loadObjFile(String fileName)
    //
    // Purpose:
    //     Load an obj file for the debugger to executes.
    //    
    // Formals:
    //     String fileName
    //
    // Pre-conditions:
	//		The following class variables need to be populated
    //      this.parseObjectFile(fileName)
    //		this.parseSymbolicAddressInformation()
	//		
    // Post-conditions:
    //     none
    //
    // Returns:
    //     A boolean value of True or False.
    //
    // Worst-case asymptotic runtime:
    //     O(length of symbolicAdressInformation) + O(1 + (the file-length of the file having the name in objectFileName))
    //
    public Boolean loadObjFile(String fileName){
        this.resetPC();
        this.parseObjectFile(fileName);
        //parseObjectFile sets this.objectCode
        if(!this.parseSymbolicAddressInformation()){
            return false;
        }
        if(!this.parseProgram(this.objectCode)){
            return false;
        }
        byte [] objectCode = this.getObjectCode();
        this.setMemory(objectCode);
        
        
        return true;
    }
	
	//
    //public Boolean parseSymbolicAddressInformation()
    //
    // Purpose:
    //     Parse address information simulated in vm252.
    //    
    // Formals:
    //     none
    //
    // Pre-conditions:
	//		The following class variables need to be populated
    //		this.symbolicAddressInformation()
	//		
    // Post-conditions:
    //     none
    //
    // Returns:
    //     A boolean value of True or False.
    //
    // Worst-case asymptotic runtime:
    //     O(length of symbolicAdressInformation)
    //

    public Boolean parseSymbolicAddressInformation(){
        String label = "";
        Integer address;
        int start = 0;
        int end = 0;
        for(int i = 0; i < this.symbolicAddressInformation.length; ++i){
            byte item = this.symbolicAddressInformation[i];
            if(item != 0){  
                end = i ; 
            }
            else{
                label = new String(Arrays.copyOfRange(this.symbolicAddressInformation, start, end + 1));
                address = this.symbolicAddressInformation[i+1] << 24 | 
                        this.symbolicAddressInformation[i+2] << 16 |
                        this.symbolicAddressInformation[i+3] << 8 |
                        this.symbolicAddressInformation[i+4];
                i = i + 4;
                start = i + 1;
                StatementToLabel.put(address, label);
            }
            
        }

        return true;

    }
    
    public void setMemory(byte[] mem) {
        
        byte[] oldValue;
        if(this.memory == null){
            oldValue = null;
        }
        else{
            oldValue = new byte[this.memory.length];
            System.arraycopy(this.memory, 0, oldValue, 0, this.memory.length);
        }
        
        this.memory = new byte[mem.length];
        System.arraycopy(mem, 0, this.memory, 0, mem.length);
        pcSupport.firePropertyChange("memory", oldValue, this.memory);

    }
    public byte[] getMemory() {
        return this.memory;      
    }

    public void setPauseTime(int pauseTime){
        this.pauseTime = pauseTime;
    }
    public int getPauseTime(){
        return this.pauseTime;
    }
	
	public void setMemoryBreakpoint(Boolean[] memBP) {
        Boolean[] oldValue;
        if(this.memoryBreakpoint == null){
            oldValue = null;
        }
        else{
            oldValue = new Boolean[this.memoryBreakpoint.length];
            System.arraycopy(this.memoryBreakpoint, 0, oldValue, 0, this.memoryBreakpoint.length);
        }
        
        this.memoryBreakpoint = new Boolean[memBP.length];
        System.arraycopy(memBP, 0, this.memoryBreakpoint, 0, memBP.length);
        pcSupport.firePropertyChange("breakpoint", oldValue, this.memoryBreakpoint);

    }
    public Boolean[] getMemoryBreakpoint() {
        return this.memoryBreakpoint;  
    }

    public void setMemoryList(List<String> memoryList){
        this.memoryList = memoryList;
    }



    public List<String> getMemoryList(){
        return this.memoryList;
    }

    public void setLabelList(List<String> labelList){
        this.labelList = labelList;
    }

    public List<String> getLabelList(){
        return this.labelList;
    }

    public List<Boolean> getBreakpointList(){
        return this.breakpointList;
    }
	
	public void setBreakpointList(List<Boolean> breakpointList){
		// List<Boolean> oldValue;
		// if(this.memoryBreakpoint == null){
		//     oldValue = null;
		// }
		// else{
		//     oldValue = this.breakpointList;
		// }
		this.breakpointList = breakpointList;
		pcSupport.firePropertyChange("breakpointlist", this.breakpointList, this.breakpointList);
    }
	
    public void setOpcodeStrList(List<String> opcodeStrList){
        this.opcodeStrList = opcodeStrList;
    }

    public List<String> getOpcodeStrList(){
        return this.opcodeStrList;
    }

    public void setOperandStrList(List<String> operandStrList){
        this.operandStrList = operandStrList;
    }

    public List<String> getOperandStrList(){
        return this.operandStrList;
    }

    public void setOpcodeTypeList(List<String> opcodeTypeList){
        this.opcodeTypeList = opcodeTypeList;
    }

    public List<String> getOpcodeTypeList(){
        return this.opcodeTypeList;
    }
    //pause
    public void setPause(Boolean pause){
        this.pause = pause;
    }

    public Boolean getPause(){
        return this.pause;
    }

    public void runProgramThreaded(){
        Thread runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    runProgram();               
                }
                catch(Exception e){
                  JOptionPane.showMessageDialog(null, "Error running program.\n Inspect object code.", "Info", JOptionPane.INFORMATION_MESSAGE);
                            
                }
            }
        });  
        runThread.start();
    }
    
    public void setStepState(Boolean stepState){
        this.stepState = stepState;
    }
    public Boolean getStepState(){
        return this.stepState;
    }
    
    //
    // public boolean runOneInstructions(byte [] instruction)
    //
    // Purpose:
    //     Simulates the execution of a VM252 program whose binary encoding is found in
    //     an array of bytes
    //
    // Formals:
    //     byte instruction - containing the binary encoding of the
    //         VM252 program whose execution is to be simulated
    //
    // Pre-conditions:
    //     none
    //
    // Post-conditions:
    //     input may have been read from the standard input stream
    //     output may have been written to the standard output stream
    //
    // Returns:
    //     boolean value.
    //
    //
    // Worst-case asymptotic runtime:
    //     O(1
    //         + max(
    //             (the # number of simulated VM252 instruction executions),
    //             (the number of invalid user-inputs entered)
    //             )
    //         )
    //

    public boolean runOneInstructions(byte [] instruction)  { 
        int numBytes = instruction.length;
        short ACC = getAccumaltor();
        //byte [] memory = getMemory();
        boolean returnVal = true;
        int offset = getProgramCounter();
        int[] components;
        int operand = 0;
        int opcode = 0;

		components = vm252architecturespecifications.VM252ArchitectureSpecifications.decodedInstructionComponents(instruction);
		opcode = components[0];
		if (numBytes == 2) {
			operand = components[1];
		}
		// must set offset inside switch statement that
		// executes one instruction
		// Begin execute one instruction
		switch (opcode) {
			case LOAD_OPCODE:
				//ACC = (short) (this.memory[operand] << 8  | this.memory[(operand+1)%MAX_LENGTH]);  
				ACC = bytesToInteger(this.memory[operand], this.memory[(operand+1)%MAX_LENGTH]);                         
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case STORE_OPCODE:
				this.memory[operand] = (byte) ( ACC >> 8 );
				this.memory[(operand+1)%MAX_LENGTH] = (byte) ( ACC & 0xFF);
				offset = (offset + numBytes) % MAX_LENGTH;
				this.setMemory(this.memory);
				break;
			case ADD_OPCODE:
				//ACC += this.memory[operand] << 8 | this.memory[(operand+1)%MAX_LENGTH];
				ACC += bytesToInteger(this.memory[operand], this.memory[(operand+1)%MAX_LENGTH]); 
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case SUBTRACT_OPCODE:
				//ACC -= this.memory[operand] << 8 | this.memory[(operand+1)%MAX_LENGTH];
				ACC -= bytesToInteger(this.memory[operand], this.memory[(operand+1)%MAX_LENGTH]); 
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case JUMP_OPCODE:
				offset = operand;
				break;
			case JUMP_ON_ZERO_OPCODE:
				if ( ACC == 0){
					offset = operand;
				}
				else {
					offset = (offset + numBytes) % MAX_LENGTH;
				}
				break;
			case JUMP_ON_POSITIVE_OPCODE:
				if ( ACC > 0){
					offset = operand;
				}
				else {
					offset = (offset + numBytes) % MAX_LENGTH;
				}
				break;
			case SET_OPCODE:
				ACC = (short) operand;
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case INPUT_OPCODE:
				// change this more sutibale for a gui 
			   // Scanner input = new Scanner(System.in);
			   boolean isInt;
			   int inputNum = 0;
			   String input;
			   do{
				input = JOptionPane.showInputDialog("Input: ");
				try{
					inputNum = Integer.parseInt(input);
					isInt = true;
				}
				catch(NumberFormatException e){
					//handle control D/Z
					isInt = false;
				}
			   }while(!isInt && input != null); //add and not Ctrl D/Z

				if(isInt) {
					ACC = (short)inputNum;
					}
					else { // assert received Ctrl-D or Ctrl-Z
						opcode = STOP_OPCODE;
						System.out.println("User clicked Cancel or closed input dialog.");
					}
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case OUTPUT_OPCODE:;
				JOptionPane.showMessageDialog(null, "Output : " + getAccumaltor(), "Output", JOptionPane.INFORMATION_MESSAGE);

				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case NO_OP_OPCODE:
				offset = (offset + numBytes) % MAX_LENGTH;
				break;
			case STOP_OPCODE:
				//offset = (offset + numBytes) % MAX_LENGTH;        
				break;
			default:
				System.out.println("bad instruction default case");
				offset = (offset + numBytes) % MAX_LENGTH;
				returnVal = false;
		} // end switch
	  
		setProgramCounter(offset);
		setAccumaltor(ACC);
		return returnVal;
    }
 
	//
    //public static void runProgram()
    //
    // Purpose:
    //     Simulates execution of the VM252 program whose binary encoding is found in program.
    //    
    //
    // Formals:
    //     none
    // Pre-conditions:
	//		The following class variables need to be populated, which they would be following 
	//		a call to parseProgram.
    //      this.memory contains a byte array of opcodes and operands.
    //		this.programCounter contains a valid value, which is any 
	//		valid index into the this.memory array.
	//
	// Dependencies:
	//		VM252Utilities.numInstructionBytes()
	//		VM252Utilities.getInstruction()
	//		vm252architecturespecifications.VM252ArchitectureSpecifications.decodedInstructionComponents()
	//		this.isMABreakPoint()
	//		this.runOneInstructions()
	//		this.getPause() 
    //      this.getStepState()
	//		various getters and setters, but especially the
	//		following setters, because these setters fire events 
	//		that inform Listeners configured in the Controller to
	//		update the View.
	//		this.setProgramCounter()
	//		Note that this.runOneInstruction() isn't a setter, but
	//		calls several setters that similarly fire events that 
	//		update the view. 
	//		
    // Post-conditions:
    //     none
    //
    // Returns:
    //     void- returns nothing.
    //
    // Worst-case asymptotic runtime:
    //     O(program.length)
    //
    
    public void runProgram() throws Exception{
        
        if(this.getProgramCounter() >= this.memory.length-1 || this.getProgramCounter() < 0 ){
            int result = JOptionPane.showConfirmDialog(
                null, "The Program Counter is >= the length of the code.\nDo you want to set the Program Counter to 0? "
                , "Set Program Counter to Zero: ", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    this.resetPC();
                }              

                else if (result == JOptionPane.NO_OPTION)   
                   throw new Exception();            
        }

        if(this.getProgramCounterToStatment().containsKey(this.getProgramCounter())){
            int currentStatmentIndex = this.getProgramCounterToStatment().get(this.getProgramCounter());
            if (this.getOpcodeStrList().get(currentStatmentIndex) == "STOP"){
                int result = JOptionPane.showConfirmDialog(
                null, "The Program Counter is pointing to a STOP code.\nDo you want to set the Program Counter to 0? "
                , "Set Program Counter to Zero: ", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION){
                    this.resetPC();
                }   
            }
        }
        
        if (this.memory.length <= MAX_LENGTH){

            int opcode;

            byte [] instruction;
            int[] components;
           // int operand = 0;
		    boolean successFlag = false;
            
            // initialize loop control variables
            opcode = -1;//just to get in the while loop
            boolean MABreakPoint = false;
            //Boolean stepper = false;
            //end initialize loop control variables
            do{
				Thread.sleep(pauseTime);
                
                int numBytes = VM252Utilities.numInstructionBytes(this.memory[getProgramCounter()]);
                if (numBytes == 1 || numBytes == 2) {
                    instruction = VM252Utilities.getInstruction(this.memory, getProgramCounter(), numBytes);
                    if (instruction != null) {
                        components = vm252architecturespecifications.VM252ArchitectureSpecifications.decodedInstructionComponents(instruction);
                        opcode = components[0];
                        // runOneIstructions modifies programCounter which is part of the loop control
                        successFlag = runOneInstructions(instruction);
                        MABreakPoint = isMABreakPoint(instruction);
                    } //end instruction is not null
                    else {
                        // quit, bad instruction
                        opcode = STOP_OPCODE;
                    }
                } //if numBytes isn't 1 or 2
                else {
                    // quit because invalid instruction
                    opcode = STOP_OPCODE;
                }
                if(this.getMemoryBreakpoint()[this.getProgramCounter()]){
                    vm252utilities.VM252Utilities.playSound("sound.wav");
                }
            }while (opcode != STOP_OPCODE 
            && programCounter <= this.memory.length 
            && !this.getPause() 
            && !this.getStepState()
            && !this.getMemoryBreakpoint()[this.getProgramCounter()]
            && !MABreakPoint);//end while
        } // end if program.length <= MAX_LENGTH
    else {
        System.out.println("Program is greater than max length.");
    }
    } // end runProgram

    public Boolean isMABreakPoint(byte [] instruction){
        int numBytes = instruction.length;
        int[] components;
        int operand = 0;
        int opcode = 0;

        components = vm252architecturespecifications.VM252ArchitectureSpecifications.decodedInstructionComponents(instruction);
        opcode = components[0];
        if (numBytes == 2) {
            operand = components[1];
        }
        switch (opcode) {
            case LOAD_OPCODE:
                if(memoryBreakpoint[operand] || memoryBreakpoint[(operand+1)%MAX_LENGTH]){
                    return true;
                }
                break;
            case STORE_OPCODE:
                if(memoryBreakpoint[operand] || memoryBreakpoint[(operand+1)%MAX_LENGTH]){
                    return true;
                }
                break;
            case ADD_OPCODE:
                if(memoryBreakpoint[operand] || memoryBreakpoint[(operand+1)%MAX_LENGTH]){
                    return true;
                }
                break;
            case SUBTRACT_OPCODE:
                if(memoryBreakpoint[operand] || memoryBreakpoint[(operand+1)%MAX_LENGTH]){
                    return true;
                }
                break;
            case SET_OPCODE:
                if(memoryBreakpoint[operand]){
                    return true;
                }
                break;
            default:
                return false;
        } // end switch
        
    
	return false;
    }

	//
    // public Boolean parseProgram( byte[] byteCode)
    //
    // Purpose:
	//		Parse the five arrays produced by the method parseObjectCode
	//		to create data structures that capture the essentials needed 
	//		for debugging the original vm252 assembly language program
	//		represented in the vm252obj file.
			
    //     VM252 program whose binary encoding is found in program.
    //    
    //
    // Formals:
    //     byteCode (in) - a byte array wich is a binary encoding of 
	//		vm252 instructions and 
    // Pre-conditions:
    //
	//		The following arrays of bytes contain information from the
	//		vm252obj file, as produced by the parseObjectFile method.
	//
    //      byte [] executableSourceLineMap
    //      byte [] symbolicAddressInformation
    //      byte [] byteContentMap
	//		byte [] objectCode, Note: objectCode is passed in as the  
	//		formal parameter byteCode.
    //
    // Post-conditions:
    // 		The following ArrayLists and HashMaps are populated with
	//		the essential information needed for debugging.
	//		ArrayLists:
	//			breakpointList
	//			labelList
	//			memoryList
	//			opcodeStrList
	//			operandStrList
	//			opcodeTypeList
	//		HashMaps:
	//			StatementToLabel
	//			programCounterToStatment
	//			statmentToFirstProgramCounter	
    //
    // Returns:
    //     Boolean 
	//			True indicates successful parsing of the objectCode.
    //
    // Worst-case asymptotic runtime:
    //     O(byteCode.length)
    //
    public Boolean parseProgram( byte[] byteCode){
        byte[] byteContentMap = new byte[this.getByteContentMap().length];
        byte [] objectCode = new byte[byteCode.length];
		if (this.memoryBreakpoint == null) {
			this.memoryBreakpoint = new Boolean[byteCode.length];
			for (int i = 0; i < objectCode.length; ++i){
				this.memoryBreakpoint[i] = false;
			}
		}
        if(byteContentMap.length != objectCode.length || objectCode.length > MAX_LENGTH){
            return false;
        }
        for (int i = 0; i < objectCode.length; ++i){
            objectCode[i] = byteCode[i];
            byteContentMap[i] = this.getByteContentMap()[i];
        }
        

        // offset is an integer pointing to the current byte in objectCode[]
        int offset = 0;
        String operandStr;
        String opcodeStr;
        String opcodeType;
        byte [] instruction;
        int[] components;
        int opcode;
        int operand = 0;
        int data;
        String itemString = "";
        List<String> memoryList = new ArrayList<>();
        List<String> labelList = new ArrayList<>();
		List<Boolean> breakpointList = new ArrayList<>();
        List<String> operandStrList = new ArrayList<>();
        List<String> opcodeStrList = new ArrayList<>();
        List<String> opcodeTypeList = new ArrayList<>();
		
        do {
            if(StatementToLabel.containsKey(offset)){
                labelList.add(StatementToLabel.get(offset));
                
            }
            else{
                labelList.add("");
            }
            // This is the case where the object code is part of an instruction.
            if(byteContentMap[offset] == 1){
				breakpointList.add(this.memoryBreakpoint[offset]);
				this.programCounterToStatment.put(offset, breakpointList.size()-1);
				this.statmentToFirstProgramCounter.put(breakpointList.size()-1, offset);

                operandStr = "";
                opcodeType = "Instruction";
                int numBytes = VM252Utilities.numInstructionBytes(objectCode[offset]);
                if (numBytes != 1 && numBytes != 2) {
                    return false;
                }
                instruction = VM252Utilities.getInstruction(objectCode, offset, numBytes);
                if (instruction == null) {
                    return false;
                }
                itemString = vm252utilities.VM252Utilities.byteArrayToString(instruction);
                components = vm252architecturespecifications.VM252ArchitectureSpecifications.decodedInstructionComponents(instruction);
                opcode = components[0];
                opcodeStr = opCodeToOpString.get((byte)opcode);
                if(components.length == 2){
					this.programCounterToStatment.put(offset+1, breakpointList.size()-1);
                    operand = components[1];
                    operandStr = String.valueOf(operand);
                    //itemString = vm252utilities.VM252Utilities.byteArrayToString(vm252architecturespecifications.VM252ArchitectureSpecifications.encodedInstructionBytes((int)opcode, (int)operand));
                    
                }
                offset += numBytes;
            }
            // This is the case where the object code is dataDirective.
			// A dataDirective specifies a two-byte signed-integer value 
			// to be stored initially into memory at the point in the 
			// program where the directive occurred.
            else{
				breakpointList.add(this.memoryBreakpoint[offset]);
				// need to check if offset+1 is valid index.
				this.statmentToFirstProgramCounter.put(breakpointList.size()-1, offset);
                data = bytesToInteger(objectCode[offset], objectCode[(offset+1)%MAX_LENGTH]);
                itemString = shortHex((short)data);
                opcodeType = "Data";
                opcodeStr = "";
                operandStr = "";
                offset += 2;
            }
            memoryList.add(itemString);
            opcodeStrList.add(opcodeStr);
            operandStrList.add(operandStr);
            opcodeTypeList.add(opcodeType);
        }while (offset < objectCode.length);

		// populate fields with local working variables
        setBreakpointList(breakpointList);
        setLabelList(labelList);
        setMemoryList(memoryList);
        setOpcodeStrList(opcodeStrList);
        setOperandStrList(operandStrList);
        setOpcodeTypeList(opcodeTypeList);
		
		return true;
    } // end parseProgram
  
	//
	// public void parseObjectFile(String objectFileName)
	//
	// This is a modified version of Professor Zaring's method
	// byte [] readObjectCodeFromObjectFile(String objectFileName)
	// The only real difference is that instead of returning
	// the objectCode, this method sets private class fields for
	// objectCode and other arrays read from the VM252 object code file.
	//
	// Purpose:
	//     Reads the object code bytes from the VM252 object code file having a
	//         given name
	//
	// Formals:
	//     objectFileName (in) - the name of the VM252 object-code file to be read
	//
	// Pre-conditions:
	//     none
	//
	// Post-conditions:
	//		If a file having the name in objectFileName exists and is a valid VM252 object-code file,
	//		then the following fields  are set (as defined in the document 
	//		VM252ArchitectureSoftwareAndProgrammingInformation.pdf).
	//		objectCode:
	//     		an array of byte’s holding bytes of the object-code for the object-code file
	//         	having the name in objectFileName.
	//		sourceFileInformation:
	//			an array of bytes of source-file information.
	//     	executableSourceLineMap:
	//			an array of  bytes of the executable source-line map.
	//		symbolicAddressInformation:
	//			an array of bytes of the symbolic-address information map.
	//		byteContentMap:
	//			an array of bytes of the byte-content map.

	// Returns:
	//		none
	//
	// Worst-case asymptotic runtime:
	//     O(1 + (the file-length of the file having the name in objectFileName))
	//
    public void parseObjectFile(String objectFileName){
        this.sourceFileInformation = null;
        this.executableSourceLineMap = null;
        this.symbolicAddressInformation = null;
        this.byteContentMap = null;
        this.objectCode = null;




        byte [] objectCode = null;

        try {

            //
            // Let objectFile = a FileInputStream corresponding to the file whose name
            //     is in objectCodeFile
            //

            final FileInputStream objectFile
                = new FileInputStream(new File(objectFileName));

            byte [] sourceFileInformation = null;
            byte [] executableSourceLineMap = null;
            byte [] symbolicAddressInformation = null;
            byte [] byteContentMap = null;

            int byte3;
            int byte2;
            int byte1;
            int byte0;

            //
            // Read the content of objectFile into objectCode, sourceFileInformation,
            //     executableSourceLineMap, symbolicAddressInformation, and
            //     byteContentMap, collectively
            //

            //
            // Let objectCodeSize = the # of bytes of object code
            //

            byte3 = objectFile.read();
            byte2 = objectFile.read();
            byte1 = objectFile.read();
            byte0 = objectFile.read();

            if (byte0 == -1 || byte1 == -1 || byte2 == -1 || byte3 == -1)
                throw new IOException();

            final int objectCodeSize
                = (byte3 & 0xff) << 24 | (byte2 & 0xff) << 16
                    | (byte1 & 0xff) << 8 | byte0 & 0xff;

            //
            // Let sourceFileInformationSize = the # of bytes of source file
            //     information
            //

            byte3 = objectFile.read();
            byte2 = objectFile.read();
            byte1 = objectFile.read();
            byte0 = objectFile.read();

            if (byte0 == -1 || byte1 == -1 || byte2 == -1 || byte3 == -1)
                throw new IOException();

            final int sourceFileInformationSize
                = (byte3 & 0xff) << 24 | (byte2 & 0xff) << 16
                    | (byte1 & 0xff) << 8 | byte0 & 0xff;

            //
            // Let executableSourceLineMapSize
            //     = the # of bytes of the executable source-line map
            //

            byte3 = objectFile.read();
            byte2 = objectFile.read();
            byte1 = objectFile.read();
            byte0 = objectFile.read();

            if (byte0 == -1 || byte1 == -1 || byte2 == -1 || byte3 == -1)
                throw new IOException();

            final int executableSourceLineMapSize
                = (byte3 & 0xff) << 24 | (byte2 & 0xff) << 16
                    | (byte1 & 0xff) << 8 | byte0 & 0xff;

            //
            // Let symbolicAddressInformationSize = the # of bytes of
            //     symbolic-address information
            //

            byte3 = objectFile.read();
            byte2 = objectFile.read();
            byte1 = objectFile.read();
            byte0 = objectFile.read();

            if (byte0 == -1 || byte1 == -1 || byte2 == -1 || byte3 == -1)
                throw new IOException();

            final int symbolicAddressInformationSize
                = (byte3 & 0xff) << 24 | (byte2 & 0xff) << 16
                | (byte1 & 0xff) << 8 | byte0 & 0xff;

            //
            // Let symbolicAddressInformationSize
            //     = the # of bytes of symbolic-address information
            //

            byte3 = objectFile.read();
            byte2 = objectFile.read();
            byte1 = objectFile.read();
            byte0 = objectFile.read();

            if (byte0 == -1 || byte1 == -1 || byte2 == -1 || byte3 == -1)
                throw new IOException();

            final int byteContentMapSize
                = (byte3 & 0xff) << 24 | (byte2 & 0xff) << 16
                    | (byte1 & 0xff) << 8 | byte0 & 0xff;

            if (byteContentMapSize != 0 && objectCodeSize != byteContentMapSize)
                throw new IOException();

            //
            // Let objectCode[ 0 ... objectCodeSize-1 ] = the bytes of object code
            //

            objectCode = new byte[ objectCodeSize ];

            int objectCodeReadStatus = objectFile.read(objectCode);

            if (objectCodeReadStatus == -1)
                throw new IOException();
            this.setObjectCode(objectCode);
                    
            //
            // Let sourceFileInformation[ 0 ... sourceFileInformationSize-1 ]
            //     = the bytes of source-file information
            //

            sourceFileInformation = new byte[ sourceFileInformationSize ];

            int sourceFileNameReadStatus
                = objectFile.read(sourceFileInformation);

            if (sourceFileNameReadStatus == -1)
                throw new IOException();
            this.setSourceFileInformation(sourceFileInformation);
            //
            // Let executableSourceLineMap[ 0 ... executableSourceLineMapSize-1 ]
            //     = the bytes of the executable source-line map
            //

            executableSourceLineMap
                = new byte[ executableSourceLineMapSize ];

            int executableSourceLineMapReadStatus
                = objectFile.read(executableSourceLineMap);

            if (executableSourceLineMapReadStatus == -1)
                throw new IOException();
            this.setExecutableSourceLineMap(executableSourceLineMap);
            //
            // Let symbolicAddressInformation[ 0
            //         ... symbolicAddressInformationSize-1 ]
            //     = the bytes of symbolic-address information map
            //

            symbolicAddressInformation
                = new byte[ symbolicAddressInformationSize ];

            int symbolicAddressInformationReadStatus
                = objectFile.read(symbolicAddressInformation);

            if (symbolicAddressInformationReadStatus == -1)
                throw new IOException();
            this.setSymbolicAddressInformation(symbolicAddressInformation);

            //
            // Let byteContentMap[ 0 ... byteContentMapSize-1 ]
            //     = the bytes of the byte-content map
            //

            byteContentMap = new byte[ byteContentMapSize ];

            int byteContentMapReadStatus = objectFile.read(byteContentMap);

            if (byteContentMapReadStatus == -1)
                throw new IOException();
            this.setByteContentMap(byteContentMap);

            objectFile.close();

        }
        catch (FileNotFoundException exception) {

            ; // do nothing

            }
        catch (IOException exception) {

            ; // do nothing

        }

    //return objectCode;

    }

    public String intHex(int num){
        String hx = "";
        for(int i = 3; i>= 0; --i){
            hx = hx + String.format("%02x",(num>>8*i)&0xff);
            //System.out.print(hx );
        }
        //System.out.println("");
        return hx;
    }
	
	public String shortHex(short num){
        String hx = "";
        for(int i = 1; i>= 0; --i){
            hx = hx + String.format("%02x",(num>>8*i)&0xff);
            //System.out.print(hx );
        }
        //System.out.println("");
        return hx;
    }
	
    public String byteHex(byte mybyte){
        String hx = "";
            hx = hx + String.format("%02x",mybyte);
        
        return hx;
    }

    // Taken From Professor Zarings vm252ArchitechtureSpecifications
        // Private Class Method
        //     short bytesToInteger(byte mostSignificantByte, byte leastSignificantByte)
        //
        // Purpose:
        //     Combines two bytes into a 16-bit signed integer value
        //
        // Formals:
        //     mostSignificantByte (in) - the byte containing the sign bit and
        //         most-significant 7 bits of the integer value
        //     leastSignificantByte (in) - the byte containing the least-significant 8 bits
        //         of the integer value
        //
        // Pre-conditions:
        //     none
        //
        // Post-conditions:
        //     none
        //
        // Returns:
        //     a 16-bit short corresponding to the concatenation of the two bytes
        //
        // Worst-case asymptotic runtime:
        //     O(1)
        //

    private static short bytesToInteger(
        byte mostSignificantByte,
        byte leastSignificantByte
        )
    {

        return
            ((short)
                ((mostSignificantByte << 8 & 0xff00 | leastSignificantByte & 0xff)));

        }

    public void resetACC() {
        this.setAccumaltor((short)0);
    }
    public void resetPC() {
        this.setProgramCounter(0);
    }

}

