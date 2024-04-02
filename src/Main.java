import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class Main{
    static KL keyListener = new KL();
    static ML mouseListener = new ML();
    static Graphics2D graphics;
    static Image imageBuffer; 
    static Graphics imageBufferGraphics;
    static boolean appRunning = false;
    static double appStartTime;
    static Rect rect;
    public static void main(String[] args){
        App app = new App(
                "Title",
                1600, 900,
                keyListener,
                mouseListener
                );

        // Handle to window graphics and backbuffer graphics
        graphics = (Graphics2D)app.getGraphics();
        System.out.println(graphics);
        imageBuffer = app.createImage(app.getWidth(), app.getHeight());
        System.out.println(imageBuffer);
        imageBufferGraphics = imageBuffer.getGraphics();

        double lastFrameTime = 0.0;
        System.out.println("Starting App");


        app.setVisible(true);
        appRunning = true;



        while(appRunning){
            double time = getTime(); 
            double deltaTime = time - lastFrameTime;
            lastFrameTime = time;
            processInput();
            app.update(deltaTime);
            app.draw(imageBufferGraphics);
            graphics.drawImage(imageBuffer, 0, 0, app);
            try{
                Thread.sleep(16);
            }catch(Exception e){
                System.out.println("Uh oh");
            }
        }
        System.out.println("Ending App");
        app.dispose();
    }
    static double getTime(){
        return (System.nanoTime() - appStartTime)*1E-9;
    }
    static void processInput(){
        for(int i = 0; i < keyListener.keyPressed.length; ++i){
            if(keyListener.keyPressed[i]){
                System.out.printf("isKeyPressed[%d] - true\n", i);
            }
        }
    }
}

class App extends JFrame{

    Rect rect;

    App(String title, int w, int h, KL keyListener, ML mouseListener){
        this.setSize(w, h);
        this.setTitle(title);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.addKeyListener(keyListener);
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        
        rect = new Rect(0, 0, 50, 50, Color.WHITE);
    }
    
    
    void update(double dt){
        ML mouse = (ML)getMouseListeners()[0];
        rect.x = mouse.x;
        rect.y = mouse.y;

    }


    
    void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        fillRect(g2, 0, 0, getWidth(), getHeight(), Color.BLUE);
        rect.draw(g2);

    }
    void fillRect(Graphics2D g2, 
            double x, double y, double w, double h, 
            Color color){
        g2.setColor(color);
        g2.fill(new Rectangle2D.Double(x, y, w, h));
    }
}
class Rect{

    double x, y, width, height;
    Color colour;

    Rect(double x, double y, double width, double height, Color colour){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colour = colour;
    }

    void draw(Graphics2D g2){
        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, width, height));
    }

}
class KL implements KeyListener{

    public void keyTyped(KeyEvent e){
    }

    boolean[] keyPressed = new boolean[256];
     
    public void keyReleased(KeyEvent e){
        keyPressed[e.getKeyCode()] = false;
    }

    public void keyPressed(KeyEvent e){
        keyPressed[e.getKeyCode()] = true;
    }

    boolean isKeyPressed(int keyCode){
        return keyPressed[keyCode];
    }
}
class ML extends MouseAdapter implements MouseMotionListener{
    boolean isPressed;
    double x, y;
   
    @Override 
    public void mousePressed(MouseEvent e){
        isPressed = true;
    }

    @Override 
    public void mouseReleased(MouseEvent e){
        isPressed = false;
    }
    
    @Override 
    public void mouseMoved(MouseEvent e){
        x = e.getX();
        y = e.getY();
    }
}
