 // Benjamin Hamilton
// CS252 Object-Oriented Programing with Java (Zaring)
// Spring 2021
// Project Phase 2
 
package vm252architecturespecifications;
import java.util.Scanner;
 
public class runOneInstruction
{
 
    //
    // Public Class Constants
    //
 
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
 
    public static void main(String []args)throws Exception{

    }
 
    // Public Class Method byte [] encodedInstructionBytes(int opcode, int operand)
    //
    // Purpose:
    //     Creates a byte array containing 8- or 16-bit binary encoding of an instruction
    //      constructed from the opcode and operand
    //
    // Formals:
    //     opcode (in) - an integer representing a VM252 numeric opcode
    //  operand (in) - an integer representing an operand
    // Pre-conditions:
    //     none
    //
    // Post-conditions:
    //     none
    //
    // Returns:
    //     a byte array (instructionBytes) that is comprised of opcode and/or operand
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //
 
        public static byte [] encodedInstructionBytes(int opcode, int operand)
        {
 
            byte[] instructionBytes;
            int opcodeBits;
            int secondByteBits;
            byte instructionByte0;
            byte instructionByte1;
 
            //opcode = opcode & 63; // ensure no garbage in the upper bits of this int
 
            switch(opcode) {
                case LOAD_OPCODE:
                case STORE_OPCODE:
                case ADD_OPCODE:
                case SUBTRACT_OPCODE:
                case JUMP_OPCODE:
                case JUMP_ON_ZERO_OPCODE:
                case JUMP_ON_POSITIVE_OPCODE:
                    // where is the sign bit in the operand int
                    // check this
                    opcodeBits = opcode << 5;
                    secondByteBits = (operand >>8) & 31;
                    instructionByte0 = (byte)(opcodeBits | secondByteBits);
                    instructionByte1 = (byte)(operand & 255);
                    instructionBytes = new byte[] {instructionByte0, instructionByte1};
                    break;
                case SET_OPCODE:
                    opcodeBits = opcode << 4;
                    secondByteBits = (operand >>8) & 15;
                    instructionByte0 = (byte)(opcodeBits | secondByteBits);
                    instructionByte1 = (byte)(operand & 255);
                    instructionBytes = new byte[] {instructionByte0, instructionByte1};
                    break;
                case INPUT_OPCODE:
                case OUTPUT_OPCODE:
                case NO_OP_OPCODE:
                case STOP_OPCODE:
                    opcodeBits = (opcode << 2) & 255;
                    instructionByte0 = (byte) opcodeBits;
                    instructionBytes = new byte[] {instructionByte0};
                    break;
 
            default:
                    instructionBytes = null;
            }
 
            return instructionBytes;
 }
 
 
    // Public Class Method  int [] decodedInstructionComponents(byte [] instructionBytes)
    //
    // Purpose:
    //     Constructs a one- or two-element array of int’s with element 0 containing the
    //     numeric opcode from the encoded instruction and with element 1 (if present)
    //     containing the numeric operand.
    //
    // Formals:
    //     instructionBytes (in) - an array of one or two byte’s, the sort of array that could have
    //     been produced by encodedInstruction.
    //
    // Pre-conditions:
    //     none
    //
    // Post-conditions:
    //     none
    //
    // Returns:
    //     return components a int array with the opcode and/or the operand
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //
 
 
        public static int [] decodedInstructionComponents(byte [] instructionBytes)
        {
 
            int opcodeBits;
            int operandBits;
            int upperThreeBits;
            int upperSixBits;
            int[] components = null;
           
            if ( instructionBytes != null && instructionBytes.length <= 2) {
        upperThreeBits = (instructionBytes[0] >> 5) & 7;
                if (upperThreeBits <= JUMP_ON_POSITIVE_OPCODE && instructionBytes.length  == 2) {
                    opcodeBits = upperThreeBits;
                    // ((xxxaaaaa << 8) & 0b00000000 00000000 00011111 00000000
                    // 256 + 512 + 1024 + 2048 + 4096 = 7936
                    operandBits = ( (instructionBytes[0] << 8) & 7936) | (instructionBytes[1] & 255);
                    components = new int[] {opcodeBits, operandBits};
                }
                else { // assert upperThreeBits = 7
                    if ( ((instructionBytes[0] >> 4) & 1) == 0  && instructionBytes.length == 2) {
                   
                        opcodeBits = SET_OPCODE;
                        // ((xxxxcccc << 8) & 0b00000000 00000000 00001111 00000000
                        // 256 + 512 + 1024 + 2048  = 3840
                        operandBits = ( (instructionBytes[0] << 8) & 3840) | (instructionBytes[1] & 255);
                        components = new int[] {opcodeBits, operandBits};
                    }
 
                    else { // assert upper four bits = 0b1111
                    if (instructionBytes.length  == 1) {
                        upperSixBits = (instructionBytes[0] >> 2) & 63;
                        switch (upperSixBits) {
                            case INPUT_OPCODE:
                            case OUTPUT_OPCODE:
                            case NO_OP_OPCODE:
                            case STOP_OPCODE:
                                opcodeBits = upperSixBits;
                                components = new int[] {opcodeBits};
                                break;
                            default:
                                components = null;
        }
                    }
        else {
                    components = null;
                }
                       
                    }
                }  
            }
            else{
                components = null;
            }
            return components;  
        }
       
    // public static int numInstructionBytes(byte instructionByte)
    //
    // Purpose:
    //     Creates a integer that tells us how many bytes getInstruction needs
    //     to get based on the opcode.
    //
    // Formals:
    //     instructionByte (in) - a byte containing an opcode.
    // Pre-conditions:
    //     none
    //
    // Post-conditions:
    //     none
    //
    // Returns:
    //     an int (numBytes) that tells how far we need to jump based on the opcode.
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //
    public static int numInstructionBytes(byte instructionByte){
        int upperThreeBits;
        int upperSixBits;
        int numBytes = 0;
        upperThreeBits = (instructionByte >> 5) & 7;
            if (upperThreeBits <= JUMP_ON_POSITIVE_OPCODE) {
                numBytes = 2;
            }
            else { // assert upperThreeBits = 7
                if ( ((instructionByte >> 4) & 1) == 0) {
                    numBytes = 2;
                }
 
                else { // assert upper four bits = 0b1111
                //if (instructionBytes.length  == 1) {
                    upperSixBits = (instructionByte >> 2) & 63;
                    switch (upperSixBits) {
                        case INPUT_OPCODE:
                        case OUTPUT_OPCODE:
                        case NO_OP_OPCODE:
                        case STOP_OPCODE:
                            numBytes = 1;
                            break;
                        default:
                            numBytes = 0;
                 }
 
 
 
                } // end else
            } // end else
        return numBytes;
    }  
 
    //public static byte [] getInstruction(byte[] instructionBytes, int arrayIndex, int numBytes)
    //
    // Purpose:
    //     Create a byte array that has the correct bytes in it based on numbytes and array index.
    //    
    //
    // Formals:
    //     instructionBytes (in) - a byte array of program.
    //     arrayIndex (in) - an int that tells which byte we are looking at.
    //     numBytes (in) - an int that ells how many bytes to take.
    // Pre-conditions:
    //     none
    //
    // Post-conditions:
    //     none
    //
    // Returns:
    //     a byte array that is 0, 1, or 2 long.
    //
    // Worst-case asymptotic runtime:
    //     O(1)
    //  
    public static byte [] getInstruction(byte[] instructionBytes, int arrayIndex, int numBytes){
        arrayIndex = arrayIndex % MAX_LENGTH;
        byte[] instBytes;
        switch (numBytes) {
            case 0:
                instBytes = null;
                break;
            case 1:
                instBytes = new byte[] {instructionBytes[arrayIndex]};
                break;
            case 2:
                instBytes = new byte[] { instructionBytes[arrayIndex], instructionBytes[(arrayIndex + 1) % MAX_LENGTH]};
                break;
            default:
                instBytes = null;
        }
        return instBytes;
    }
 
    public static boolean runOneInstructions(byte [] instruction)  { 
        int numBytes = instruction.length();
        short ACC = this.getAccumaltor();
        byte [] memory = this.getMemory();
        boolean returnVal = true;
        int offset = this.getProgramCounter();
        int[] components;
        int operand = 0;
        int opcode = 0;
//**********************BEGIN runOneInstructions()
                    components = decodedInstructionComponents(instruction);
                    opcode = components[0];
                    if (numBytes == 2) {
                        operand = components[1];
                    }
                    // must set offset inside switch statement that
                    // executes one instruction
                    // Begin execute one instruction
                    switch (opcode) {
                        case LOAD_OPCODE:
                            ACC = (short) (memory[operand] << 8 | memory[(operand+1)%MAX_LENGTH]);                            
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case STORE_OPCODE:
                            memory[operand] = (byte) ( ACC >> 8 );
                            memory[(operand+1)%MAX_LENGTH] = (byte) ( ACC & 0xFF);
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case ADD_OPCODE:
                            ACC += memory[operand] << 8 | memory[(operand+1)%MAX_LENGTH];
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case SUBTRACT_OPCODE:
                            ACC -= memory[operand] << 8 | memory[(operand+1)%MAX_LENGTH];
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
                            Scanner input = new Scanner(System.in);
                            System.out.print("Input: ");
                            while (input.hasNext() && !input.hasNextInt()) {
                                System.out.println("Invalid Input, try again");
                                input.nextLine();
                                System.out.printf("Input:");  
                            }
                            // only way to get out of while loop
                            // is for hasNextInt to be true or
                            // for hasNext to be false
                            // hasNext is false if Ctrl-Z or -D
                            if (input.hasNextInt()) {
                                ACC = (short)input.nextInt();
                                }
                                else { // assert received Ctrl-D or Ctrl-Z
                                    opcode = STOP_OPCODE;
                                    System.out.println("Received Ctrl-D or Ctrl-Z");
                                }
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case OUTPUT_OPCODE:
                            System.out.println("OutPut: " + ACC);
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case NO_OP_OPCODE:
                            offset = (offset + numBytes) % MAX_LENGTH;
                            break;
                        case STOP_OPCODE:
                            offset = (offset + numBytes) % MAX_LENGTH;        
                            break;
                        default:
                            System.out.println("bad instruction default case");
                            offset = (offset + numBytes) % MAX_LENGTH;
                            returnVal = false;
                    } // end switch
                  
                  // End execute one instruction
    this.setProgramCounter(offset);
    this.setAccumaltor(ACC);
	return returnVal;
    }
 
    //public static void runProgram(byte [] program)
    //
    // Purpose:
    //     Simulates execution of the VM252 program whose binary encoding is found in program.
    //    
    //
    // Formals:
    //     program (in) - a byte array of opcodes and operands.
    // Pre-conditions:
    //     none
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
    
    public static void runProgram(byte [] program) {
        // what size is program?
        // what to do if program.length > MAX_LENGTH bytes
        // what to do if program.length < MAX_LENGTH bytes
        byte [] memory = new byte[MAX_LENGTH];
        if (program.length <= MAX_LENGTH){
            for (int i = 0; i < program.length; ++i){
                memory[i] = program[i];
 
            }
            for(int i = program.length; i < MAX_LENGTH; ++i) {
                    memory[i] = NO_OP_OPCODE;
            }
 
            int opcode;
            //ACC is the working memory location for the program to store values
            short ACC = 0;
               // final Scanner in = new Scanner(System.in);
            //final PrintStream out = System.out;
 
            // offset is an integer pointing to the current byte in memory[]
            // that holds the next program instruction
            int offset = 0;
            byte [] instruction;
            int[] components;
            int operand = 0;
		boolean successFlag = false;
           
            do {
                int numBytes = numInstructionBytes(memory[offset]);
                if (numBytes == 1 || numBytes == 2) {
                instruction = getInstruction(memory, offset, numBytes);
                if (instruction != null) {
                    components = decodedInstructionComponents(instruction);
                    opcode = components[0];
			        successFlag = runOneInstructions(instruction);
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
            }while (opcode != STOP_OPCODE);
        } // end if program.length <= MAX_LENGTH
    else {
        System.out.println("Program is greater than max length.");
    }
    } // end runProgram

    
}
 
 

