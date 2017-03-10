package xyz.koleno.GridDiffusion.gui;

import xyz.koleno.GridDiffusion.app.Behaviors;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import javax.swing.BoxLayout;
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
	this.setVisible(true);
    }
    
    private void init() {
	this.mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
	
	this.initMenu();
	
	grid = new Grid();
	grid.setCols(this.prefs.getInt("rows", 10));
	grid.setRows(this.prefs.getInt("cols", 10));
	mainPanel.add(grid);	
	
	this.initOptions();
	
	this.add(mainPanel);
    }
    
    private void initMenu() {
	this.setJMenuBar(new Menu());
    }
    
    private void initOptions() {
	
	JPanel options = new JPanel();
	options.setMaximumSize(new Dimension(120, this.getSize().height));	
	options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		
	Behaviors beh = Behaviors.getInstance();
	this.optBeh = new JTextField[beh.getBehaviors().length];
	for(int i = 0; i < beh.getBehaviors().length; i++) {
	    JLabel labBeh = new JLabel("Behavior " + beh.getBehaviors()[i] + ": ");
	    labBeh.setForeground(beh.getColor(i));
	    options.add(labBeh);
	    optBeh[i] = new JTextField("" + beh.getPayoff(i));
	    options.add(optBeh[i]);
	}	
	
        // buttons
	startButton = new JButton("Start diffusion");
	startButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		startButtonSubmitted();
	    }
	});
	options.add(startButton);
	
	stepButton = new JButton("One step");
	stepButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		stepButtonSubmitted();
	    }
	});
	options.add(stepButton);
	
	backButton = new JButton("Back (one step)");
	backButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		backButtonSubmitted();
	    }
	});
	options.add(backButton);
	
	mainPanel.add(options);

	backInitButton = new JButton("Back (start position)");
	backInitButton.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		backInitButtonSubmitted();
	    }
	});
	options.add(backInitButton);
	
	mainPanel.add(options);	
		
    }
    
    private void stepButtonSubmitted() {
	Behaviors beh = Behaviors.getInstance();
	for(int i = 0; i < optBeh.length; i++) {
	    beh.setPayoff(i, Double.parseDouble(optBeh[i].getText()));
	}
	
	this.grid.getNetwork().startDiffusionNew();
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
		    grid.getNetwork().startDiffusionNew();
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
