package testBob;
/**
 * JRadioButtonTest.java
 * Copyright (c) 2002 by Dr. Herong Yang, http://www.herongyang.com/
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
public class JRadioButtonTest {
   public static void main(String[] a) {
      JFrame f = new JFrame("My Radio Buttons");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ButtonGroup g = new ButtonGroup();
      MyRadioButton b1 = new MyRadioButton("On");
      g.add(b1);
      f.getContentPane().add(b1,BorderLayout.NORTH);
      MyRadioButton b2 = new MyRadioButton("Off");
      g.add(b2);
      f.getContentPane().add(b2,BorderLayout.SOUTH);
      f.pack();
      f.setVisible(true);
   }
   private static class MyRadioButton extends JRadioButton 
      implements ActionListener, ChangeListener, ItemListener  {
      static int count = 0;
      String text = null;
      public MyRadioButton(String t) {
         super(t);
         text = t;
         addActionListener(this);
         addChangeListener(this);
         addItemListener(this);
      }
      public void actionPerformed(ActionEvent e) {
         count++;
         System.out.println(count+": Action performed - "+text);
      }
      public void stateChanged(ChangeEvent e) {
         count++;
         System.out.println(count+": State changed on - "+text);
      }
      public void itemStateChanged(ItemEvent e) {
         count++;
         System.out.println(count+": Item state changed - "+text);
      }
   }
}