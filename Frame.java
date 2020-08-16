import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Math;
import javax.swing.JLabel;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.*;

public class Frame extends JFrame {
  
  private BufferedImage img;
  private BufferedImage img2;
  private JFrame frame;
  private LinkedList<int[]> list;
  private MapSocket socket;
  private JPanel pane;
  private JLabel label;
  private JButton button;
  private boolean toggleDensLoc;  
  
  
  public Frame () throws IOException{
    socket = new MapSocket();
    frame = buildFrame();
    img = ImageIO.read(new File("uwmap.jpg"));
    img2 = ImageIO.read(new File("uwmap.jpg"));
    System.out.println(img2.getHeight(null) + "," + img2.getWidth(null));
    label = new JLabel();
    JPanel p = new JPanel();
    frame.add(p);
    p.add(label);
    button = new JButton("Toggle Density/Location");
    p.add(button);
    toggleDensLoc = true;
    button.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e) {
         toggleDensLoc = !toggleDensLoc;
      }
    });
  }
  
  public void drawPoint2() {
     Graphics graphic = img.getGraphics();
     graphic.drawImage(img2, 0, 0, null);
     int size = list.size();
     for(int i = 0; i < size; i++) {
        int[] array = list.removeFirst();
        int a = (int) (array[1]*0.448);
        int b = (int) (array[2]*0.448);
        
        // set color based on density
        if(array[0] > 40) {
          graphic.setColor(new Color(255, 0, 0));
        } else if (array[0] > 20) {
          graphic.setColor(new Color(255, 99, 71));
        } else {
          graphic.setColor(new Color(240,128,128));
        }
        
        graphic.fillOval(a , b, array[0], array[0]);
        System.out.println(array[0] + "," + array[1] + "," + array[2]);
     }
     label.setIcon(new ImageIcon(img));
  }
  
  public void update() {
        java.util.Timer t = new java.util.Timer();
        MapSocket socket = new MapSocket();
        t.scheduleAtFixedRate(new TimerTask() {
        public void run() {   
            try {
               if(toggleDensLoc) {
                  socket.sendGetPopulationInfo();
               } else {
                  socket.sendGetDensityInfo();
               }
            } catch(Exception e) {
               
            }
            if(toggleDensLoc) {
                  list = socket.getCoordinates();
            } else {
                  list = socket.getDensityCoordinates();
            }
            drawPoint2();
        }
      }, 1000, 100);
    }
  
  public void movePoints(Point[] list) {
     for(int i = 0; i < list.length; i++) {
        int tempX = list[i].getX();
        int tempY = list[i].getY();
        list[i].setX(tempX + 10);
        list[i].setY(tempY + 10);
     }
  }
  
  
  
  private static JFrame buildFrame() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("UW Map");
        frame.setSize(1024, 704);
        frame.setVisible(true);
        return frame;
  }
  
  
}