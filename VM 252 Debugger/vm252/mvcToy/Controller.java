import javax.swing.JOptionPane;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

// See Documentation for PropertyChangeListener and PropertyChangeEvent
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeListener.html
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeEvent.html

public class Controller {
 private Model model;
 private View view;
 
 //  what makes this function as the controller is that this code instantiates 
 // (and is thus, aware of) instances of the model and view
 public Controller(Model m, View v) {
  model = m;
  view = v;
  updateView();
 }
 // initialize the view's ACCTextField value to the model's ACC value
 // same for PC
 public void updateView() {
  view.getACCTextfield().setText(model.getACC());
  view.getPCTextfield().setText(model.getPC());
 }
 // since the constructor for the controller instantiates a View and a Model
 // as view and model, view can have listeners addded to the elements of view
 // 
 public void initListeners() {
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
		  updateView();
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
 
}