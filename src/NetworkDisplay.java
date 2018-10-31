import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
- make output distribution thing for ANY neuron, if it's a SELECTED neuron.
 */
public class NetworkDisplay extends JFrame implements KeyListener, MouseListener{

    Network n;
    Drawer d;
    int neuronRadius = 20;
    int fontSize = 12;
    int buttonrows = 2;
    int buttonHeight = 40;

    public NetworkDisplay() {
        super ("Net");

        n = new Network();

        setSize (1000, 800);

        setResizable(false);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String [] position = Data.getData ("lastLocation", "0, 0").split (", ");
        setLocation(Integer.parseInt (position [0]), Integer.parseInt (position [1]));

        addKeyListener(this);
        addMouseListener (this);

        setLayout (null);
        setVisible (true);


        setContentPane (new JPanel (){
            public void paintComponent (Graphics g) {
                if (d != null){
                    NetworkDisplay.this.g = g;
                    d.draw();
                }
            }
        });

        d = new Drawer (n, this);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                d.updateNetwork = true;
            }
        });

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                Data.putData ("lastLocation", getX() + ", " + getY());
            }
        });

        requestFocus();

        Thread thread = new Thread (){
            long time = System.currentTimeMillis();
            @Override
            public void run (){
                while (true) {
                    process ();
                    repaint();
                    try {
                        Thread.sleep((int)(Math.max (-(System.currentTimeMillis () - time - 16), 0)));
                    } catch (Exception e) {
                        System.out.println("um");
                    }
                    //System.out.println(System.currentTimeMillis() - time);
                    time = System.currentTimeMillis();
                }
            }
        };
        thread.start ();

        d.windowWidth = getContentPane().getSize().width;
        d.windowHeight = getContentPane().getSize().height;
    }

    boolean stop = true;

    private ArrayList<JButton> buttons = new ArrayList<>();
    void addButton (String name, Action e){
        JButton newButton = new JButton (name);
        newButton.addActionListener ((ActionEvent f)->
            e.thing()
        );
        buttons.add (newButton);
        newButton.setText("[" + ((char)(buttons.size()-1 + 97)) + "] " + buttons.get(buttons.size()-1).getText());
        newButton.addActionListener ((ActionEvent f)-> NetworkDisplay.this.requestFocus());//put the focus back onto the jframe
        add (newButton);
    }
    void setButtonPositions (){//needs to prioritize putting buttons near beginning for non-whole number fitting into rows
        int buttonWidth = d.windowWidth / (buttons.size() + (buttons.size()%2 == 1?1:0)) * buttonrows;
        for (int y = 0; y < buttonrows; y ++)
            for (int x = 0; x < buttons.size() / buttonrows + (buttons.size()%buttonrows != 0?1:0); x ++) {
                buttons.get(x + y * (buttons.size() / buttonrows)).setBounds(
                        buttonWidth * x, d.windowHeight - buttonHeight * (y + 1),
                        buttonWidth - 1, buttonHeight);
            }
    }
    private Graphics g;

    private float thicknessMultiplier = 7;
    void setStroke (double value){
        ((Graphics2D) g).setStroke(new BasicStroke((float) value * thicknessMultiplier));
    }
    void setFont (float font){
        g.setFont(g.getFont().deriveFont(font));
    }
    void setColor (Color c){
        g.setColor (c);
    }
    void drawRect (int x, int y, int width, int height){
        g.drawRect (x, y, width, height);
    }
    void drawOval (int x, int y, int diameter1, int diameter2){
        g.drawOval (x, y, diameter1, diameter2);
    }
    void drawLine (int x1, int y1, int x2, int y2){
        g.drawLine (x1, y1, x2, y2);
    }
    void drawString (String text, int x, int y, String position){
        int width = g.getFontMetrics().stringWidth(text);

        ((Graphics2D) g).setStroke(new BasicStroke(1));
        g.setColor (d.halfwhite);
        if (position.equals ("middle"))
            g.fillRect (x - width/2, y - fontSize/2+1, width, fontSize);
        if (position.equals ("topright"))
            g.fillRect (x - width, y + 1, width, fontSize);
        if (position.equals ("topmiddle"))
            g.fillRect (x - width/2, y + 1, width, fontSize);
        if (position.equals ("topleft"))
            g.fillRect (x, y + 1, width, fontSize);
        if (position.equals ("bottomright"))
            g.fillRect (x - width, y - fontSize + 1, width, fontSize);
        if (position.equals ("bottomleft"))
            g.fillRect (x, y - fontSize + 1, width, fontSize);
        if (position.equals ("bottommiddle"))
            g.fillRect (x - width/2, y - fontSize + 1, width, fontSize);
        if (position.equals ("middleright"))
            g.fillRect (x - width, y - fontSize/2, width, fontSize);

        g.setColor (Color.BLACK);
        if (position.equals ("middle"))
            g.drawString (text, x - width/2, y + fontSize/2);
        if (position.equals ("topright"))
            g.drawString (text, x - width, y + fontSize);
        if (position.equals ("topmiddle"))
            g.drawString (text, x - width/2, y + fontSize);
        if (position.equals ("topleft"))
            g.drawString (text, x, y + fontSize);
        if (position.equals ("bottomright"))
            g.drawString (text, x - width, y);
        if (position.equals ("bottomleft"))
            g.drawString (text, x, y);
        if (position.equals ("bottommiddle"))
            g.drawString (text, x - width/2, y);
        if (position.equals ("middleright"))
            g.drawString (text, x - width, y + fontSize/2);
    }
    void drawDouble (double text, int x, int y, String position){
        drawString ((Math.round(text * 1000.0)/1000.0)+"", x, y, position);
    }
    void drawDouble (double text, int decimals, int x, int y, String position){
        drawString ((Math.round(text * Math.pow (10, decimals))/Math.pow (10, decimals))+"", x, y, position);
    }

    private void process (){
        d.updateNetwork();
    }

    String keyboard="";

    //this method will only fire for typeable keys (characters)
    public void keyTyped(KeyEvent e){
        //System.out.print ("@" + e.getKeyChar () + "@");
    }
    public void keyPressed(KeyEvent e){
        //System.out.println (e.getKeyChar());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                break;
            default:
                if (!keyboard.contains (e.getKeyChar ()+"") && !e.isActionKey ())
                    keyboard = keyboard + (e.getKeyChar ());
                break;
        }
    }
    //maybe for debugging
    public void keyReleased(KeyEvent e){
        int location = keyboard.indexOf(e.getKeyChar());
        if (location != -1){
            keyboard = keyboard.substring(0, location) + keyboard.substring(location + 1);
        }
        //System.out.println (e.getKeyChar());
        if (e.getKeyChar() - 97 >= 0 && e.getKeyChar() - 97 < buttons.size())
            buttons.get (e.getKeyChar() - 97).doClick();
    }

    public void mouseExited (MouseEvent event){//when mouse exits window range (doesn't need focus)

    }

    public void mouseReleased (MouseEvent event){//called when the mouse button is released
        d.userClicked(event.getX() - 8, event.getY() - 31);
    }
    public void mousePressed (MouseEvent event){//called when the mouse button is pressed, but doesn't continue firing
        //System.out.println (event.getX() + ", " + event.getY());

        //put in click+dragging neurons (with neuronRadius and positions
        //when clicked on, make it the "focused" neuron, showing stats and extra buttons
    }
    public void mouseEntered (MouseEvent event){//when mouse enters window range (doesn't need focus)

    }
    public void mouseClicked (MouseEvent event){//does NOT get called after a click and drag
    }

    public static void main (String [] args){
        new NetworkDisplay();
    }
}