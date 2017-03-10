package xyz.koleno.GridDiffusion.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Dusan Koleno
 */
public class SettingsWindow extends JFrame {
    
    private JPanel panel;
    private JComboBox adaptBox;
    private JComboBox randomizeBox;
    private JTextField updateInterval;
    private JTextField cols;
    private JTextField rows;
    private Preferences prefs;
    private MainWindow window;
    
    public SettingsWindow(MainWindow window) {
	super("Settings");

	this.window = window;
	
	this.setResizable(false);
	this.setDefaultCloseOperation(HIDE_ON_CLOSE);	
	this.setSize(600, 200);
	this.init();

	this.prefs = Preferences.userNodeForPackage(xyz.koleno.GridDiffusion.Application.class);	
	this.loadPrefs();
	
	this.setVisible(true);
    }
    
    private void init() {
	this.panel = new JPanel();
	this.panel.setBorder(new EmptyBorder(5, 5, 5, 5));	
	this.panel.setLayout(new GridLayout(6, 2));
	
	String[] options = {"No", "Yes"};
	
	panel.add(new JLabel("Allow more than one change of behavior?"));
	adaptBox = new JComboBox(options);
	panel.add(adaptBox);
	
	panel.add(new JLabel("Randomize over two equivalent behaviors?"));
	randomizeBox = new JComboBox(options);
	panel.add(randomizeBox);	
	
	panel.add(new JLabel("Update interval (in seconds): "));
	updateInterval = new JTextField();
	panel.add(updateInterval);	
	
	panel.add(new JLabel("Rows (max. 100)"));
	rows = new JTextField();
	panel.add(rows);
	
	panel.add(new JLabel("Columns (max. 100)"));
	cols = new JTextField();
	panel.add(cols);	
	
	panel.add(new JLabel(" "));
	JButton saveButton = new JButton("Save");
	saveButton.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		saveButtonSubmitted();
	    }
	});
	panel.add(saveButton);
	
	this.add(this.panel);
    }
    
    private void loadPrefs() {
	this.cols.setText(String.valueOf(this.prefs.getInt("cols", 10)));
	this.rows.setText(String.valueOf(this.prefs.getInt("rows", 10)));
	this.updateInterval.setText(String.valueOf(this.prefs.getInt("updateInterval", 2)));
	this.randomizeBox.setSelectedIndex(this.prefs.getInt("randomize", 1));
	this.adaptBox.setSelectedIndex(this.prefs.getInt("adaptation", 1));	
    }
    
    private void savePrefs() {
	int colsNum = (Integer.parseInt(this.cols.getText()) > 100) ? 100 : Integer.parseInt(this.cols.getText());
	int rowsNum = (Integer.parseInt(this.rows.getText()) > 100) ? 100 : Integer.parseInt(this.rows.getText());	
	
	this.prefs.putInt("updateInterval", Integer.parseInt(this.updateInterval.getText()));
	this.prefs.putInt("cols", colsNum);
	this.prefs.putInt("rows", rowsNum);
	this.prefs.putInt("randomize", this.randomizeBox.getSelectedIndex());
	this.prefs.putInt("adaptation", this.adaptBox.getSelectedIndex());
    }
    
    private void saveButtonSubmitted() {
	this.savePrefs();
	this.window.updateGridSize();
	JOptionPane.showMessageDialog(this, "Settings have been saved.");
    }
}
