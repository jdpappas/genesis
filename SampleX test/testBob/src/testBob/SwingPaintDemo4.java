/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
 
package testBob;
 
import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics; 
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
 
public class SwingPaintDemo4 {
     
    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
 
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
        SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.add(new MyPanel());
        f.setSize(250,250);
        f.setVisible(true);
    } 
 
}
 
class MyPanel extends JPanel {
 
    RedSquare redSquare = new RedSquare();
    RedSquare redSquare2 = new RedSquare();
    RedSquare rSq;
    
    public MyPanel() {
    	
    	redSquare.setRect(50,50,20,20);
    	redSquare2.setRect(50,50,20,20);
        setBorder(BorderFactory.createLineBorder(Color.black));
        
        addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                moveSquare(e.getPoint());
              
            }
        });
 
        addMouseMotionListener(new MouseAdapter(){
            public void mouseDragged(MouseEvent e){
                moveSquare(e.getPoint());
            }
        });
 
    }
 
    private void moveSquare(Point p){
    	
    	boolean sel1=redSquare.contains(p);
    	boolean sel2=redSquare2.contains(p);
    	
    	if (sel1) {
    		rSq=redSquare;
    	} else if (sel2) {
    		rSq=redSquare2;
    	}
    	
    	if (rSq!=null) { // if the first time there is no object selected.
    		
        // Current square state, stored as final variables 
        // to avoid repeat invocations of the same methods.
        final int CURR_X = rSq.x;
        final int CURR_Y = rSq.y;
        final int CURR_W = rSq.width;
        final int CURR_H = rSq.height;
        final int OFFSET = 1;
 
        if ((CURR_X!=p.x) || (CURR_Y!=p.y)) {
 
            // The square is moving, repaint background 
            // over the old square location. 
            repaint(CURR_X,CURR_Y,CURR_W+OFFSET,CURR_H+OFFSET);
 
            // Update coordinates.
            rSq.x=p.x;
            rSq.y=p.y;
 
            // Repaint the square at the new location.
            repaint(rSq.x, rSq.y, 
            		rSq.width+OFFSET, 
            		rSq.height+OFFSET);
        }
        }
    }
 
     
    public void paintComponent(Graphics g) {
        super.paintComponent(g);       
        g.drawString("This is my custom Panel!",10,20);
 
        redSquare.paintSquare(g);
        redSquare2.paintSquare(g);
    }  
}
 
class RedSquare extends Rectangle {
  

    public void paintSquare(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(x,y,width,height);  
    }
}