package xyz.koleno.GridDiffusion.app;

import java.util.ArrayList;
import java.util.Random;
import java.util.prefs.Preferences;

/**
 * Network - maintains collection of nodes in a grid
 * @author Dusan Koleno
 */
public class Network {
    
    private int time;
    private ArrayList<Node> nodes;
    private Preferences prefs;
    
    public Network() {
	this.time = 0;
	this.nodes = new ArrayList<>();
	this.prefs = Preferences.userNodeForPackage(xyz.koleno.GridDiffusion.Application.class);			
    }
    
    public void addNode(Node n) {
	this.nodes.add(n);
    }    
    
    public void setBehavior(int node, int behavior, int time) {
	this.nodes.get(node).setBehavior(behavior, time);
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
    
    /**
     * Starts diffusion of behaviors onto the nodes in the network
     */
    public void startDiffusion() {
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
