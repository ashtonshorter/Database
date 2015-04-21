package data.controller;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

import javax.swing.JOptionPane;

import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.ResultSetMetaData;

import data.model.QueryInfo;
import data.view.dataFrame;

public class dataController
{
	private String connectionString;	//how you talk to the database
	private Connection databaseConnection;
	private String currentQuery;
	private DataAppController baseController;
	
	/**
	 * Declares objects and loads object 
	 */
	public dataController(DataAppController baseController) {
		this.baseController = baseController;
		connectionString = "jdbc:mysql://localhost/gasoline_travel?user=root";	//gets the database address
		//connectionStringBuilder();
		checkDriver();
		setupConnection();
	}
	
	public void submitQuery(String tableName) {
		
	}
	
	/**
	 * Change connection string so we are able to talk to other databases
	 * @param pathToDBServer path to the database server
	 * @param databaseName name of the database
	 * @param userName user name for the database server
	 * @param password	password for the database server
	 */
	public void connectionStringBuilder(String pathToDBServer, String databaseName, String userName, String password) {
		connectionString = "jdbc:mysql://";	//wipes out connection string, starts new
		connectionString += pathToDBServer;
		connectionString += "/" + databaseName;
		connectionString += "?user=" + userName;	//"?" = end of path, sends it to program at end of path
		connectionString += "&password=" + password;
	}

	
	/**
	 * Gets the driver for the database, if not installed, return error and shut down
	 */
	private void checkDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch(Exception currentException) {
			displayErrors(currentException);	//driver not loaded
			System.exit(1);	//shut down
		}
	}
	
	/**
	 * If there is no error, close connection
	 */
	public void closeConnection() {
		try {
			databaseConnection.close();
		} catch(SQLException error) {
			displayErrors(error);
		}
	}
	
	/**
	 * Sets up the connection, returns error if unable to
	 */
	private void setupConnection() {
		try {
			databaseConnection = (Connection) DriverManager.getConnection(connectionString);
		} catch(SQLException currentException) {
			displayErrors(currentException);
		}
	}
	
	/**
	 * Gets current error and displays it
	 * @param currentException current error with the database
	 */
	public void displayErrors(Exception currentException) {
		JOptionPane.showMessageDialog(baseController.getFrame(), currentException.getMessage());
		if(currentException instanceof SQLException) {
			JOptionPane.showMessageDialog(baseController.getFrame(), "SQL State: " + ((SQLException) currentException).getSQLState());
			JOptionPane.showMessageDialog(baseController.getFrame(), "SQL Error Code: " + ((SQLException) currentException).getErrorCode());
	
		}	
	}
	
	/**
	 * displays the database tables and errors if any
	 * @return results
	 */
	public String displayTables() {
		String results = "";
		String query = "SHOW TABLES";
		
		try {
			Statement firstStatement = databaseConnection.createStatement();	//gets connection
			ResultSet answer = firstStatement.executeQuery(query);	//executes the query
			
			while(answer.next()) {
				results += answer.getString(1) + "\n";	//answer shrinks
			}
			
			answer.close();
			firstStatement.close();
		} catch(SQLException error) {
			displayErrors(error);
		}
		
		return results;	
	}
	
	/**
	 * describes the database tables and reports errors if any
	 * @return results
	 */
	public String describeTables() {
		String results = "";
		String query = "DESCRIBE kwikee_marts";
		
		try {
			Statement firstStatement = databaseConnection.createStatement();	//gets connection
			ResultSet answer = firstStatement.executeQuery(query);	//executes the query
			
			while(answer.next()) {
				results += answer.getString(1) + "\t" + answer.getString(2) + "\t" + answer.getString(3) + "\n";	//answer shrinks
			}
			
			answer.close();
			firstStatement.close();
		} catch(SQLException error) {
			displayErrors(error);
		}
		
		return results;	
	}
	
	/**
	 * Checks if there is a structure violation
	 * @return if there is a structure violation
	 */
	private boolean checkForStructureViolation() {
		if(currentQuery.toUpperCase().contains(" DATABASE ")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method for dropping tables/indeces from a database. Throws exception if attempted
	 * @throws SQLException when the query contains a DROP DATABASE command.
	 */
	public void dropStatement() {
		String results = "";
		try {
			if(checkForStructureViolation()) {
				throw new SQLException("You dropped the database.", "Try again.", Integer.MIN_VALUE);
			}
			
			if(currentQuery.toUpperCase().contains("INDEX")) {
				results = "The index was: ";
			} else {
				results = "The table was: ";
			}
			
			Statement dropStatement = databaseConnection.createStatement();
			int affected = dropStatement.executeUpdate(currentQuery);
			
			dropStatement.close();
			
			if(affected == 0) {
				results += "dropped";
			}
			
			JOptionPane.showMessageDialog(baseController.getFrame(), results);
		} catch(SQLException dropError) {
			displayErrors(dropError);
		}
		
		
	}
	
	public String [] getDatabaseColumnNames(String tableName) {
		String[] columns;
		currentQuery = "SELECT * FROM `" + tableName + "`";
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		
		try {
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answers = firstStatement.executeQuery(currentQuery);
			ResultSetMetaData answerData = answers.getMetaData();
			
			columns = new String[answerData.getColumnCount()];
			
			for(int column = 0; column < answerData.getColumnCount(); column++) {
				columns[column] = answerData.getColumnName(column+1);
			}
			
			answers.close();
			firstStatement.close();
			endTime = System.currentTimeMillis();
			
		} catch(SQLException currentException) {
			endTime = System.currentTimeMillis();
			columns = new String [] {"empty"};
			displayErrors(currentException);
		}
		
		long queryTime = endTime - startTime;
		baseController.getQueryList().add(new QueryInfo(currentQuery, queryTime));
		return columns;
	}
	
	public String[] getMetaData() {
		String[] columnInformation;
		
		try {
			Statement firstStatement = databaseConnection.createStatement();
			ResultSet answer = firstStatement.executeQuery(currentQuery);
			ResultSetMetaData myMeta = answer.getMetaData();
			
			columnInformation = new String[myMeta.getColumnCount()];
			
			for(int index = 0; index < myMeta.getColumnCount(); index++) {
				columnInformation[index] = myMeta.getColumnName(index+1);
			}
			
			answer.close();
			firstStatement.close();
			
		} catch(SQLException currentException) {
			columnInformation = new String [] {"nada exists"};
			displayErrors(currentException);
		}
		return columnInformation;
	}
	
	public String [][] realInfo() {
		return null;
	}
	
	public String [][] tableInfo() {
		return null;
	}
}
