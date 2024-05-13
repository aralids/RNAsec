/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rnass;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author PC
 */
public class Circle extends JComponent {
        private int x;
        private int y;
        private int width;

    public Circle(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(this.x, this.y, this.width, this.width);
        System.out.println("PAINTED COMPONENT");
    }
}
