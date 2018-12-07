package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
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

import userinterface.AccountsSummary.Account;
public class TransactionSummary extends BasicLayout implements ActionListener {

	JLabel header = new JLabel("Transactions");
	JButton addTrans = new JButton("Add Transaction");
	JButton budSum = new JButton("Budget Summary");
	ArrayList<Transaction> trans_list;
	
	public TransactionSummary(){
		top.add(header);
		middle.setLayout(new GridBagLayout());
		try {
			trans_list = getTransactions();
		} catch (SQLException e1) {
				e1.printStackTrace();
		}
		createMiddle(trans_list);
	
		bottom.add(budSum);
		budSum.addActionListener(this);
		bottom.add(addTrans);	
		addTrans.addActionListener(this);
	}
	
	
	public void createMiddle(ArrayList<Transaction> trans_list ){
		int row = 1;
		for (Transaction tran: trans_list){ 
			JLabel trans_info = new JLabel(tran.getTransString());
			JButton edit_button = new JButton("Edit");
			JButton delete_button = new JButton("Delete");
			edit_button.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					//method to populate add transaction screen with this transaction's data
					//AddTransaction.populate();
					BudgetApplet.changeScreen("Add Transaction");
					}
			});
			delete_button.addActionListener(new ActionListener(){
				ArrayList<Transaction> trans_list;
				public void actionPerformed(ActionEvent e){
					//ask user if they are sure that they want to delete the transaction 
					Object [] options = {"Yes","No"};
					int n = JOptionPane.showOptionDialog(null, "Are you sure you want to delete this Transaction?", 
							"Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
					//if user selects YES, delete transaction and then send message that Transaction has been deleted
					if (n == JOptionPane.YES_OPTION){
						try {
							deleteTransaction(tran.getCreditID());
							JOptionPane.showMessageDialog(null,"Transaction: "+ tran.getTransString() + " deleted ");		
							middle.removeAll();
							try {
								trans_list = getTransactions();
							} catch (SQLException e1) {
									e1.printStackTrace();
							}
							createMiddle(trans_list);
							
						} catch (SQLException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null,"Problem deleting transaction from the database");
						}
					}
				}
			});
			JPanel trans = new JPanel();
			trans.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 4;
			trans.add(trans_info, c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 5;
			d.gridy = 1;
			d.gridwidth = 1;
			trans.add(edit_button, d);
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 6;
			e.gridy = 1;
			e.gridwidth = 1;
			trans.add(delete_button, e);
			
			GridBagConstraints f = new GridBagConstraints();
			f.fill = GridBagConstraints.HORIZONTAL;
			f.gridx = 1;
			f.gridy = row;
			f.gridwidth = 1;
			middle.add(trans,f);
			row++;
			}
			
	}
	
	public ArrayList<Transaction> getTransactions() throws SQLException{
		Connection conn = get_connection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT CreditID, Title, Amount, Category, DateCreated FROM Credit WHERE Username = \"NotSoKoolUser11\" ORDER BY DateCreated ASC;"; 
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	ArrayList <Transaction> accts = new ArrayList<>();
    	
    	while (rs.next()) {
    		
    		String creditID = rs.getString(1);
    		String title = rs.getString(2);
    		String amount = rs.getString(3);
    		String category = rs.getString(4);
    		String date = rs.getString(5);
    		// transaction info =  title,amount,category,date
    		String transaction_info = date + "   "+ title + "   $" + amount  + "   " + category;
    		Transaction result = new Transaction(creditID, transaction_info);
    		accts.add(result);
    	}
    	conn.close();
    	return accts;
	}
	
	public void deleteTransaction(String creditID) throws SQLException{
		// execute update to delete account from database
		Connection conn = get_connection();
    	Statement stmt = conn.createStatement();
    	//insert query here
    	String sql = "DELETE FROM Credit WHERE CreditID = ? AND Username = ?;";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, creditID);
    	prepared_statement.setString(2, "NotSoKoolUser11");
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
		//if addTrans is pressed 
		else if (label == "Add Transaction"){
			BudgetApplet.changeScreen("Add Transaction");
		}
	}
	public class Transaction{
		private String creditID;
		private String trans_string;
		
		public Transaction(String credit, String trans_str){
			this.creditID = credit;
			this.trans_string = trans_str;
		}
		public String getCreditID(){
			return creditID;
		}
		public String getTransString(){
			return trans_string;
		}
		
	}
}
