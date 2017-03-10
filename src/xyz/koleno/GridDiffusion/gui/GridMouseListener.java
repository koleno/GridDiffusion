package xyz.koleno.GridDiffusion.gui;

import xyz.koleno.GridDiffusion.app.Behaviors;
import xyz.koleno.GridDiffusion.app.Network;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 * Mouse listener attached to each node in a grid
 * 
 * @author Dusan Koleno
 */
public class GridMouseListener extends MouseAdapter {

    private final Network network;
    private final int columns;
    private final int rows;
    private final double marginLeft;
    private final double marginTop;
    private final double rectWidth;
    private final double rectHeight;
    
    public GridMouseListener(Network network, int columns, int rows, int rectWidth, int rectHeight, int marginLeft, int marginTop) {
	this.network = network;
	this.columns = columns;
	this.rows = rows;
	this.marginLeft = marginLeft;
	this.marginTop = marginTop;
	this.rectWidth = rectWidth;
	this.rectHeight = rectHeight;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
	super.mouseClicked(e);
	int x = (int) Math.round((e.getX() - marginLeft) / rectWidth);
	int y = (int) Math.round((e.getY() - marginTop) / rectHeight);
	
	int node = y * (columns + 1) + x;
	
	String ret = (String) JOptionPane.showInputDialog(null, "Choose behavior:", "Behavior", JOptionPane.PLAIN_MESSAGE, null, Behaviors.getInstance().getBehaviors(), Behaviors.getInstance().getBehaviors()[0]);
	this.network.setBehavior(node, Behaviors.getInstance().getIndex(ret), -1);
	e.getComponent().repaint();
    }
    
    
    
}
