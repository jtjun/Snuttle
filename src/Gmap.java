import java.awt.*;
import javax.swing.*;

public class Gmap {
    public static void main(String[] ar){
        Dimension dim = new Dimension(1551, 1561);
        JFrame mapp = new JFrame("Map");
        mapp.setPreferredSize(dim);
        mapp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DrawCline drawCline = new DrawCline(10, 10, 20, 20);
        mapp.add(drawCline);

        mapp.pack();
        mapp.setVisible(true);
    }
}

class DrawCline extends JPanel{
    private int x0, x1;
    private int y0, y1;
    private int r=3;
    DrawCline(int x0i, int y0i, int x1i, int y1i){
        x0 = x0i;
        y0 = y0i;
        x1 = x1i;
        y1 = y1i;
    }
    public void paint(Graphics g){
        super.paint(g);
        g.drawLine(x0,y0,x1,y1);
        g.drawOval(x0-r,y0-r,2*r,2*r);
        g.drawOval(x1-r,y1-r,2*r,2*r);
    }
}
class Drawline extends JPanel{
    private int x0, x1;
    private int y0, y1;
    Drawline(int x0i, int y0i, int x1i, int y1i){
        x0 = x0i;
        y0 = y0i;
        x1 = x1i;
        y1 = y1i;
    }
    public void paint(Graphics g){
        super.paint(g);
        g.drawLine(x0,y0,x1,y1);
    }
}
class DrawStation extends JPanel{
    private int x0, y0;
    private int r=3;
    private String name;
    DrawStation(Station S){
        x0 = S.getX();
        y0 = S.getY();
        name = S.getName();
    }
    public void paint(Graphics g){
        super.paint(g);
        g.drawOval(x0-r,y0-r,2*r,2*r);
        g.drawString(name, x0, y0);
    }
}
