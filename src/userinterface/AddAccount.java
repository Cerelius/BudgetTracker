package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.sql.*;

public class AddAccount extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Add Account");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Account");
	
	JLabel acctNumLabel = new JLabel("Account Number:");
	JLabel routNumLabel = new JLabel("Routing Number:");
	JLabel bankLabel = new JLabel("Bank Name:");
	JLabel acctNameLabel = new JLabel("Account Name:");
	JLabel cardNumLabel = new JLabel("Card Number:");
	JLabel balanceLabel = new JLabel("Balance:");
	JTextField acctNumText = new JTextField(10);
	JTextField routNumText = new JTextField(10);
	JTextField bankText = new JTextField(10);
	JTextField acctNameText = new JTextField(10);
	JTextField cardNumText = new JTextField(10);
	JTextField balanceText = new JTextField(10);
	JLabel [] labels = {acctNumLabel, routNumLabel, bankLabel, acctNameLabel, cardNumLabel, balanceLabel};
	JTextField [] textFields = {acctNumText, routNumText, bankText, acctNameText, cardNumText, balanceText};
	
	public AddAccount(){
		middle.setLayout(new GridBagLayout());
		for (int i=0 ; i< labels.length ; i++){
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = i+1;
			c.gridwidth=1;
			middle.add(labels[i],c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 2;
			d.gridy = i+1;
			d.gridwidth=2;
			middle.add(textFields[i],d);
			textFields[i].addActionListener(this);
		}
		top.add(header);
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(cancel);
		cancel.addActionListener(this);
		bottom.add(save);
		save.addActionListener(this);
	}

	public void testDB() throws SQLException{
		// this is a dummy method to show how to connect to the database and execute queries
		Connection conn = get_connection();
    	Statement stmt = conn.createStatement();
    	String sql = "SELECT * FROM Users where Username = ?";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	String username = "SuperKoolUser91";
    	prepared_statement.setString(1, username);
    
    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	int columnsNumber = rsmd.getColumnCount();
    	
    	while (rs.next()) 
    	{
    	    for (int i = 1; i <= columnsNumber; i++) 
    	    {
    	        if (i > 1) 
    	        {
    	        	System.out.print(",  ");
    	        }
    	        String columnValue = rs.getString(i);
    	        System.out.print(columnValue + " " + rsmd.getColumnName(i));
    	    }
    	    System.out.println("");
    	}
    	conn.close();
	}
		
	public void insertAccount(String values) throws SQLException{
		// execute update to add account into database
		Connection conn = get_connection();
    	Statement stmt = conn.createStatement();
    	String sql = "INSERT INTO UserAccounts (AccountNumber, RoutingNum, Bank_Name, Account_Title, UserName, CardNum, Balance)"
    			+ "VALUES (" + values + ")";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.executeUpdate();
    	conn.close();
    	
	}
	@Override	
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if cancel is pressed 
		if (label == "Cancel"){
			BudgetApplet.changeScreen("Accounts Summary");
		}
		//if save is pressed 
		if (label == "Save Account"){
			String acctNum = acctNumText.getText();
			String routNum = routNumText.getText();
			String bank = bankText.getText();
			String acctName = acctNameText.getText();
			String cardNum = cardNumText.getText();
			String balance = balanceText.getText();
			
			// insert account into database using these values 
			// still need to get userName from current user somehow 
			String values =  "\"" + acctNum+ "\", \"" +	routNum+ "\", \"" + bank+ "\", \"" + acctName+ "\", \"" + "SuperKoolUser91"+ "\", \"" + cardNum+ "\", \"" + balance + "\"";
 			System.out.println(values);
			try {
				insertAccount(values);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			BudgetApplet.changeScreen("Accounts Summary");
		}
		
	}
	}