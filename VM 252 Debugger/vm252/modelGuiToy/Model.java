import javax.swing.event.SwingPropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class Model {
    private SwingPropertyChangeSupport spcSupport = new SwingPropertyChangeSupport(this);
    private String ACC;
    private String PC;
    public Model(String ACC, String PC) {
    this.ACC = ACC;
    this.PC = PC;
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
		spcSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	    spcSupport.removePropertyChangeListener(listener);
    }
    public String getACC() {
    return ACC;
    }
    public void setACC(String ACC) {
        String oldValue = this.ACC;
        this.ACC = ACC;
        spcSupport.firePropertyChange(this.ACC, oldValue, ACC);

    }
    public String getPC() {
    return PC;
    }
    public void setPC(String PC) {
        String oldValue = this.PC;
        this.PC = PC;
        spcSupport.firePropertyChange(this.PC, oldValue, PC);
    }
}