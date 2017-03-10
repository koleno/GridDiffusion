package xyz.koleno.GridDiffusion.gui;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * About Window
 * @author Dusan Koleno
 */
public class AboutWindow extends JFrame {
    
    private JPanel panel;
    private MainWindow window;
    
    public AboutWindow(MainWindow window) {
	super("About");

	this.window = window;
	
	this.setResizable(false);
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);	
	this.setSize(260, 310);
	this.init();

        this.setLocationRelativeTo(null); // center the window                
	this.setVisible(true);
    }
    
    private void init() {
	this.panel = new JPanel();
	this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));	
	this.panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
	StringBuilder about = new StringBuilder();
	about.append("<html><center><h1>Grid Diffusion</h1>");
	about.append("Developed by<br>Dusan Koleno <br><br>");
        about.append("Released under<br>GNU GPLv2<br><br>");
        
        
	about.append("Developed in Spring 2014 for independent study course <br> Models and Algorithms for Social Media <br><br>");
	about.append("Instructor <br> Prof. Evgeny Dantsin<br>");
	about.append("Roosevelt University");
	about.append("</center></html>");
	
	JLabel aboutLabel = new JLabel(about.toString());
	this.panel.add(aboutLabel);
	
	this.add(panel);
    }
    
}
