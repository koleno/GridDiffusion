package xyz.koleno.GridDiffusion.app;

import java.awt.Color;

/**
 * Available behaviors
 * There are 5 default ones (A,B,C,D,E)
 * 
 * @author Dusan Koleno
 */
public class Behaviors {
    
    private static Behaviors instance;
    private final Color[] colors;
    private final String[] behaviors;
    private final double[] payoffs;
    
    private Behaviors() {
	Color[] c = {Color.red, new Color(0, 200, 0), Color.blue, new Color(238,201,0), new Color(142,56,142)};
	this.colors = c;
	
	String[] b = {"A", "B", "C", "D", "E"};
	this.behaviors = b;
	
	double[] p = {1.0,1.0,1.0,1.0,1.0};
	this.payoffs = p;
    }
    
    public Color getColor(int i) {
	try {
	    return this.colors[i];
	} catch(IndexOutOfBoundsException e) {
	    return Color.LIGHT_GRAY;
	}
    }
    
    public void setPayoff(int b, double payoff) {
	this.payoffs[b] = payoff;
    }
    
    public double getPayoff(int b) {
	return this.payoffs[b];
    }
    
    public int getIndex(String name) {
	for(int i = 0; i < this.behaviors.length; i++) {
	    if(this.behaviors[i].equalsIgnoreCase(name)) {
		return i;
	    }
	}
	
	return -1;
    }
    
    public String[] getBehaviors() {
	return this.behaviors;
    }
    
    public static Behaviors getInstance() {
	if(instance == null) {
	    instance = new Behaviors();
	}
	
	return instance;
    }
}
