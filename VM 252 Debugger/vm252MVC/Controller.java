//
// Contributers: Uddam Chea, Frank Lugola, Lonyjera Okal, and Benjamin Hamilton
// Class: cs252
// Assignment: Phase 10
// Date: 12/13/2021
// 
// This is the controller of our program. This connects the view and the controller. 
//

//Imports
import javax.swing.JOptionPane;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JFileChooser;
import java.io.*;
import vm252utilities.VM252Utilities;
import java.awt.*;
import javax.swing.*;
import java.lang.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import javax.swing.border.*;

// See Documentation for PropertyChangeListener and PropertyChangeEvent
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeListener.html
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeEvent.html

public class Controller {
  private Model model;
  private View view;
  private JFileChooser fileChooser;
  private JTable table;
 
  // The controller is aware of instances of the model and view
  public Controller(Model m, View v) {
    model = m;
    view = v;
    //Create a file chooser
    fileChooser = new JFileChooser();
    initView();
  }

  // initialize the view's ACCTextField value to the model's ACC value
  // same for PC
  public void initView() {
    view.getAcc().setText(String.valueOf(model.getAccumaltor()));
    view.getPc().setText(String.valueOf(model.getProgramCounter()));
  }

  // updateView is called by a PropertyChangeListener.
  // When certain changes are made in the model, the view needs to be updated.
  // The formal parameter pcEvt holds the property name of the
  // event and the old value and newvalue of the property that changed.
  // Updates to view components are made according to the pcEvt proprty name.
  public void updateView(PropertyChangeEvent pcEvt) {
    view.getAcc().setText(String.valueOf(model.getAccumaltor()));
    view.getPc().setText(String.valueOf(model.getProgramCounter()));

    if(pcEvt.getPropertyName() == "memory"){
      model.parseProgram(model.getMemory());
      updateTable();
    }

    if(pcEvt.getPropertyName() == "PC"){
      updateTable();
    }
    if(pcEvt.getPropertyName() == "breakpointlist"){
      updateTable();
    }
  }
  
  // since the constructor for the controller is aware of a View and a Model
  // as view and model, the controller can add listeners to the elements of view.
  // Those listeners could directly be methods in model. However, here the methods are
  // a controller method that, in turn, calls a model method to make the
  // appropriate update to model. Vice-versa for model events that affect the GUI.
  public void initListeners() {
	// GUI affects Model Listeners
	// If events happen in the Gui to certain components, call these controller methods
	// most of these controller methods then call a method in the model to modify the model fields. 
	  view.getAcc().addActionListener(e -> saveACC());
	  view.getAccButton().addActionListener(e -> saveACC());
	  view.getPc().addActionListener(e -> savePC());
	  view.getPc_set().addActionListener(e -> savePC());
	  view.getLoad().addActionListener(e -> loadObjFile());
	  view.getStart().addActionListener(e -> runObjFile());
	  view.getHelp().addActionListener(e -> showHelp());
	  view.getSpeed().addActionListener(e -> savePauseTime());
	  view.getPause().addActionListener(e -> pauseRunProgram());
	  view.getStep().addActionListener(e -> stepRunProgram());
	  view.getRunNext().addActionListener(e->runNext());   
		  // We don't set the view's Jtable model checkbox listener here
		  // because the model is overwritten when the object code
		  // is loaded and each time the table is updated.
		  // So the listener (commented out below) is implemented
		  // in the updateTable method. We include this comment here simply 
		  // because the listener is added apart from this usual place in the code.
	  // view.getTable().getModel().addTableModelListener(new myTableModelListener());
		
	  // Model affects View Listeners
	  // set up publish subscribe so that any changes to the model publish an event
	  // that the model subscribes to. Effectively, if model changes, view is upodated
	  // this registers a controller metho d as a listener (propertyChangeListener) with the model instances
	  model.addPropertyChangeListener(new MyPropChangeListener());
  }

	private class MyPropChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent pcEvt) {
			updateView(pcEvt);
		}
	}

	//override TableChanged to only take action on certain columns
	private class myTableModelListener implements TableModelListener {
		@Override 
		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
			int column = e.getColumn();
			TableModel tmodel = (TableModel)e.getSource();
			Object data = tmodel.getValueAt(row, column);
			switch (column) {
				case 0:
					if (data instanceof Boolean) {
						  
						  //the new value is data 
						  //we need to set memory breakpoint(array) @ correct index
						  //model.getMemoryBreakpoint()[index] = data
						  //the correct index is model.getStatmentToFirstProgramCounter().get(row)
						  Boolean[] mb = model.getMemoryBreakpoint();
						  int byteArrayIndex  = model.getStatmentToFirstProgramCounter().get(row);
						  mb[byteArrayIndex] = (Boolean)data;
						  List<Boolean> mbl = model.getBreakpointList();
						  mbl.set(row, (Boolean)data);
						  mb[model.getStatmentToFirstProgramCounter().get(row)] = (Boolean)data;
						  model.setMemoryBreakpoint(mb);
						  model.setBreakpointList(mbl);
						}
						break;
				case 2:
					try{
					  byte[] bytes = parseHexBinary((String)data);
					  for(int i = 0; i < bytes.length; ++i){

							//System.out.println("changed bytes" + bytes[i] + "data" + data);
							byte[] mem = model.getMemory();
							mem[model.getStatmentToFirstProgramCounter().get(row) + i] = bytes[i]; 
							model.setMemory(mem);

					  }
					}
					catch(Exception ex){
					  JOptionPane.showMessageDialog(null, "Invalid value ", "Info", JOptionPane.INFORMATION_MESSAGE);

					}
					break;
			} //end switch
		} // end tableChanged               
	} //myTableModelListener

	//read the view value of ACC and then use it to update the model value of ACC
	private void saveACC() {
		model.setAccumaltor((myParseShort(view.getAcc().getText())));
		}
		private void savePC() {
      if(myParseInt(view.getPc().getText()) <= model.getMemory().length){
        model.setProgramCounter(myParseInt(view.getPc().getText()));
      }
      else{
        JOptionPane.showMessageDialog(null, "Program Counter is invalid", "Info", JOptionPane.INFORMATION_MESSAGE);
      }
		}

		private void showModelValues() {
		JOptionPane.showMessageDialog(null, "Model: " + model.getAccumaltor() + " " + model.getProgramCounter(), "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	// Catch invalid input for JText boxes. In all cases here, zero is an acceptable default value.
	public static Integer myParseInt(String str) {
		try {
		  return Integer.parseInt(str);
		} 
		catch (NumberFormatException e) {
		  JOptionPane.showMessageDialog(null, "Invalid value, " + str + " -must be an integer.", "Info", JOptionPane.INFORMATION_MESSAGE);
		  return 0;
		}
	}

	public static Short myParseShort(String str) {
		try {
		  return Short.parseShort(str);
		} 
		catch (NumberFormatException e) {
		  JOptionPane.showMessageDialog(null, "Invalid value, " + str + " -must be an integer.", "Info", JOptionPane.INFORMATION_MESSAGE);
		  return (short)0;
		}
	}

	private void savePauseTime() {
		model.setPauseTime(myParseInt(view.getSpeed().getText()));
	}

	private void showHelp(){
		view.getHelp_display().setText(model.getHelpmsg());
	}

	private void loadObjFile(){
		//In response to a load button click:
		Boolean success = false;
		int numtries = 0;
		do{
		int returnVal = this.fileChooser.showOpenDialog(view.getLoad());
		//JOptionPane.showMessageDialog(null, "Opening: " +this.fileChooser.showOpenDialog(view.getLoad())+ JFileChooser.APPROVE_OPTION , "Info", JOptionPane.INFORMATION_MESSAGE);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		  File file = this.fileChooser.getSelectedFile();
			  //This is where an application would open the file.
		  
		  try{
			success = model.loadObjFile(file.getPath());
			//JOptionPane.showMessageDialog(null, "Opening: " + success , "Info", JOptionPane.INFORMATION_MESSAGE);

		  }
		  catch(Exception e){
			success = false;
		  }
		  if(!success){
			JOptionPane.showMessageDialog(null, file.getName() +  " Could not be parsed, please select a new file.", "Info", JOptionPane.INFORMATION_MESSAGE);
		  }
		} 
		else {
		  JOptionPane.showMessageDialog(null, "Open command cancelled by user." , "Info", JOptionPane.INFORMATION_MESSAGE);
		}
		numtries += 1;
		}while(!success && numtries<3);
		if(numtries == 3){
		  JOptionPane.showMessageDialog(null, "The current state of the debuger is invaild,\n until a valid 'vm252obj' file is loaded.\n Press load or quit." , "Info", JOptionPane.INFORMATION_MESSAGE);
		}
		updateTable();
	}

	private Boolean updateTable(){
		if(model.getBreakpointList() == null 
		|| model.getLabelList() == null
		|| model.getMemoryList() == null
		|| model.getOpcodeStrList() == null
		|| model.getOperandStrList() == null
		|| model.getOpcodeTypeList() == null){
		  return false;
		}
		if(model.getProgramCounter() < 0 || model.getProgramCounterToStatment().get(model.getProgramCounter()) >= model.getMemoryList().size()){
		  return false;
		}


		DefaultTableModel tableModel=new DefaultTableModel(){
		  // See oracle documentation for info on
		  //  subclass to DefaultTableModel and override getColumnClass.
		  // E.g., see 
		  //https://docs.oracle.com/javase/7/docs/api/javax/swing/table/TableModel.html#getColumnClass(int)
		  // One might expect a setColumnClass, but overriding 
		  // getColumnClass seems to be the standard (only?) way to specify
		  // the type of the data in the column (if other than String is needed.)
		  // JTable renders Boolean as checkbox by default. Hence, we want to specify as Boolean.)
		  // This is very specific to our case where we know the first column is the
		  // the Boolean column of interest.
		  public Class<?> getColumnClass(int column)
		  {
			if (column == 0) {
			  return Boolean.class;
			}
			else {
			  return String.class;
			}
		  }
		};//end defaultTableModel table model


		//
		//populate Table.
		//headers for the columns
		tableModel.addColumn("Breakpoint");
		tableModel.addColumn("Label");
		tableModel.addColumn("Memory");
		tableModel.addColumn("Opcode");
		tableModel.addColumn("Operand");
		tableModel.addColumn("Type");
		//column data
		List<Boolean> breakpointList = model.getBreakpointList();
		List<String> labelList = model.getLabelList();
		List<String> memList = model.getMemoryList();
		List<String> opcodeList = model.getOpcodeStrList();
		List<String> operandList = model.getOperandStrList();
		List<String> opcodeType = model.getOpcodeTypeList();
		//now add the data (from the columns above) row by row
		for(int i = 0; i < memList.size(); ++i) {
		  tableModel.addRow(new Object[] {breakpointList.get(i), labelList.get(i), memList.get(i), opcodeList.get(i), operandList.get(i), opcodeType.get(i)});
		}
		// make a new JTable object and 
		//	* override the isCellEditable method so that only certain columns are editable,
		// 	* override the prepareRenderer component so that row colors alternate and the 
		//	  current instruction is highlighted and outlined.
		this.table = new JTable(tableModel){
		  public boolean isCellEditable(int row, int column){
			if(column == 0 || column == 2){
			  return true;
			}
			else{
			  return false;
			}
		  }
		  // custom row colors
		  public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
			Component c = super.prepareRenderer(renderer, row, column);
			JComponent jc = (JComponent)c;
			//  add custom rendering here
			if (!isRowSelected(row)){
			  if(row % 2 == 0){
				c.setBackground(Color.white);
			  }
			  else{
				c.setBackground(new Color(204,204,255));
			  }
			}
			
			// Add a border to the selected row
			if(model.getProgramCounterToStatment().containsKey(model.getProgramCounter())){
			  if (model.getProgramCounterToStatment().get(model.getProgramCounter()) == row){
				jc.setBorder(new MatteBorder(1, 0, 1, 0, Color.black));
				c.setBackground(Color.yellow);
			  }
			}
			else{
			  int badvalue = model.getProgramCounter();
			  model.resetPC();
			  JOptionPane.showMessageDialog(null, "PC = "+ badvalue + "is invalid.\n Reseting PC to zero.", "Info", JOptionPane.INFORMATION_MESSAGE);
			  updateTable();
			}
			return c;
		  };

		}; // end new JTable

		view.setTable(table); // set the table in view to this new table
		view.getJTableScrollPane().setViewportView(table);// let the scroll pane know that there is a new object 
		//Jtable checkbox listener
		view.getTable().getModel().addTableModelListener(new myTableModelListener());

		return true;
		// end populate table
	}


	private void runObjFile(){
		try{
			model.runProgramThreaded();               
		}
		catch(Exception e){
		  JOptionPane.showMessageDialog(null, "Error running program", "Info", JOptionPane.INFORMATION_MESSAGE);
					
		}
	}

	//pause
	private void pauseRunProgram(){
		if(!model.getPause()){
		model.setPause(true);
		view.getPause().setText("Resume");
		}
		else{
		  model.setPause(false);
		  view.getPause().setText("Pause");
		  try{
			model.runProgramThreaded();               
		  }
		  catch(Exception e){
			JOptionPane.showMessageDialog(null, "Unable to resume program"  + e , "Info", JOptionPane.INFORMATION_MESSAGE);
					  
		  }
		}
	}

	private void stepRunProgram(){
		if(!model.getStepState()){
		model.setStepState(true);
		view.getStep().setText("Stop Stepping");
		}
		else{
		  model.setStepState(false); 
		  view.getStep().setText("Step");
		}
	}
	// This makes a new thread each time runNext is clicked. it works but might not be efficient. 
	private void runNext(){
		if(model.getStepState()){
		  try{
			model.runProgramThreaded();               
		  }
		  catch(Exception e){
			JOptionPane.showMessageDialog(null, "Unable to run next instruction." + e , "Info", JOptionPane.INFORMATION_MESSAGE);
					  
		  }
		}
		else{
		  JOptionPane.showMessageDialog(null, "To step through the function, please click the Step button." , "Info", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// parse hex to binary helper methods from 
	// com.sun.xml.internal.bind.DatatypeConverterImpl.java
	public byte[] parseHexBinary(String s) {
		final int len = s.length();

		// "111" is not a valid hex encoding.
		if( len%2 != 0 )
		  throw new IllegalArgumentException("hexBinary needs to be even-length: "+s);

		byte[] out = new byte[len/2];

		for( int i=0; i<len; i+=2 ) {
		  int h = hexToBin(s.charAt(i  ));
		  int l = hexToBin(s.charAt(i+1));
		  if( h==-1 || l==-1 )
			  throw new IllegalArgumentException("contains illegal character for hexBinary: "+s);

		  out[i/2] = (byte)(h*16+l);
		}

		return out;
	}

	private static int hexToBin( char ch ) {
		if( '0'<=ch && ch<='9' )    return ch-'0';
		if( 'A'<=ch && ch<='F' )    return ch-'A'+10;
		if( 'a'<=ch && ch<='f' )    return ch-'a'+10;
		return -1;
	}

	private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

	public String printHexBinary(byte[] data) {
		StringBuilder r = new StringBuilder(data.length*2);
		for ( byte b : data) {
		  r.append(hexCode[(b >> 4) & 0xF]);
		  r.append(hexCode[(b & 0xF)]);
		}
		return r.toString();
	}

}