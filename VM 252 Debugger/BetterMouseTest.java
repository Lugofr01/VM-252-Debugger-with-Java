//
// This example program was adapted from an example program provided with
//
//     Horstmann & Cornell
//     _Core Java, Volume 1 - Fundamentals_
//     Prentice Hall
//
// Edited by: Lonyjera Okal, Ratanak Uddam Chea, Benjamin Hamilton, Frank Lugola
// CLass: CS252
// Date : 10/30/2021
//
// This example program is edited by Group3: Lonyjera Okal, Ratanak Uddam Chea, Benjamin Hamilton, Frank Lugola on 10/29/2021
// This version fixes 3 issues in the program
    // Issue (a): the cursor shape is incorrect when placing and removing squares
    // Issue (b): the order in which an overlap square is removed is incorrect
    // Issue (c): double-clicking on an empty space creates then immediately removes it. This is incorrect
//

// These methods were changed to achieve the goal
    // public Rectangle2D findSquareContainingPoint(Point2D clickPoint)
    // public void placeAdditionalSquare(Point2D clickPoint)
    // public void removeExistingSquare(Rectangle2D existingSquare)
    // public void removeExistingSquare(Rectangle2D existingSquare)
    // public void mousePressed(MouseEvent event)
    // public void mouseClicked(MouseEvent event)


// These methods were added to achieve the goal
    // public void checkCurrentCursor(MouseEvent event)
//

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
 
 
public class BetterMouseTest
{
 
    public static void main(String [] commandLineArguments)
    {
 
        EventQueue.invokeLater(
            () ->
                {
 
                    ProgramFrame frame = new ProgramFrame();
 
                    frame.setTitle("Better Mouse Test");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
 
                    }
            );
 
        }
 
    }
 
class ProgramFrame extends JFrame
{
 
    public ProgramFrame()
    {
 
        add(new MouseComponent());
        pack();
 
        }
 
    }
 
 
class MouseComponent extends JComponent
{
 
    private static final int OUR_DEFAULT_WIDTH = 300;
    private static final int OUR_DEFAULT_HEIGHT = 300;
 
    private static final int OUR_SQUARE_SIDE_LENGTH = 30;
 
    private ArrayList< Rectangle2D > mySquares;
    private Rectangle2D myCurrentSquare;
 
    private ArrayList< Rectangle2D > squares()
    {
 
        return mySquares;
 
        }
 
    private Rectangle2D currentSquare()
    {
 
        return myCurrentSquare;
 
        }
 
    private void setSquares(ArrayList< Rectangle2D > other)
    {
 
        mySquares = other;
 
        }
 
    private void setCurrentSquare(Rectangle2D other)
    {
 
        myCurrentSquare = other;
 
        }
 
    public MouseComponent()
    {
 
        setSquares(new ArrayList< Rectangle2D >());
        setCurrentSquare(null);
 
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseMotionHandler());
 
        }
 
    @Override
    public Dimension getPreferredSize()
    {
 
        return new Dimension(OUR_DEFAULT_WIDTH, OUR_DEFAULT_HEIGHT);
 
        }
 
 
    @Override
    public void paintComponent(Graphics canvas)
    {
 
        for (Rectangle2D squareOnCanvas : squares())
            ((Graphics2D) canvas).draw(squareOnCanvas);
 
        }
 
    public Rectangle2D findSquareContainingPoint(Point2D clickPoint)
    {
 
        int squareNumber;

        //
        // This method is edited to fix Issue (b).
        // Goes through the list backwards so that 
        // the first square found is the square that is on top.
        //

        if (squares().size() == 0)
            return null;

        for (squareNumber = squares().size() - 1;
                squareNumber >= 0
                    && ! squares().get(squareNumber).contains(clickPoint);
                -- squareNumber)
                // inv:
                //     No square in squares()[0 ... squareNumber - 1] spatially contains clickpoint
                //
            ; // do nothing
 
        return squareNumber >= 0 ? squares().get(squareNumber) : null;
 
        }
 
    public void placeAdditionalSquare(Point2D clickPoint)
    {   
        //
        // the cursor should change to a crosshair when it is on a square
        // this includes when the square is created
        // so we setCursor to a crosshair here in this method to do that
        //

        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        setCurrentSquare(
            new Rectangle2D.Double(
                clickPoint.getX() - OUR_SQUARE_SIDE_LENGTH / 2,
                clickPoint.getY() - OUR_SQUARE_SIDE_LENGTH / 2,
                OUR_SQUARE_SIDE_LENGTH,
                OUR_SQUARE_SIDE_LENGTH
                )
            );
 
        squares().add(currentSquare());
 
        repaint();
 
        System.out.println(
            "New square placed at (" + clickPoint.getX() +", " + clickPoint.getY() + ")"
            );
 
        }
 
    public void removeExistingSquare(Rectangle2D existingSquare)
    {

        //
        // the cursor should change back to the default arrow when it is on a square
        // and that square is removed
        // so we setCursor to default here in this method to do that
        //

        setCursor(Cursor.getDefaultCursor());

        if (existingSquare != null) {
            
            if (existingSquare == currentSquare())
                setCurrentSquare(null);
                squares().remove(existingSquare);

 
            repaint();
 
            System.out.println("Existing square removed");
 
            }
            
        }

    //
    // This method is added to keep track and change the cursor if needed
    //
    // Formal parameter: MouseEvent event
    //
    // If there is a square and the cursor is over that square, change the cursor to a crosshair
    // If the cursor is over an empty space (no square), the cursor should be the default arrow
    //

    public void checkCurrentCursor(MouseEvent event){

        setCurrentSquare(findSquareContainingPoint(event.getPoint()));

        if (currentSquare() != null)
            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

        else
            setCursor(Cursor.getDefaultCursor());

    }
 
 
    private class MouseHandler extends MouseAdapter
    {
        //
        // Declaring an additional int variable unActionedClicks
        // the clicks are processed one at a time, without knowing 
        // if there is another click coming after. 
        // If there is no action to take for the current click, 
        // the variable unActionedClicks is set to one. 
        // If there is action to take for the current click, 
        // the variable unActionedClicks is set to zero.
        // 

        int myPresses = 0;
        int myClicks = 0;
	    int unActionedClicks = 0;
 
        @Override
        public void mousePressed(MouseEvent event)
        {
 
            ++ myPresses;
            System.out.println(
                "Mouse press " + myPresses + " at (" + event.getX() + ", " + event.getY() + ")"
                );
 
            setCurrentSquare(findSquareContainingPoint(event.getPoint()));

            //This should be handeled in mouseClicked not in mousePressed.
           // if (currentSquare() == null)
                //placeAdditionalSquare(event.getPoint());

            
        }
 
        @Override
        public void mouseClicked(MouseEvent event)
        {
            ++ myClicks;
            setCurrentSquare(findSquareContainingPoint(event.getPoint()));
            // This handles for example, a click that is not on a square 
            // and there are no unaccounted for actions from previous clicks.
            if (unActionedClicks == 0 && currentSquare() == null) {
                // System.out.println("0, null");
                placeAdditionalSquare(event.getPoint());
                unActionedClicks = 0;
            }
            // This handles for example, a click that is on a square 
            // and there are no unaccounted for actions from previous clicks.
            else if (unActionedClicks == 0 && currentSquare() != null) {
                // System.out.println("0, NOTnull");
                unActionedClicks = 1;
            }
             // and there is 1 unaccounted for actions from previous clicks.
             // This is to handle the second click of a double-click. 
             // There is another condition that getClickCount is greater than 
             // one to handle the case of single-clicking repeatedly on the same square.
             // For example, one square is created with one click. 
             // Then, after a pause, the mouse is clicked once on the first square.
             // In this case, we don't want the first square removed. 
             // This logic also prevents a similar case with multiple squares.
            else if (unActionedClicks == 1 && currentSquare() != null && event.getClickCount() >= 2) {
                // System.out.println("1, NOTnull");
                removeExistingSquare(currentSquare());
                unActionedClicks = 0;
            }
            	// this handles the case where we have clicked twice, for example,
                // to make a square then move out of the square and click once to make
                // a new one.
            else if (unActionedClicks == 1 && currentSquare() == null){
                placeAdditionalSquare(event.getPoint());
                unActionedClicks = 0;
            }
 
            if (event.getClickCount() < 2)
                System.out.println(
                    "Mouse click " + myClicks + " at (" + event.getX() + ", " + event.getY() + ")" + " Detected " + event.getClickCount() + " clicks."
                    );
            else
                System.out.println(
                    "Mouse double-click " + myClicks + " at (" + event.getX() + ", " + event.getY() + ")" + " Detected " + event.getClickCount() + " clicks."

                    );

            checkCurrentCursor(event);
            }
 
        }
 
 
    private class MouseMotionHandler implements MouseMotionListener
    {
 
        int myMoves = 0;
        int myDrags = 0;
 
        @Override
        public void mouseMoved(MouseEvent event)
        {
 
            ++ myMoves;
            System.out.println(
                "Mouse move " + myMoves + " to (" + event.getX() + ", " + event.getY() + ")"
                );
 
            if (findSquareContainingPoint(event.getPoint()) == null)
                setCursor(Cursor.getDefaultCursor());
            else
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
 
            }
 
        @Override
        public void mouseDragged(MouseEvent event)
        {
 
            ++ myDrags;
            System.out.println(
                "Mouse drag " + myDrags + " to (" + event.getX() + ", " + event.getY() + ")"
                );
 
            if (currentSquare() != null) {
 
                currentSquare().setFrame(
                    event.getX() - OUR_SQUARE_SIDE_LENGTH / 2,
                    event.getY() - OUR_SQUARE_SIDE_LENGTH / 2,
                    OUR_SQUARE_SIDE_LENGTH,
                    OUR_SQUARE_SIDE_LENGTH
                    );
 
                repaint();
                
                }
 
            }
 
        }
 
    }