/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 *
 * @author Amish Naik
 */
public class DoodlePanel extends JPanel implements MouseListener, MouseMotionListener{
    GeneralPath path=new GeneralPath();

    LinkedList<GeneralPath> storedPaths = new LinkedList<GeneralPath>();
    
    
    public DoodlePanel() {
        setBackground(Color.WHITE);

        addMouseListener(this);
        addMouseMotionListener(this);
    }

    
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;
        g.setStroke(new BasicStroke(3));        
        for (GeneralPath storedPath : storedPaths ) {
            g.draw(storedPath);
        }
        
        g.draw(path);        
    }
    
    public void setDoodle(LinkedList<GeneralPath> doodle){
        storedPaths = doodle;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    @Override
    public void mousePressed(MouseEvent me) {
        path=new GeneralPath();
        path.moveTo(me.getX(), me.getY());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        storedPaths.add(path);
        path=new GeneralPath();
        repaint();
        
    }

    // Implementation of MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent me) {
        path.lineTo(me.getX(), me.getY());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent me) {        
    }
}
