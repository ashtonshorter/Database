package data.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import data.controller.DataAppController;


public class DynamicDataPanel extends JPanel
{
	private SpringLayout baseLayout;
	private DataAppController baseController;
	private JButton submitQueryBtn;
	private JTextArea displayArea;
	private ArrayList<JTextField> inputFieldList;
	private String tableName;
	
	public DynamicDataPanel(DataAppController controller, String tableName) {
		//add your items
		this.baseController = controller;
		this.tableName = tableName;
		baseLayout = new SpringLayout();
		displayArea = new JTextArea(10,20);
		submitQueryBtn = new JButton("A Button");
		
		
		//call the methods
		setupPanel(tableName);
		setupPane();
		setupLayout();
		setupListeners();
	}
	
	private void setupPane() {
		//setup display area
		displayArea.setEditable(false);
		displayArea.setLineWrap(true);
		displayArea.setWrapStyleWord(true);
		displayArea.setBackground(Color.DARK_GRAY);
	}
	
	/**
	 * Dynamically instantiates the components for the panel
	 * @param table the Table that provides the reference for the input and display components
	 */
	private void setupPanel(String tableName) {
		this.setBackground(Color.LIGHT_GRAY);
		this.setLayout(baseLayout);

		this.add(submitQueryBtn);
		int verticalOffset = 20;
		String [] columns = baseController.getDatabase().getDatabaseColumnNames(tableName);
		
		for(int fieldCount = 0; fieldCount < columns.length; fieldCount++) {
			JTextField dynamicField = new JTextField(20);
			JLabel dynamicLabel = new JLabel(columns[fieldCount] = "entry field:");
			
			this.add(dynamicField);
			this.add(dynamicLabel);
			
			dynamicLabel.setName(columns[fieldCount] + "Label");
			dynamicField.setName(columns[fieldCount] + "Field");
			
			baseLayout.putConstraint(SpringLayout.NORTH, dynamicLabel, verticalOffset, SpringLayout.NORTH, this);
			baseLayout.putConstraint(SpringLayout.NORTH, dynamicField, verticalOffset, SpringLayout.NORTH, this);
			baseLayout.putConstraint(SpringLayout.EAST, dynamicField, 60, SpringLayout.EAST, dynamicLabel);
			
			verticalOffset += 50;
		}
	}
	
	private void setupLayout() {	//put layout code in here for offsets etc..
		//myBtn
		baseLayout.putConstraint(SpringLayout.NORTH,  submitQueryBtn, 10, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST,  submitQueryBtn, 10, SpringLayout.WEST, this);
		//displayArea
		baseLayout.putConstraint(SpringLayout.NORTH, displayArea, 53, SpringLayout.NORTH, this);
		baseLayout.putConstraint(SpringLayout.WEST, displayArea, 138, SpringLayout.WEST, this);
	}
	
	private void setupListeners() {
		submitQueryBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent click) {
				String query = "INSERT INTO " + tableName + getFieldList() + " VALUES " + getValueList() + ";";
				baseController.getDatabase().submitQuery(query);
			}
		});
	}
	
	private String getValueList() {
		String values = "(";
		
		for(int index = 0; index < inputFieldList.size(); index++) {
			String temp = inputFieldList.get(index).getText();
			
			if(index == inputFieldList.size()-1) {	//if last part, end with a ')'
				values += "'" + temp + "',)";
			} else {
				values += "'" + temp + "', ";	//else if not end of list, add a comma
			}
		}
		
		return values;
	}
	
	private String getFieldList() {
		String fields = "(";
		//needs the format(`field`, `field`, `field`)
		for(int index = 0; index < inputFieldList.size(); index++) {
			String temp = inputFieldList.get(index).getName();
			int cutoff = temp.indexOf("Field");
			temp = temp.substring(0,cutoff);
			
			if(index == inputFieldList.size()-1) {
				fields += "`" + temp + "`,)";
			} else {
				fields += "`" + temp + "`, ";
			}
			
		}
		
		return fields;
	}
}
