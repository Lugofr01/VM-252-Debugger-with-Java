import javax.swing.*;
import java.awt.*;

public class JListExample {
	public static void main(String[] args) {
		JFrame f = new JFrame("JList Example");
		f.getContentPane().setLayout(new FlowLayout());
//		HighlightPainter yellowPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
		
		int visibleRowCount = 10;
		String prototypeCellValue = "01234567890123456789";
		
		DefaultListModel myListModel = new DefaultListModel();
		JList<String> myJList = new JList<>(myListModel);
		myJList.setVisibleRowCount(visibleRowCount);
		myJList.setPrototypeCellValue(prototypeCellValue);
		
		JScrollPane scrollPane = new JScrollPane(myJList);
		f.getContentPane().add(scrollPane,BorderLayout.CENTER);
		f.pack();
		f.setVisible(true);
		
		String myList[] = {"F10D", "379A", "AAAA", "AAAA", "F1F2", "F3F4", "3031", "3233", "6162", "6364", "6566", "6768", "4142", "4344", "4546", "4748", "AAAB", "ACAD 2020", "2020"};
		myListModel.removeAllElements(); // not needed here, but useful to know how to reset
		for(String item:myList) {
			myListModel.addElement(item);
		}
		
		int highlightIndices[] = {4,5};
		myJList.setSelectedIndices(highlightIndices);
		myJList.setSelectionBackground(Color.yellow);
		// need a Listener to monitor for changes in GUI, but for now pretend we selected indices as above
		int indicesToMark[] = myJList.getSelectedIndices();
		
		String oldText;
		String newText;
		Boolean isIn;
		DefaultListModel<String> newListModel = (DefaultListModel<String>)myJList.getModel();
		for(int i=0; i<newListModel.getSize(); ++i) {
			isIn = false;
		
			for(int item:highlightIndices) {
				if (item == i) {
					isIn = true;
					break;
				}
			}
			if (isIn) {
				oldText = newListModel.getElementAt(i);
				newText = oldText + "*";
				newListModel.setElementAt(newText, i);
				System.out.println(isIn);
			}
		}
	}
}