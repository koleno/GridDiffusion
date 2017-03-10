package xyz.koleno.GridDiffusion.app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Node (vertex)
 * 
 * @author Dusan Koleno
 */
public class Node {
    
    private int name;
    private int behavior;
    private int lastChange;
    private ArrayList<Node> adjacencyList;
    private LinkedList<Integer> historyOfBehaviors;
    private LinkedList<Integer> historyOfChanges;
    
    public Node(int name) {
	this.name = name;
	this.behavior = -1;
	this.lastChange = -1;
	this.historyOfBehaviors = new LinkedList<>();
	this.historyOfChanges = new LinkedList<>();	
	this.adjacencyList = new ArrayList<>();
    }
    
    public int getName() {
	return this.name;
    }
    
    /**
     * Sets behavior for a node
     * @param b chosen behavior
     * @param time time when the behavior was acquired
     */
    public void setBehavior(int b, int time) {
	this.historyOfBehaviors.add(this.behavior);
	this.historyOfChanges.add(this.lastChange);
	this.lastChange = time; // set time when behavior is changing
	this.behavior = b; // set new behavior
    }
    
    public int getBehavior() {
	return this.behavior;
    }
    
    public int getLastChange() {
	return this.lastChange;
    }
    
    /**
     * Goes back to previous state of this node
     */
    public void goBack() {
	if(!this.historyOfBehaviors.isEmpty()) {
 	    this.behavior = this.historyOfBehaviors.getLast();
	    this.lastChange = this.historyOfChanges.getLast();
	    this.historyOfBehaviors.removeLast();
	    this.historyOfChanges.removeLast();
	}
    }
    
    public void addNode(Node n) {
	this.adjacencyList.add(n);
    }
    
    /**
     * Returns adjacent nodes (neighbors)
     * @return list of nodes
     */
    public ArrayList<Node> getNodes() {
	return this.adjacencyList;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 61 * hash + Objects.hashCode(this.name);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final Node other = (Node) obj;
	if (!Objects.equals(this.name, other.name)) {
	    return false;
	}
	return true;
    }
    
    @Override
    public String toString() {
	String ret = this.getName() + " " + this.getBehavior();
	
	int i = 0;
	for(Node n : this.getNodes()) {
	    if(i == 0) {
		ret += " ";
	    }
	    
	    if(i < this.getNodes().size() - 1) {
		ret += n.getName() + " ";
	    } else {
		ret += n.getName();
	    }
	    
	    i++;
	}
	
	return ret;
    }
}
