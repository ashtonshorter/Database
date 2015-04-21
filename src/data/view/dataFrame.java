package data.view;

import javax.swing.JFrame;

import data.controller.DataAppController;
import data.controller.dataController;

public class dataFrame extends JFrame
{
	private dataPanel basePanel;
	
	//gets the basePanel and runs methods
	public dataFrame(DataAppController controller) {
		basePanel = new dataPanel(controller);
		setupFrame();
	}
	
	//declares attributes for the frame
	private void setupFrame() {
		this.setSize(600,600);
		this.setContentPane(basePanel);
		this.setVisible(true);
	}
}
