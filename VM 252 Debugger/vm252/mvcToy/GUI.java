public class GUI{
    public static void main(String[] args) {
        // Assemble all the pieces of the MVC
        Model m = new Model("First", "Last");
        View v = new View("MVC using Separate Files for M, V, C, and main");
        Controller c = new Controller(m, v);
        c.initListeners();
    }
}