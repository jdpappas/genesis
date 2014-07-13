package controlStd;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.SwingUtilities;

public class ControlStd {

	public ControlStd() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(); 
            }
        });
    }
	}

class RedSquare{
	 
    private int xPos;
    private int yPos;
    private int width;
    private int height;
 
    public RedSquare (int newX, int newY, int newW, int newH) {
    	setX(newX); setY(newY); setW(newW); setH(newH);
    }
    
    public void setX(int xPos){ 
        this.xPos = xPos;
    }
 
    public int getX(){
        return xPos;
    }
 
    public void setY(int yPos){
        this.yPos = yPos;
    }
 
    public int getY(){
        return yPos;
    }
    
    public void setW(int width){
        this.width = width;
    }
    
    public int getW(){
        return width;
    } 
 
    public void setH(int height){
        this.height = height;
    }
    
    public int getH(){
        return height;
    }
 
    public void paintSquare(Graphics g){
        g.setColor(Color.RED);
        g.fillRect(xPos,yPos,width,height);
        g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,width,height);  
    }
}