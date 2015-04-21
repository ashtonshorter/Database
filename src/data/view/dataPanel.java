package data.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import data.controller.DataAppController;
import data.controller.dataController;

public class dataPanel extends JPanel
{
	private SpringLayout baseLayout;
	private DataAppController baseController;
	private JButton dataBtn;
	private JTextArea displayArea;
	private JScrollPane displayPane;
	private JPasswordField samplePassword;
	
	//declares objects, gets controller, and runs methods
	public dataPanel(DataAppController controller) {
		this.baseController = controller;
		baseLayout = new SpringLayout();
		dataBtn = new JButton("Test the query");
		displayPane = new JScrollPane();
		displayArea = new JTextArea(10,30);
		samplePassword = new JPasswordField(null, 20);
		
		setupLayout();
		setupPanel();
		setupListeners();
	}
	
	private void setupTable() {
		
	}
	
	//sets up attributes for the panel
	private void setupPanel() {
		this.setBackground(Color.magenta);
		this.setSize(1000, 1000);
		this.setLayout(baseLayout);
		this.add(displayPane);
		this.add(dataBtn);
		this.add(displayArea);
		this.add(samplePassword);
		
		samplePassword.setEchoChar('*');
		samplePassword.setFont(new Font("Serif", Font.BOLD, 32));
		samplePassword.setForeground(Color.BLACK);
	}
	
	//sets up the layout and offsets of objects
	private void setupLayout() {
		baseLayout.putConstraint(SpringLayout.NORTH, displayArea, 16, SpringLayout.SOUTH, dataBtn);
		baseLayout.putConstraint(SpringLayout.WEST, displayArea, 82, SpringLayout.WEST, this);
		baseLayout.putConstraint(SpringLayout.NORTH, dataBtn, 10, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, dataBtn, 10, SpringLayout.WEST, this);
	}
	
	//sets up the listeners for actions
	private void setupListeners() {
		dataBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				String databaseAnswer = baseController.getDatabase().describeTables();
				displayArea.setText(databaseAnswer);
			}
		});
	}

}
