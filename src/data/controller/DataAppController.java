package data.controller;

import java.util.ArrayList;

import data.model.QueryInfo;
import data.view.dataFrame;

public class DataAppController
{
	private dataFrame baseFrame;
	private dataController databaseController;
	private ArrayList <QueryInfo> queryList;
	
	public DataAppController() {
		baseFrame = new dataFrame(this);
		databaseController = new dataController(this);
		queryList = new ArrayList<QueryInfo>();
	}
	
	public void start() {
		
	}
	
	public dataController getDatabase() {
		return databaseController;
	}
	
	/**
	 * Returns the base frame
	 * @return returns the base frame
	 */
	public dataFrame getFrame() {
		return baseFrame;
	}
	
	public ArrayList<QueryInfo> getQueryList() {
		return queryList;
	}
}
