package xyz.koleno.GridDiffusion.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Menu
 * @author Dusan Koleno
 */
public class Menu extends JMenuBar {

    private JMenu appMenu;
    
    public Menu() {
	super();
	init();
    }
    
    private void init() {
	this.appMenu = new JMenu("Application");
	this.add(appMenu);
	
	JMenuItem openItem = new JMenuItem("Open from file");
	openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
	openItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		openItemSelected();
	    }
	});
	
	this.appMenu.add(openItem);
	
	JMenuItem saveItem = new JMenuItem("Save to file");
	saveItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));	
	saveItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		saveItemSelected();
	    }
	});
	
	this.appMenu.add(saveItem);
	
	JMenuItem settingsItem = new JMenuItem("Settings");
	settingsItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		settingsItemSelected();
	    }
	});
	
	this.appMenu.add(settingsItem);
	
	JMenuItem resetItem = new JMenuItem("Reset Grid");
	resetItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		resetItemSelected();
	    }
	});
	
	this.appMenu.add(resetItem);
	
	JMenuItem aboutItem = new JMenuItem("About");
	aboutItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		aboutItemSelected();
	    }
	});
	
	this.appMenu.add(aboutItem);
    }
    
    private void settingsItemSelected() {
	SettingsWindow settingsWindow = new SettingsWindow((MainWindow)SwingUtilities.getWindowAncestor(this));
    }
    
    private void resetItemSelected() {
	MainWindow window = (MainWindow)SwingUtilities.getWindowAncestor(this);
	window.getGrid().resetGrid();
    }
    
    private void saveItemSelected() {
	MainWindow window = (MainWindow)SwingUtilities.getWindowAncestor(this);
	JFileChooser fc = new JFileChooser();
	int ret = fc.showSaveDialog(window);
	if(ret == JFileChooser.APPROVE_OPTION) {
	    window.getGrid().saveToFile(fc.getSelectedFile());
	}
    }
    
    private void openItemSelected() {
	MainWindow window = (MainWindow)SwingUtilities.getWindowAncestor(this);
	JFileChooser fc = new JFileChooser();
	int ret = fc.showOpenDialog(window);
	if(ret == JFileChooser.APPROVE_OPTION) {
	    window.getGrid().readFromFile(fc.getSelectedFile());
	}
    }
    
    private void aboutItemSelected() {
	MainWindow window = (MainWindow)SwingUtilities.getWindowAncestor(this);
	AboutWindow aboutWindow = new AboutWindow(window);
    }
    
}