package TrivialSketcher;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class TrivialSketcher{

     
    //  Creates  GUI.  
    private static void createInferface() {

        // Interface setup
        JFrame theFrame = new JFrame("Drawing Example");

        // set what should happen when window is closed
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add panel that supports drawing as described
        final DrawingRect drawInPanel = new DrawingRect();
        theFrame.add(drawInPanel, BorderLayout.CENTER);

        // add a 'clear' button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawInPanel.clear();
                drawInPanel.repaint();
            }
        });
        buttonPanel.add(clearButton);
        theFrame.add(buttonPanel, BorderLayout.SOUTH);

        // set size
        theFrame.setSize(600, 400);

        // make visible
        theFrame.setVisible(true);
    }

     
     // Main method.
     
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	createInferface();
            }
        });
    }

   

 
    private static class RectangleDimensions {
        public final int left;
        public final int top;
        public final int width;
        public final int height;
        public RectangleDimensions(int l, int t, int w, int h) {
            left = l;
            top = t;
            width = w;
            height = h;
        }
    }

    // drawing panel class
    private static class DrawingRect extends JPanel {

        
        public DrawingRect() {

            
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    onMousePressed(e);
                }
                public void mouseReleased(MouseEvent e) {
                    onMouseReleased(e);
                }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    onMouseDragged(e);
                }
            });

          
            clear();
        }

       
        public void clear() {
            rectangles = new ArrayList<RectangleDimensions>();
            select = null;
            drag = null;
        }

       
        public void paintComponent(Graphics g) {

            // paint background
            super.paintComponent(g); 

            // paint completed rectangles
            int nextColor = 0;
            for (RectangleDimensions r : rectangles) {
                g.setColor(colors[nextColor]);
                g.fillRect(r.left, r.top, r.width, r.height);
                nextColor = (nextColor + 1) % colors.length;
            }

            
            if ((select != null) && (drag != null)) {
                g.setColor(Color.black);
                RectangleDimensions outline = 
                    makeRectangle(select, drag);
                g.drawRect(outline.left, outline.top, 
                        outline.width, outline.height);
            }
        }

         
         // Respond to mouse pressed event (by saving the clicked-on
         // point so we can use it later to construct a rectangle).
        
         
        private void onMousePressed(MouseEvent e) {
            select = e.getPoint();
        }

         
         // Respond to mouse released event (by constructing a rectangle
         // using the point saved on a mouse pressed event).
         
         
        private void onMouseReleased(MouseEvent e) {
            if (select != null) {
            	RectangleDimensions r = makeRectangle(select, e.getPoint());
                rectangles.add(r);
                select = null;
                drag = null;
            }
            repaint();
        }

        
         // Respond to "mouse dragged" event (by showing an outline of the
         // rectangle that would be added if the mouse were released).
         
         
        private void onMouseDragged(MouseEvent e) {
            drag = e.getPoint();
            repaint();
        }

       
        private RectangleDimensions makeRectangle(Point p1, Point p2) {
            return new RectangleDimensions(
                    Math.min(p1.x, p2.x),
                    Math.min(p1.y, p2.y),
                    Math.abs(p1.x - p2.x),
                    Math.abs(p1.y - p2.y));
        }


        private static Color[] colors = new Color[] {
            Color.yellow, Color.red, Color.blue, Color.green 
        };

        private ArrayList<RectangleDimensions> rectangles;
        private Point select;
        private Point drag;
    }
}