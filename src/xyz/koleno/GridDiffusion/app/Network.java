package xyz.koleno.GridDiffusion.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.prefs.Preferences;

/**
 *
 * @author Dusan Koleno
 */
public class Network {
    
    private int time;
    private ArrayList<Node> nodes;
    //private ArrayList<Node> nodesToVisit;
    //private ArrayList<Node> selectedNodes;
    //private ArrayList<Node> futureNodesToVisit;
    private Preferences prefs;
    
    public Network() {
	this.time = 0;
	this.nodes = new ArrayList<>();
	//this.futureNodesToVisit = new ArrayList<>();
	//this.nodesToVisit = new ArrayList<>();
	//this.selectedNodes = new ArrayList<>();
	this.prefs = Preferences.userNodeForPackage(xyz.koleno.GridDiffusion.Application.class);			
    }
    
    public void addNode(Node n) {
	this.nodes.add(n);
    }    
    
    public void setBehavior(int node, int behavior, int time) {
	this.nodes.get(node).setBehavior(behavior, time);
	
	/*if(!this.selectedNodes.contains(this.nodes.get(node))) {
	    this.selectedNodes.add(this.nodes.get(node));
	}*/
    }
    
    public Node getNode(int name) {
	int index = this.nodes.indexOf(new Node(name));
	if(index == -1) {
	    return null;
	} else {
	    return this.nodes.get(index);
	}
    }
    
    public ArrayList<Node> getNodes() {
	return this.nodes;
    }
    
    public int getSize() {
	return this.nodes.size();
    }
    
    public void startDiffusionNew() {
	Behaviors beh = Behaviors.getInstance();
	for (Node n : this.nodes) {
	    double[] payoffs = new double[beh.getBehaviors().length];	    
	    
	    int[] nodesWithBehavior = new int[beh.getBehaviors().length];	   
	    for (Node neighbor : n.getNodes()) {
		if(neighbor.getBehavior() > -1 && neighbor.getLastChange() < this.time) {
		    nodesWithBehavior[neighbor.getBehavior()]++;
		}
	    }
	   
	    double maxPayoff = 0;
	    int bestBehavior = -1;
	    for(int i = 0; i < beh.getBehaviors().length; i++) {
		if(nodesWithBehavior[i]*beh.getPayoff(i) > maxPayoff) {
		    maxPayoff = nodesWithBehavior[i]*beh.getPayoff(i);
		    bestBehavior = i;
		}
		
		Random gen = new Random(System.nanoTime());
		if(maxPayoff > 0 && nodesWithBehavior[i]*beh.getPayoff(i) == maxPayoff) {
		    if(prefs.getInt("randomization", 1) == 1 && gen.nextInt(100) % 2 == 0) { // if randomization is allowed, randomly choose to adapt or not this behavior
			maxPayoff = nodesWithBehavior[i]*beh.getPayoff(i);
			bestBehavior = i;
		    } else if(prefs.getInt("randomization", 1) == 0) { // otherwise randomization is not allowed, adapt behavior
			maxPayoff = nodesWithBehavior[i]*beh.getPayoff(i);
			bestBehavior = i;		
		    }
		}
	    }

	    if (bestBehavior > -1 && n.getBehavior() != bestBehavior) {
		if(n.getLastChange() == -1 || prefs.getInt("adaptation", 1) == 1) { // adapt if it is the first behavior, or there are allowed multiple adaptations
		    n.setBehavior(bestBehavior, this.time);
		}
	    }
	}

	this.time++;
    }

    
    /*public void startDiffusion() {
	Behaviors beh = Behaviors.getInstance();
	futureNodesToVisit.clear();
	
	// if user selected adapters, visit only them
	if(selectedNodes.size() > 0) {
	    nodesToVisit.clear();
	    nodesToVisit.addAll(selectedNodes);
	}
	
	System.out.println("Size of nodes to visit: " + nodesToVisit.size());
	
	// shuffle the list, so the nodes will be choosen not by their position in the grid
	Collections.shuffle(nodesToVisit, new Random(System.nanoTime()));

	// visit choosen nodes and modify behaviors of their neighbors
	for (Node n : nodesToVisit) {
	    
	    // change behaviors of neighbors
	    for (Node neighborOfAdapter : n.getNodes()) {
		this.adaptateBehavior(neighborOfAdapter);
	    }
	    
	    // change behavior of initial adapter if needed
	    this.adaptateBehavior(n);
	}
	
	// clear nodes to visit and selected nodes after diffusion
	this.nodesToVisit.clear();
	this.selectedNodes.clear();
	
	// add previously changes nodes in this iteration to the nodes you need to visit next time
	this.nodesToVisit.addAll(futureNodesToVisit);
    }*/    
    
    /*private void adaptateBehavior(Node n) {
	Behaviors beh = Behaviors.getInstance();

	double[] payoffs = new double[beh.getBehaviors().length];
	for (Node neighbor : n.getNodes()) {
	    int neighborBehavior = neighbor.getBehavior();

	    if (neighborBehavior > -1) {
		payoffs[neighborBehavior] += beh.getPayoff(neighborBehavior);
	    }
	}

	int bestBehavior = -1;
	double maxPayoff = 0;
	for (int i = 0; i < payoffs.length; i++) {
	    if (maxPayoff < payoffs[i]) {
		bestBehavior = i;
		maxPayoff = payoffs[i];
	    }
	}

	if (bestBehavior > -1) {
	    n.setBehavior(bestBehavior, -1);
	    
	    if(!futureNodesToVisit.contains(n)) {
		futureNodesToVisit.add(n);
	    }
	}

    }*/
    
    /*public void startDiffusionOld() {
	Behaviors beh = Behaviors.getInstance();
	for(Node n : this.nodes) {
	    double[] payoffs = new double[beh.getBehaviors().length];
	    
	    for(Node neighbor : n.getNodes()) {
		int neighborBehavior = neighbor.getBehavior();
		
		if(neighborBehavior > -1) {
		    payoffs[neighborBehavior] += beh.getPayoff(neighborBehavior);
		}
	    }
	    
	    int bestBehavior = -1;
	    double maxPayoff = 0;
	    for(int i = 0; i < payoffs.length; i++) {
		if(maxPayoff < payoffs[i]) {
		    bestBehavior = i;
		    maxPayoff = payoffs[i];
		}
	    }
	    
	    if(bestBehavior > -1) {
		n.setBehavior(bestBehavior, -1);
	    }
	}
    }*/
    
    public void goBack() {
	if (this.time > -1) {
	    for (Node n : this.nodes) {
		if (n.getLastChange() == (this.time - 1)) {
		    n.goBack();
		}
	    }

	    this.time--;
	}
    }
    
    public void goBackInit() {
	if (this.time > -1) {
	    for(int i = this.time; this.time > 0; i--) {
		for (Node n : this.nodes) {
		    if (n.getLastChange() == (this.time - 1)) {
			n.goBack();
		    }
		}
		this.time--;
	    }
	}
    }    
    
    @Override
    public String toString() {
	String ret = "";
	
	for(Node n : this.nodes) {
	    ret += n + "\n";
	}
	
	return ret;
    }
}
