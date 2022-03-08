//
// Contributers: Uddam Chea, Frank Lugola, Lonyjera Okal, and Benjamin Hamilton
// Class: cs252
// Assignment: Phase 10
// Date: 12/13/2021
// 
//This is the view of our program. This has the code to make the GUI
//

//Imports
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.ToolTipManager;

public class View extends JPanel {
  
	// View uses Swing framework -to display UI to user
	// Initialize necessary components for the GUI
	private Box row;
	private JTextField speed;
	private JPanel column;
	private JButton load;
	private JButton start;
	private JButton pause;
	private JButton step;
	private JButton runNext;
	private JButton quit;
	private JButton help;
	private JTextArea main_display;
	private JTable table;
	private JTextArea help_display;
	private Box fixedcol;
	private Box fixedrow;
	private Box brow;
	private JTextField acc;
	private JButton accButton;
	private JButton accHex;
	private JTextField pc;
	private JButton pc_set;
	private JButton pcHex;
	private DefaultListModel memoryListModel;
	private JScrollPane JTableScrollPane;

	// getters and setters for class variables.
    public Box getRow() {
        return row;
    }

    public void setRow(Box row) {
        this.row = row;
    }

    public JTextField getSpeed() {
        return speed;
    }

    public void setSpeed(JTextField speed) {
        this.speed = speed;
    }

    public JPanel getColumn() {
        return column;
    }

    public void setColumn(JPanel column) {
        this.column = column;
    }

    public JButton getLoad() {
        return load;
    }

    public void setLoad(JButton load) {
        this.load = load;
    }

    public JButton getStart() {
        return start;
    }

    public void setStart(JButton start) {
        this.start = start;
    }

    public JButton getPause() {
        return pause;
    }

    public void setPause(JButton pause) {
        this.pause = pause;
    }
    public JButton getStep(){
        return step;
    }
    public void setStep(JButton step){

        this.step = step;
    }

    public JButton getRunNext() {
        return runNext;
    }

    public void setRunNext(JButton runNext) {
        this.runNext = runNext;
    }

    public JButton getQuit() {
        return quit;
    }

    public void setQuit(JButton quit) {
        this.quit = quit;
    }

    public JButton getHelp() {
        return help;
    }

    public void setHelp(JButton help) {
        this.help = help;
    }

    public JTextArea getMain_display() {
        return main_display;
    }

    public void setMain_display(JTextArea main_display) {
        this.main_display = main_display;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table){
        this.table = table;
    }

    public JTextArea getHelp_display() {
        return this.help_display;
    }

    public void setHelp_display(JTextArea help_display) {
        this.help_display = help_display;
    }

    public Box getFixedcol() {
        return fixedcol;
    }

    public void setFixedcol(Box fixedcol) {
        this.fixedcol = fixedcol;
    }

    public Box getFixedrow() {
        return fixedrow;
    }

    public void setFixedrow(Box fixedrow) {
        this.fixedrow = fixedrow;
    }

    public Box getBrow() {
        return brow;
    }

    public void setBrow(Box brow) {
        this.brow = brow;
    }

    public JTextField getAcc() {
        return acc;
    }

    public void setAcc(JTextField acc) {
        this.acc = acc;
    }

    public JButton getAccButton() {
        return accButton;
    }

    public void setAccButton(JButton accButton) {
        this.accButton = accButton;
    }

    public JTextField getPc() {
        return pc;
    }

    public void setPc(JTextField pc) {
        this.pc = pc;
    }

    public JButton getAccHex() {
        return accHex;
    }

    public void setAccHex(JButton accHex) {
        this.accHex = accHex;
    }

    public JButton getPc_set() {
        return pc_set;
    }

    public void setPc_set(JButton pc_set) {
        this.pc_set = pc_set;
    }

    public JButton getPcHex() {
        return pcHex;
    }

    public void setPcHex(JButton pcHex) {
        this.pcHex = pcHex;
    }
    
    public JScrollPane getJTableScrollPane() {
        return JTableScrollPane;
    }

    public void setJTableScrollPane(JScrollPane JTableScrollPane) {
        this.JTableScrollPane = JTableScrollPane;
    }
    
	// Constructor
	public View(String title) {
		ToolTipManager.sharedInstance().setInitialDelay(1000);	
		// set invisible border to make it look nice
		this.setBorder(new EmptyBorder(10, 10, 10, 10));
		JFrame frame = new JFrame(title);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		row = Box.createHorizontalBox();
		row.setBorder(new EmptyBorder(10, 10, 10, 10));

		row.add(Box.createHorizontalGlue());
		row.add(new JLabel("Pause Time in mSec: "));

		speed = new JTextField(10);
		speed.setToolTipText("Sets the delay between Instructions. Set to 0 for no delay, 1000 = one second.");
		speed.setMaximumSize(speed.getPreferredSize());

		row.add(speed);
		row.add(Box.createHorizontalGlue());

		// Add a plain row of buttons along the top of the pane
		frame.add(row, BorderLayout.NORTH);

		// Add a plain column of buttons along the right edge
		column = new JPanel();
		column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
		column.setBorder(new TitledBorder(new EtchedBorder(), "Commands"));
	   
		// Add command buttons to the column
		load = new JButton("Load");
        load.setToolTipText("Click to choose a file to load");
		column.add(load);
		column.add(Box.createRigidArea(new Dimension(0, 10)));	

		start = new JButton("Start");
        start.setToolTipText("Click to start the execution of the selected file");
		column.add(start);
		column.add(Box.createRigidArea(new Dimension(0, 10)));

		pause = new JButton("Pause");
        pause.setToolTipText("Pause the execution");
		column.add(pause);
		column.add(Box.createRigidArea(new Dimension(0, 10)));

		step = new JButton("Step");
        step.setToolTipText("Enable manual stepping through the execution of the application");
		column.add(step);

		runNext = new JButton("Run Next");
        runNext.setToolTipText("Executes the next instruction if Step is clicked");
		column.add(runNext);
		// createRigidArea used here and below creates space between buttons
		column.add(Box.createRigidArea(new Dimension(0, 10)));

		quit = new JButton("Quit");
        quit.setToolTipText("Terminates the application");
		quit.addActionListener(e -> {
			System.exit(0);
		 });
		 
		column.add(quit);
		column.add(Box.createRigidArea(new Dimension(0, 10)));

		help = new JButton("Help");
        help.setToolTipText("Display help messages at the bottom display");
		column.add(help);

		// Add column to right of panel
		frame.add(column, BorderLayout.EAST);

		// Create a display in the center of the panel
		main_display = new JTextArea(25,50);
		main_display.setBorder(new EmptyBorder(10, 10, 10, 10));
		main_display.setText("DISPLAY GOES HERE");
        main_display.setToolTipText("Click the Load button to display information of the file");
		main_display.setPreferredSize(new Dimension());

		// Create a display in the bottom below main_display to show help messages
		help_display = new JTextArea(5,50);
		help_display.setEditable(false); // set textArea non-editable
		help_display.setBorder(new EmptyBorder(10, 10, 10, 10));
		help_display.setText("Press Help Button:");
        help_display.setToolTipText("Click the Help button to display the messages");
		JScrollPane helpScrollPane = new JScrollPane(help_display);

		// Create table that will hold information about program being debugged
		DefaultTableModel tableModel = new DefaultTableModel();
		this.table = new JTable(tableModel);
		JTableScrollPane = new JScrollPane(this.table);
		//table

		// Add padding to the display area to make it looks nice
		fixedcol = Box.createVerticalBox();
		fixedcol.add(Box.createVerticalStrut(12));
		fixedcol.add(JTableScrollPane);
		fixedcol.add(Box.createVerticalStrut(12));
		fixedcol.add(helpScrollPane);

		fixedrow = Box.createHorizontalBox();
		fixedrow.add(Box.createHorizontalStrut(12));
		fixedrow.add(fixedcol);
		fixedrow.add(Box.createHorizontalStrut(12));

		// Add this to the center 
		frame.add(fixedrow, BorderLayout.CENTER);

		// Add a plain row of buttons along the bottom of the pane
		brow = Box.createHorizontalBox();
		brow.setBorder(new EmptyBorder(10, 10, 10, 10));
		brow.add(Box.createHorizontalGlue());

		// Add ACC label, textfield and buttons
		brow.add(new JLabel("ACC"));
		acc = new JTextField(10);
        acc.setToolTipText("Change content of ACC");
		acc.setMaximumSize(acc.getPreferredSize());
		brow.add(acc);

		accButton = new JButton("SET ACC");
		brow.add(accButton);

		brow.add(Box.createHorizontalGlue());

		// Add PC label, textfield and buttons
		brow.add(new JLabel("PC"));
		pc = new JTextField(10);
        pc.setToolTipText("Change content of PC");
		pc.setMaximumSize(pc.getPreferredSize());
		brow.add(pc);

		pc_set = new JButton("SET PC");
		brow.add(pc_set);

		brow.add(Box.createHorizontalGlue());

		

		brow.add(Box.createHorizontalGlue()); // stretchy space

		// Add this to the bottom
		frame.add(brow, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
	}
}