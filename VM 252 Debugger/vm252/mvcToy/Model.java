import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

// See Documentation for PropertyChangeListener and PropertyChangeSupport
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeListener.html
// https://docs.oracle.com/javase/7/docs/api/java/beans/PropertyChangeSupport.html

public class Model {
    private PropertyChangeSupport pcSupport = new PropertyChangeSupport(this);
    private String ACC;
    private String PC;
    public Model(String ACC, String PC) {
    this.ACC = ACC;
    this.PC = PC;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	    pcSupport.removePropertyChangeListener(listener);
    }
    public String getACC() {
    return ACC;
    }
    public void setACC(String ACC) {
        String oldValue = this.ACC;
        this.ACC = ACC;
        pcSupport.firePropertyChange(this.ACC, oldValue, ACC);

    }
    public String getPC() {
    return PC;
    }
    public void setPC(String PC) {
        String oldValue = this.PC;
        this.PC = PC;
        pcSupport.firePropertyChange(this.PC, oldValue, PC);
    }
}