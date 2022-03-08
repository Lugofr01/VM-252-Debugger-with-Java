import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import vm252GUI.java;

public class vm252Model {
    short accumulator;
    int programCounter;
    String objectFileName;
    final int MAX_LENGTH = 8192;
    byte [] memory = new byte[MAX_LENGTH];

    public vm252Model(){
    }
//Methods that implement debugger actions as listed in vm252dbg help such as:
// [aa,amb,amd,amdx,ap,ba,bl,cb,h,mb,md,mi,n,ob,od,oi,q,r,s,t,z]   
    //Alter contents of accumulator to IV
    public int alterACC(int ACC){
      return ACC;
    }
    //Alter contents of program counter to MA
    public int alterProgCounter(int MA){
      return MA;
    }
    //Set a breakpoint at address MA
    public int breakAddress(int MA){
      return MA;
    }
    //Set a breakpoint at source-line L
    public int breakLine(int MA){
      return MA;
    }
    //Clear all breakpoints
    public void clearBreak(){

    }
    //Print this help message
    public void helpMsg(){

    }
    //Display all of machine memory as bytes in hex
    public void memoryHex(){
      
    }
    //Display all of machine memory as 2-byte data in hex
    public void memoryShort(){
      
    }
    //Display all of machine memory as instructions, data, and labels
    public void memoryInstruction(){

    }
    //Execute next machine instruction
    public void NextInstruction(){

    }
    //Display the portion of machine memory holding object code as bytes in hex
    public void displayHex(){

    }
    //Display the portion of machine memory holding object code as 2-byte data in hex
    public void displayShort(){

    }
    //Display the portion of machine memory holding object code as instructions, data, and labels
    public void displayInstruction(){

    }
    //Quit
    public void quit(){

    }
    //Run machine until error occurs or stop instruction is executed
    public void run(){

    }

    //Display machine state will be handeled by the gui

    //Toggle instruction tracing
    public void toggle(){

    }
    //Reinitialize program counter to zero
    public void setPCZero(){

    }
//Extra commands for Debugger
    // read and validate a object file.
    public String loadFile(String file){
      return file;
    }
    // Pause execution of instruction.
    public void pauseExec(){
        
    }
    // Change speed of instruction execution.
    public void slowmo(){

    }
    // Execute a single instruction
    public void runOneInstruction(){

    }
    //This might just mean publish an event to which the gui subscribes
    public void updateGui(){

    }
    public short getAccumaltor(){

        String x = acc.getText();
        return this.accumulator;
    }
    public void setAccumaltor(short accumulator){
        this.accumulator = accumulator;
    }
    public int getProgramCounter(){
        return programCounter;
    }
    public void setProgramCounter(int programCounter){
        this.programCounter = programCounter;
    }
    public String getObjectFileName(){
        return objectFileName;
    }
    public void setObjectFileName(String objectFileName){
        this.objectFileName = objectFileName;
    }
    public byte[] getMemory(){
        return memory;
    }
    public void setMemory(byte[] memory){
        this.memory = memory;
    }
    public static void main(String[] hello){

    }
} 