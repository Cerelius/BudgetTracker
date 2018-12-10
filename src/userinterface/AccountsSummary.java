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
	JButton load_button = new JButton("Load Accounts");
	ArrayList<Account> acct_list;
	
	/** AccountsSummary
	 * The Accounts Summary screen displays the name and bank of each of the users accounts
	 * and allows the user to edit or delete each account 
	 */
	public AccountsSummary(){
		top.add(header);
		// set middle of screen to start with a load button
		// when pressed, this button will query the database for accounts and display them on the screen  
		middle.setLayout(new GridBagLayout());
		callCreateMiddle();
		// add buttons to bottom of the screen in order to navigat to other screens 
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(budSum);
		budSum.addActionListener(this);
		bottom.add(addAcct);
		addAcct.addActionListener(this);
	}
	
	public void callCreateMiddle(){
		middle.removeAll();
		try {
			acct_list = getAccounts();
		} catch (SQLException e1) {
				e1.printStackTrace();
		}
		createMiddle(acct_list);
		updateUI();
	}
	
	/** createMiddle
	 * this method takes in a list of account objects and displays them along with
	 * edit and delete buttons for each
	 */
	public void createMiddle( ArrayList<Account> acct_list){
		int row = 1;
		for (Account account : acct_list){
			//get attributes from account object
			String acct_number = account.getNum();
			String rout_number = account.getRout();
			String acct_string = account.getInfo();
			JLabel acct_info = new JLabel(acct_string);
			//create edit and delete buttons
			JButton edit_button = new JButton("Edit");
			JButton delete_button = new JButton("Delete");
			// if edit button is pressed, populate add account screen with account's info 
			edit_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					// reset current screen to load button for the next time you return to this screen 
					middle.removeAll();
					middle.add(load_button);
					AddAccount.populate(acct_number, rout_number);
					BudgetApplet.changeScreen("Add Account");
					}
			});
			// if the delete button is pressed, ask user "are you sure" and then delete account from the database 
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
							//reload screen so that the deleted account no longer is visible 
							middle.removeAll();
							try {
								acc = getAccounts();
							} catch (SQLException e1) {
									e1.printStackTrace();
							}
							createMiddle(acc);
							BudgetApplet.changeScreen("Budget Summary");
							BudgetApplet.changeScreen("Accounts Summary");
						} catch (SQLException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null,"Problem deleteing account from the database");
						}
					}
					}
			});
			// add account info, edit button and delete button to JPanel acct
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
			// add acct to middle JPanel and increment row so that the next account appears below 
			GridBagConstraints f = new GridBagConstraints();
			f.fill = GridBagConstraints.HORIZONTAL;
			f.gridx = 1;
			f.gridy = row;
			f.gridwidth = 1;
			middle.add(acct,f);
			row++;
		}
		}
	
	/** getAccounts
	 * this method queries the database for all accounts from the user and returns
	 * an array list of account objects
	 */
	public ArrayList<Account> getAccounts() throws SQLException{
		//create connection and query for all account objects from current user 
		Connection conn = get_connection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM UserAccounts WHERE Username = ?;";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, userName);
    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	ArrayList <Account> accts = new ArrayList<>();
    	// for each result, create an account object and add it to the array list
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
	
	/** deleteAccount
	 * this method executes the database update to delete an account given its 
	 * account number and routing number 
	 */
	public void deleteAccount(String acct_num, String rout_num) throws SQLException{
		Connection conn = get_connection();
    	Statement stmt = conn.createStatement();
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
		//if budSum is pressed, reset screen to the load button and change screens 
		if (label == "Budget Summary"){
			middle.removeAll();
			middle.add(load_button);
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if addAcct is pressed, reset this screen to the load button and change screens
		if (label == "Add Account"){
			middle.removeAll();
			middle.add(load_button);
			BudgetApplet.changeScreen("Add Account");
		}
		
	}
	
	/** Account
	 * this class represents the necessary data about an account, its account number, 
	 * routing number, and a string to describe the account 
	 * The account number and routing number can be used later to access the account in the database
	 */
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

