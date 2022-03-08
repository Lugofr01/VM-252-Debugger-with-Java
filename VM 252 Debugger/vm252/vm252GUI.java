//
// Contributers: Uddam Chea, Frank Lugola, Lonyjera Okal, and Benjamin Hamilton
// Class: cs252
// Assignment: Phase 05
// Date: 11/08/2021
// 



//imports
import java.awt.*;
import java.awt.event.*;
import java.util.List as JUList;
import javax.swing.*;
import javax.swing.border.*;

public class vm252GUI extends JPanel {
  private Box row;
  private JTextField speed;
  private JButton soundBtn;
  private JButton LightModeBtn;
  private JPanel column;
  private JButton load;
  private JButton start;
  private JButton pause;
  private JButton runNext;
  private JButton slowmo;
  private JButton quit;
  private JTextField ba;
  private JButton amb;
  private JButton help;
  private JTextArea main_display;
  private JTextArea help_display;
  private Box fixedcol;
  private Box fixedrow;
  private Box brow;
  private JTextField acc;
  private JButton accButton;
  private JTextField pc;
  private JButton pc_reset;
  private JTextField memory;

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

    public JButton getSoundBtn() {
        return soundBtn;
    }

    public void setSoundBtn(JButton soundBtn) {
        this.soundBtn = soundBtn;
    }

    public JButton getLightModeBtn() {
        return LightModeBtn;
    }

    public void setLightModeBtn(JButton LightModeBtn) {
        this.LightModeBtn = LightModeBtn;
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

    public JButton getRunNext() {
        return runNext;
    }

    public void setRunNext(JButton runNext) {
        this.runNext = runNext;
    }

    public JButton getSlowmo() {
        return slowmo;
    }

    public void setSlowmo(JButton slowmo) {
        this.slowmo = slowmo;
    }

    public JButton getQuit() {
        return quit;
    }

    public void setQuit(JButton quit) {
        this.quit = quit;
    }

    public JTextField getBa() {
        return ba;
    }

    public void setBa(JTextField ba) {
        this.ba = ba;
    }

    public JButton getAmb() {
        return amb;
    }

    public void setAmb(JButton amb) {
        this.amb = amb;
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

    public JTextArea getHelp_display() {
        return help_display;
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

    public JButton getPc_reset() {
        return pc_reset;
    }

    public void setPc_reset(JButton pc_reset) {
        this.pc_reset = pc_reset;
    }

    public JTextField getMemory() {
        return memory;
    }

    public void setMemory(JTextField memory) {
        this.memory = memory;
    }
  
  public vm252GUI() {
    // Use a BorderLayout layout manager to arrange various Box components
    this.setLayout(new BorderLayout());

    // Give the entire panel a margin by adding an empty border
    // We could also do this by overriding getInsets()
    this.setBorder(new EmptyBorder(10, 10, 10, 10));

    // Add a plain row of buttons along the top of the pane
    Box row = Box.createHorizontalBox();

    row.add(Box.createHorizontalGlue()); // stretchy space
    row.add(new JLabel("Current Speed: "));

    JTextField speed = new JTextField(10);
    speed.setMaximumSize(speed.getPreferredSize());
    row.add(speed);
    row.add(Box.createHorizontalGlue()); // stretchy space
    
    JButton soundBtn =  new JButton("Sound");
    row.add(soundBtn);
    row.add(Box.createHorizontalGlue()); // stretchy space

    JButton LightModeBtn = new JButton("Light Mode/Dark Mode");
    row.add(LightModeBtn);
    row.add(Box.createHorizontalGlue()); // stretchy space

    this.add(row, BorderLayout.NORTH);

    // Add a plain column of buttons along the right edge
    // Use BoxLayout with a different kind of Swing container
    // Give the column a border: can't do this with the Box class
    JPanel column = new JPanel();

    column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
    column.setBorder(new TitledBorder(new EtchedBorder(), "Commands"));
   

    JButton load = new JButton("Load");
    column.add(load);
    JButton start = new JButton("Start");
    column.add(start);
    JButton pause = new JButton("Pause");
    column.add(pause);
    JButton runNext = new JButton("Run Next");
    column.add(runNext);
    JButton slowmo = new JButton("Slo-Mo");
    column.add(slowmo);
    JButton quit = new JButton("Quit");
    column.add(quit);
    // JButton ba = new JButton("BA");
    // column.add(ba);

    column.add(new JLabel("BA:"));
    JTextField ba = new JTextField(10);
    ba.setMaximumSize(ba.getPreferredSize());
    column.add(ba);

    JButton amb = new JButton("AMB");
    column.add(amb);
    JButton help = new JButton("Help");
    column.add(help);

    this.add(column, BorderLayout.EAST); // Add column to right of panel

    // Create a component to display in the center of the panel
    JTextArea main_display = new JTextArea(25,50);

    main_display.setBorder(new EmptyBorder(10, 10, 10, 10));
    main_display.setText("DISPLAY GOES HERE");
    main_display.setPreferredSize(new Dimension());

    // JScrollPane scrollPane = new JScrollPane(main_display);    
    // scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    JTextArea help_display = new JTextArea(5,50);
    help_display.setEditable(false); // set textArea non-editable
    help_display.setBorder(new EmptyBorder(10, 10, 10, 10));
    help_display.setText("Help Messages Go Here");
    help_display.setPreferredSize(new Dimension());

    // Use Box objects to give the JTextArea an unusual spacing
    // First, create a column with 3 kids. The first and last kids
    // are rigid spaces. The middle kid is the text area
    Box fixedcol = Box.createVerticalBox();
    fixedcol.add(Box.createVerticalStrut(12)); // 12 rigid pixels
    fixedcol.add(main_display); // Component fills in the rest
    fixedcol.add(Box.createVerticalStrut(12)); // 72 rigid pixels
    fixedcol.add(help_display);

    // Now create a row. Give it rigid spaces on the left and right,
    // and put the column from above in the middle.
    Box fixedrow = Box.createHorizontalBox();
    fixedrow.add(Box.createHorizontalStrut(12));
    fixedrow.add(fixedcol);
    fixedrow.add(Box.createHorizontalStrut(12));

    // Now add the JTextArea in the column in the row to the panel
    this.add(fixedrow, BorderLayout.CENTER);

    // Add a plain row of buttons along the top of the pane
    Box brow = Box.createHorizontalBox();
    brow.add(Box.createHorizontalGlue()); // stretchy space

    brow.add(new JLabel("ACC"));
    JTextField acc = new JTextField(10);
    acc.setMaximumSize(acc.getPreferredSize());
    brow.add(acc);
    brow.add(acc);
    brow.add(Box.createHorizontalGlue()); // stretchy space
    JButton accButton = new JButton("SET ACC");
    brow.add(accButton);
    brow.add(Box.createHorizontalGlue()); // stretchy space


    brow.add(new JLabel("PC"));
    JTextField pc = new JTextField(10);
    pc.setMaximumSize(pc.getPreferredSize());
    brow.add(pc);

    JButton pc_reset = new JButton("Reset PC");
    brow.add(pc_reset);
    brow.add(Box.createHorizontalGlue()); // stretchy space

    brow.add(new JLabel("Memory"));
    JTextField memory = new JTextField(10);
    memory.setMaximumSize(memory.getPreferredSize());
    brow.add(memory);
    brow.add(Box.createHorizontalGlue()); // stretchy space

    this.add(brow, BorderLayout.SOUTH);
  }

  public static void main(String[] a) {
    JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });

    f.setContentPane(new vm252GUI());
    f.pack();
    f.setVisible(true);
  }

}