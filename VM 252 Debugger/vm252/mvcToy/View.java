import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
}