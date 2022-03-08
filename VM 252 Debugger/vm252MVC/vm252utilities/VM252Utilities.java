//
// This is the vm252utilities from phase 2 of the project nd we use some of the methods
// in here to help run our file in the model (Model.java) ex. readObjectCodeFromObjectFile. 
// We also put the methods nessecary to run a file that we made.
// the methods we added do not need any of the specific values directly from the model,
// so we put them in this file to declutter Model.java.
//
package vm252utilities;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class VM252Utilities
{
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

   

	
	
	
	//TODO: Are there any other constant HashMaps to define?
	
	//TODO: Any other methods to convert? For example, 
	// byteToString
	// shortToString
	// intToString
	// arrays of those types to String
	// instructionToString ??This may already be done somewhere??

    //
    // Public Class Methods
    //

        


        


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

    public static String byteArrayToString(byte[] bytes) {
        String hx = "";
      for(int i = 0; i<bytes.length; ++i){
        hx = hx + String.format("%02x",bytes[i]);
        //if(i != 0 && (i+1)%4 == 0){
        //  if((i+1)%16 == 0){
        //    hx = hx + "\n";
        //  }
        //  else {
        //    hx = hx + " ";
        //  }
        
        //}  
      }
    return hx;
    }

    public static String intArrayToString(int[] bytes) {
        String hx = "";
      for(int i = 0; i<bytes.length; ++i){
        hx = hx + String.format("%02x",bytes[i]);
        //if(i != 0 && (i+1)%4 == 0){
        //  if((i+1)%16 == 0){
        //    hx = hx + "\n";
        //  }
        //  else {
        //    hx = hx + " ";
        //  }
        
        //}  
      }
    return hx;
    }

    public static Boolean playSound(String filename)
	{
	   try 
	   {
		File soundfile = new File(filename).getAbsoluteFile( );
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundfile);
		Clip soundClip = AudioSystem.getClip( );
		soundClip.open(audioStream);
        soundClip.start( );
		return true;
	   }
	   catch(Exception e)
	   {
		 System.out.println("Error: Can't open sound file.");
		 e.printStackTrace( );
         return false;
	   }
	}
}
