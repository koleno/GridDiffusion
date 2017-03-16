package xyz.koleno.GridDiffusion.gui;

import java.awt.Color;
import xyz.koleno.GridDiffusion.app.Behaviors;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Main Window
 * @author Dusan Koleno
 */
public class MainWindow extends JFrame {
    
    private final JPanel mainPanel;
    private JPanel options;
    private JTextField[] optBeh;
    private JButton startButton;
    private JButton stepButton;
    private JButton backButton;
    private JButton backInitButton;
    private Grid grid;
    private Timer timer;
    private Boolean timerRunning;
    private final Preferences prefs;
    
    public MainWindow() {
	this.setTitle("Grid Diffusion");
	this.setSize(700, 500);
	this.setResizable(true);
	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	this.setMinimumSize(new Dimension(600, 500));
	mainPanel = new JPanel();
	this.timerRunning = false;
	this.prefs = Preferences.userNodeForPackage(xyz.koleno.GridDiffusion.Application.class);	
	this.init();

        this.setLocationRelativeTo(null); // center the window        
        this.setVisible(true);
    }
    
    private void init() {
	this.initMenu();
        
        this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	mainPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 0.9;
        cons.gridx = 0;
        cons.weighty= 1;	
        	
	grid = new Grid();
	grid.setCols(this.prefs.getInt("rows", 10));
	grid.setRows(this.prefs.getInt("cols", 10));
	mainPanel.add(grid, cons);	
	
	this.initOptions();
        cons.gridx = 1;
        cons.weightx = 0.1;
	mainPanel.add(options, cons);
        
	this.add(mainPanel);
    }
    
    private void initMenu() {
	this.setJMenuBar(new Menu());
    }
    
    private void initOptions() {
	options = new JPanel();
        options.setBorder(new EmptyBorder(0, 5, 0, 0));	
        options.setLayout(new GridBagLayout());
        
        // components are organized in one column with even width and height
        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = GridBagConstraints.BOTH;
        cons.weightx = 1;
        cons.gridx = 0;
        cons.weighty= 1;
        cons.insets = new Insets(0, 0, 5, 0); // bottom margin for the components
        
	Behaviors beh = Behaviors.getInstance();
	this.optBeh = new JTextField[beh.getBehaviors().length];
	for(int i = 0; i < beh.getBehaviors().length; i++) {
	    JLabel labBeh = new JLabel("Behavior " + beh.getBehaviors()[i] + ": ");
            labBeh.setVerticalAlignment(JLabel.BOTTOM);
	    labBeh.setForeground(beh.getColor(i));
	    options.add(labBeh, cons);
	    optBeh[i] = new JTextField("" + beh.getPayoff(i));
	    options.add(optBeh[i], cons);
	}	
	
        // buttons
	startButton = new JButton("Start Diffusion");
	startButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		startButtonSubmitted();
	    }
	});
	options.add(startButton, cons);
	
	stepButton = new JButton("Take One Step");
	stepButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		stepButtonSubmitted();
	    }
	});
	options.add(stepButton, cons);
	
	backButton = new JButton("Back One Step");
	backButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		backButtonSubmitted();
	    }
	});
	options.add(backButton, cons);
	
	backInitButton = new JButton("Back to Start");
	backInitButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		backInitButtonSubmitted();
	    }
	});
	options.add(backInitButton, cons);
    }
    
    private void stepButtonSubmitted() {
	Behaviors beh = Behaviors.getInstance();
	for(int i = 0; i < optBeh.length; i++) {
	    beh.setPayoff(i, Double.parseDouble(optBeh[i].getText()));
	}
	
	this.grid.getNetwork().startDiffusion();
	this.grid.repaint();
    }
    
    private void startButtonSubmitted() {
	if(!timerRunning) {
	    this.stepButton.setEnabled(false);
	    this.backButton.setEnabled(false);
	    this.startButton.setText("Pause Diffusion");

	    Behaviors beh = Behaviors.getInstance();
	    for(int i = 0; i < optBeh.length; i++) {
		beh.setPayoff(i, Double.parseDouble(optBeh[i].getText()));
	    }

	    this.timer = new Timer();
	    this.timer.schedule(new TimerTask() {
		@Override
		public void run() {
		    grid.getNetwork().startDiffusion();
		    grid.repaint();
		}
	    }, prefs.getInt("updateInterval", 2)*1000, prefs.getInt("updateInterval", 2)*1000);
	    
	    timerRunning = true;	    
	} else {
	    this.stepButton.setEnabled(true);
	    this.backButton.setEnabled(true);
	    this.startButton.setText("Start Diffusion");
	    this.timer.cancel();
	    timerRunning = false;
	}
    }
    
    protected void updateGridSize() {
	grid.setCols(this.prefs.getInt("rows", 10));
	grid.setRows(this.prefs.getInt("cols", 10));
	grid.resetGrid();
    }
    
    public Grid getGrid() {
	return this.grid;
    }
    
    private void backButtonSubmitted() {
	this.grid.getNetwork().goBack();
	this.grid.repaint();
    }
    
    private void backInitButtonSubmitted() {
	this.grid.getNetwork().goBackInit();
	this.grid.repaint();
    }    
}
