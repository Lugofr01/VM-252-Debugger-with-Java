import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
public class View {
 // View uses Swing framework -to display UI to user
 private JFrame frame;
 private JLabel ACCLabel;
 private JLabel PCLabel;
 private JTextField ACCTextfield;
 private JTextField PCTextfield;
 private JButton ACCResetButton;
 private JButton PCResetButton;
 private JButton ACCChangeButton;
 private JButton PCChangeButton;
 private JButton showModelBtn;
 private JButton showViewBtn;
 private JButton bye;
 public View(String title) {
  frame = new JFrame(title);
  frame.getContentPane().setLayout(new BorderLayout());
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setSize(700, 240);
  frame.setLocationRelativeTo(null);
  frame.setVisible(true);
  // Create UI elements
  ACCLabel = new JLabel("ACC :");
  PCLabel = new JLabel("PC :");
  ACCTextfield = new JTextField();
  PCTextfield = new JTextField();
  ACCResetButton = new JButton("Reset ACC");
  PCResetButton = new JButton("Reset PC");
  ACCChangeButton = new JButton("Change ACC");
  PCChangeButton = new JButton("Change PC");
  showModelBtn = new JButton("Show Model Values");
  showViewBtn = new JButton("Show View Values");
  bye = new JButton("Bye");
  // Add UI element to frame
  GroupLayout layout = new GroupLayout(frame.getContentPane());
  layout.setAutoCreateGaps(true);
  layout.setAutoCreateContainerGaps(true);
  // all this create parallel and sequential is not intuitive to code by hand, use grid instead
  //columns
  layout.setHorizontalGroup(layout.createSequentialGroup()
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ACCLabel)
    .addComponent(PCLabel).addComponent(showModelBtn).addComponent(showViewBtn).addComponent(bye))
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ACCTextfield)
    .addComponent(PCTextfield))
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ACCResetButton)
    .addComponent(PCResetButton))
	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ACCChangeButton)
    .addComponent(PCChangeButton)));
	//rows
  layout.setVerticalGroup(layout.createSequentialGroup()
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(ACCLabel)
    .addComponent(ACCTextfield).addComponent(ACCResetButton).addComponent(ACCChangeButton))
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(PCLabel)
    .addComponent(PCTextfield).addComponent(PCResetButton).addComponent(PCChangeButton))
	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(showModelBtn))
    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(showViewBtn))
	.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bye)));
	
  layout.linkSize(SwingConstants.HORIZONTAL, ACCResetButton, PCResetButton);
  layout.linkSize(SwingConstants.HORIZONTAL, showModelBtn, showViewBtn);
  frame.getContentPane().setLayout(layout);
 }
 
 // getters and setters for objects in the View
 public JFrame getFrame() {
  return frame;
 }
 public void setFrame(JFrame frame) {
  this.frame = frame;
 }
 public JLabel getACCLabel() {
  return ACCLabel;
 }
 public void setACCLabel(JLabel ACCLabel) {
  this.ACCLabel = ACCLabel;
 }
 public JLabel getPCLabel() {
  return PCLabel;
 }
 public void setPCLabel(JLabel PCLabel) {
  this.PCLabel = PCLabel;
 }
 public JTextField getACCTextfield() {
  return ACCTextfield;
 }
 public void setACCTextfield(JTextField ACCTextfield) {
  this.ACCTextfield = ACCTextfield;
 }
 public JTextField getPCTextfield() {
  return PCTextfield;
 }
 public void setPCTextfield(JTextField PCTextfield) {
  this.PCTextfield = PCTextfield;
 }
 public JButton getACCResetButton() {
  return ACCResetButton;
 }
 public void setACCResetButton(JButton ACCResetButton) {
  this.ACCResetButton = ACCResetButton;
 }
 public JButton getPCResetButton() {
  return PCResetButton;
 }
 public void setPCResetButton(JButton PCResetButton) {
  this.PCResetButton = PCResetButton;
 }
 public JButton getACCChangeButton() {
  return ACCChangeButton;
 }
 public void setACCChangeButton(JButton ACCChangeButton) {
  this.ACCChangeButton = ACCChangeButton;
 }
 public JButton getPCChangeButton() {
  return PCChangeButton;
 }
 public void setPCChangeButton(JButton PCChangeButton) {
  this.PCChangeButton = PCChangeButton;
 }
 public JButton getShowModelBtn() {
  return showModelBtn;
 }
 public void setShowModelBtn(JButton showModelBtn) {
  this.showModelBtn = showModelBtn;
 }
 public JButton getShowViewBtn() {
  return showViewBtn;
 }
 public void setShowViewBtn(JButton showViewBtn) {
  this.showViewBtn = showViewBtn;
 }
 public JButton getBye() {
  return bye;
 }
 public void setBye(JButton bye) {
  this.bye = bye;
 }
 ///////////////////////////////////////////////////////////////////
 // Controller Functionality
 private Model model;
 private View view;
 
 //  what makes this function as the controller is that this code instantiates 
 // (and is thus, aware of) instances of the model and view
 public void Controller(Model m, View v) {
  model = m;
  view = v;
  initView();
 }
 // initialize the view's ACCTextField value to the model's ACC value
 // same for PC
 public void initView() {
  view.getACCTextfield().setText(model.getACC());
  view.getPCTextfield().setText(model.getPC());
 }
 // since the constructor for the controller instantiates a View and a Model
 // as view and model, view can have listeners addded to the elements of view
 // 
 public void initListeners(Model model, View view) {
	 // GUI affects Model
	 // if events happen in the Gui to certain components, call these controller methods
	 // most of these controller methods tehn call a method in the model to modify the model 
    view.getACCResetButton().addActionListener(e -> resetACC());
    view.getPCResetButton().addActionListener(e -> resetPC());
    view.getACCTextfield().addActionListener(e -> saveACC());
    view.getACCChangeButton().addActionListener(e -> saveACC());
    view.getPCChangeButton().addActionListener(e -> savePC());
    view.getShowModelBtn().addActionListener(e -> showModelValues());
    view.getShowViewBtn().addActionListener(e -> showViewValues());
    view.getBye().addActionListener(e -> sayBye());

  // Model affects View
  // set up publish subscribe so that any changes to the model publish an event
  // that the model subscribes to. Effectively, if model changes, view is upodated
  // this registers a controller method as a listener (propertyChangeListener) with the model instances

  model.addPropertyChangeListener(new MyPropChangeListener());
 }

private class MyPropChangeListener implements PropertyChangeListener {
      @Override
      public void propertyChange(PropertyChangeEvent pcEvt) {
		  initView();
		//String ACC = "ACC";
        // if (ACC.equals(pcEvt.getPropertyName())) {
        //    view.getACCTextfield().setText((String)pcEvt.getNewValue());
        // }
      }
 }

 //read the view value of ACC and then use it to update the model value of ACC
 private void saveACC() {
  model.setACC(view.getACCTextfield().getText());
  JOptionPane.showMessageDialog(null, "ACC changed : " + model.getACC(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void savePC() {
  model.setPC(view.getPCTextfield().getText());
  JOptionPane.showMessageDialog(null, "PC changed : " + model.getPC(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void resetACC() {
  model.setACC("Zero");
  JOptionPane.showMessageDialog(null, "ACC reset : " + model.getACC(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void resetPC() {
  model.setPC("Zero");
  JOptionPane.showMessageDialog(null, "PC reset : " + model.getPC(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 
 private void showModelValues() {
  JOptionPane.showMessageDialog(null, "Model: " + model.getACC() + " " + model.getPC(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void showViewValues() {
  JOptionPane.showMessageDialog(null, "There is no need to do this; we can see the View values in the GUI.\nView " + view.getACCTextfield().getText() + " " + view.getPCTextfield().getText(), "Info", JOptionPane.INFORMATION_MESSAGE);
 }
 private void sayBye() {
  System.exit(0);
 }

 public static void main(String[] args) {
  // Assemble all the pieces of the MVC
  Model m = new Model("First", "Last");
  View v = new View("MVC using Separate Files for M with V and C combined");
  v.Controller(m, v);
  v.initListeners(m, v);
 }
}