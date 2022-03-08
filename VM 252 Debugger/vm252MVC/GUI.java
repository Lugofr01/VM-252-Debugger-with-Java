//
// Contributers: Uddam Chea, Frank Lugola, Lonyjera Okal, and Benjamin Hamilton
// Class: cs252
// Assignment: 10
// Date: 12/13/2021
// 
// This class has the main that
// connects and starts the Model, Controller, and View.
//
public class GUI{
    public static void main(String[] args) {
        // Assemble all the pieces of the MVC
        Model m = new Model((short)0, 0);
        View v = new View("vm252Debugger");
        Controller c = new Controller(m, v);
        c.initListeners();
    }
}