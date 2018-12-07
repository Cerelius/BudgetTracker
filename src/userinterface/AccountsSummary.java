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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AccountsSummary extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Payment Accounts");
	JButton addAcct = new JButton("Add Account");
	JButton budSum = new JButton("Budget Summary");
	ArrayList<Account> acct_list;
	
	public AccountsSummary(){
		top.add(header);
		middle.setLayout(new GridBagLayout());
		try {
			acct_list = getAccounts();
		} catch (SQLException e1) {
				e1.printStackTrace();
		}
		createMiddle(acct_list);
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(budSum);
		budSum.addActionListener(this);
		bottom.add(addAcct);
		addAcct.addActionListener(this);
	}
	
	public void createMiddle( ArrayList<Account> acct_list){
		int row = 1;
		for (Account account : acct_list){
			String acct_number = account.getNum();
			String rout_number = account.getRout();
			String acct_string = account.getInfo();
			JLabel acct_info = new JLabel(acct_string);
			JButton edit_button = new JButton("Edit");
			JButton delete_button = new JButton("Delete");
			edit_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//method to populate add account screen with this account's data
					//AddAccount.populate();
					BudgetApplet.changeScreen("Add Account");
					}
				/*
				 * String acct_number = account.getNum();
				 * String rout_number = account.getRout();
				 * populate(acct_number, rout_number)
				 * query for account info from id
				 * set text fields to info from query
				 * 
				 */
				
			});
			
			delete_button.addActionListener(new ActionListener(){
				ArrayList<Account> acc;
				public void actionPerformed(ActionEvent e){
					//ask user in popup if they really want to delete this account
					Object [] options = {"Yes","No"};
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this Account?", 
							"Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
					//if user selects YES, delete account and all linked transactions and then send message that account has been deleted
					if (n == JOptionPane.YES_OPTION){
						try {
							deleteAccount(acct_number,rout_number);
							//send user alert that account was deleted
							JOptionPane.showMessageDialog(null,"Account: "+ acct_string + "\n and linked transactions deleted ");
							//BudgetApplet.screens.repaint();
							middle.removeAll();
							try {
								acc = getAccounts();
							} catch (SQLException e1) {
									e1.printStackTrace();
							}
							createMiddle(acc);
						} catch (SQLException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null,"Problem deleteing account from the database");
						}
					}
					}
			});
			JPanel acct = new JPanel();
			acct.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 4;
			acct.add(acct_info, c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 5;
			d.gridy = 1;
			d.gridwidth = 1;
			acct.add(edit_button, d);
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 6;
			e.gridy = 1;
			e.gridwidth = 1;
			acct.add(delete_button, e);
			
			GridBagConstraints f = new GridBagConstraints();
			f.fill = GridBagConstraints.HORIZONTAL;
			f.gridx = 1;
			f.gridy = row;
			f.gridwidth = 1;
			middle.add(acct,f);
			row++;
		}
		}
	
	public ArrayList<Account> getAccounts() throws SQLException{
		Connection conn = get_connection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM UserAccounts WHERE Username = \"SuperKoolUser91\";";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	ArrayList <Account> accts = new ArrayList<>();
    	
    	while (rs.next()) {
    		String acct_num = rs.getString(1);
    		String rout_num = rs.getString(2);
    		// account info =  account name and bank name 
    		String acct_info = rs.getString(4)+ " - "+ rs.getString(3);
    		Account result = new Account(acct_num, rout_num, acct_info);
    		accts.add(result);
    	}
    	conn.close();
    	return accts;
	}
	
	public void deleteAccount(String acct_num, String rout_num) throws SQLException{
		// execute update to delete account from database
		Connection conn = get_connection();
    	Statement stmt = conn.createStatement();
    	//insert query here
    	String sql = "DELETE FROM UserAccounts WHERE AccountNumber = ? AND RoutingNum = ?;";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, acct_num);
    	prepared_statement.setString(2, rout_num);
    	prepared_statement.executeUpdate();
    	conn.close();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if budSum is pressed
		if (label == "Budget Summary"){
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if addAcct is pressed 
		if (label == "Add Account"){
			BudgetApplet.changeScreen("Add Account");
		}
		
	}
	
	public class Account{
		private String account_number;
		private String routing_number;
		private String acct_string;
		
		public Account(String acct_num, String rout_num, String acct_str){
			this.account_number = acct_num;
			this.routing_number = rout_num;
			this.acct_string = acct_str;
		}
		public String getNum(){
			return account_number;
		}
		public String getRout(){
			return routing_number;
		}
		public String getInfo(){
			return acct_string;
		}
		
	}
}

