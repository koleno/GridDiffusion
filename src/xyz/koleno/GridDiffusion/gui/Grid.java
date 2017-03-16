package xyz.koleno.GridDiffusion.gui;

import xyz.koleno.GridDiffusion.app.Behaviors;
import xyz.koleno.GridDiffusion.app.Network;
import xyz.koleno.GridDiffusion.app.Node;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Grid rendering class
 * @author Dusan Koleno
 */
public class Grid extends JPanel {
    
    private Network network;
    private GridMouseListener mouseListener;
    
    private int rows;
    private int cols;
    private int rectWidth;
    private int rectHeight;
    private int marginTop;
    private int marginLeft;   
    
    public Grid() {
	super();
	
	this.rows = 10;
	this.cols = 11;
	this.rectHeight = this.rectWidth = 10;
	this.marginLeft = this.marginTop = 0;
		
	this.setBackground(Color.WHITE);
	this.network = new Network();
	this.mouseListener = null;
    }

    @Override
    public void paint(Graphics g) {
	super.paint(g);
	this.drawGrid(g);
	this.resetMouseListener();
    }
    
    private void drawGrid(Graphics g) {
        this.calculateDimensions();    
        
	if(this.network.getSize() != (cols + 1) * (rows + 1)) {
	    this.initNetwork();
	}
	
	// color of the grid
	g.setColor(Color.LIGHT_GRAY);		

	// squares
	for(int column = 0; column < cols; column++) {
	    for(int row = 0; row < rows; row++) {
		g.drawRect((column*rectWidth) + marginLeft, (row*rectHeight) + marginTop, rectWidth, rectHeight);
	    }
	}
	
	// vertices
	int n = 0;
	for(int row = 0; row < (rows + 1); row++) {
	    for(int column = 0; column < (cols + 1); column++) {	
		g.setColor(Behaviors.getInstance().getColor(this.network.getNode(n).getBehavior()));
		g.fillOval((column*rectWidth) + marginLeft - 5, (row*rectHeight) + marginTop - 5, 10, 10);
		/* String number = "" + this.network.getNode(n).getLastChange();
		g.drawString(number, (column*50) + marginLeft - number.length() + 4, (row*50) + marginTop); */
		n++;
	    }
	}

    }
    
    private void initNetwork() {
	
	int size = (cols + 1) * (rows + 1);
	this.network = new Network();
	
	// creating nodes according to current grid
	for(int i = 0; i < size; i++) {
	    this.network.addNode(new Node(i));
	}
	
	// creating adjacency lists for nodes 
	for(int i = 0; i < size; i++) {
	    Node n = this.network.getNode(i);
	    	    
	    if(this.network.getNode(i - 1) != null && (i+1)%(cols+1) != 1) { // add node to the left (if it is not the most left node)
		n.addNode(this.network.getNode(i - 1));
	    }
	    
	    if(this.network.getNode(i + 1) != null && (i+1)%(cols+1) != 0) { // add node to the right (if it is not the most right node)
		n.addNode(this.network.getNode(i + 1));
	    }

	    if(this.network.getNode(i - (cols + 1)) != null && (i+1) > (cols+1)) { // add node to the top (if it is not the most top node)
		n.addNode(this.network.getNode(i - (cols + 1)));
	    }

	    if(this.network.getNode(i + (cols + 1)) != null && i < size-cols) { // add node to the bottom (if it is not the most bottom node)
		n.addNode(this.network.getNode(i + (cols + 1)));
	    }	    
	    
	}
	
	// (re)set mouse listener
	this.resetMouseListener();
	System.out.println("Network has been built.");
    }
    
    public void resetGrid() {
	this.network = new Network();
	this.repaint();
    }
    
    public void resetMouseListener() {
	this.removeMouseListener(this.mouseListener);
	this.mouseListener = new GridMouseListener(this.network, cols, rows, rectWidth, rectHeight, marginLeft, marginTop);		
	this.addMouseListener(this.mouseListener);	
    }
    
    public Network getNetwork() {
	return this.network;
    }
    
    public void saveToFile(File file) {
	try {
	    FileWriter fw = new FileWriter(file);
	    fw.write(this.rows + " " + this.cols + "\n");
	    fw.write(this.network.toString());
	    fw.close();
	} catch(IOException e) {
	    System.out.println(e.getMessage());
	}
    }   
    
    public void readFromFile(File file) {
	try {
	    Scanner reader = new Scanner(file);

	    int i = 0;
	    while(reader.hasNext()) {
		String line = reader.nextLine();

		if(i == 0) {
		    this.setRows(Integer.parseInt(line.substring(0, line.indexOf(" "))));
		    this.setCols(Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.length())));
		    this.initNetwork();
		} else {
		    int name = Integer.parseInt(line.substring(0, line.indexOf(" ")));
		    int behavior = Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.indexOf(" ", line.indexOf(" ") + 1)));
		    this.network.getNode(name).setBehavior(behavior, -1);
		}
		
		i++;
	    }
	    
	    this.repaint();
	} catch(FileNotFoundException | NumberFormatException e) {
	    System.out.println(e.getMessage());
	}
    }    
    
    public void setRows(int rows) {
	this.rows = rows;
    }
    
    public void setCols(int cols) {
	this.cols = cols;
    }

    private void calculateDimensions() {
	int height = this.getSize().height;
	int width = this.getSize().width;
	this.rectWidth = width / cols;
	this.rectHeight = height / rows;	
        
        // add margins if the grid is too close to edges of its container
        // this is done by adding more cols/rows that won't be rendered
        if(rectWidth*cols > (rectWidth - 10)) {
            this.rectWidth = width / (cols + 1); 
        }
        
        if(rectHeight*rows > (rectWidth - 10)) {
            this.rectHeight = height / (rows + 1);	
        }
        
	this.marginLeft = (width - (cols*rectWidth)) / 2;
	this.marginTop =  (height - (rows*rectHeight)) / 2;    
    }
        
}
