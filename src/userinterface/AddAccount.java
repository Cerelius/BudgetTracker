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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import userinterface.AddTransaction.InvalidAddTransactionInputException;

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
	static JTextField acctNumText = new JTextField(15);
	static JTextField routNumText = new JTextField(15);
	static JTextField bankText = new JTextField(15);
	static JTextField acctNameText = new JTextField(15);
	static JTextField cardNumText = new JTextField(15);
	static JTextField balanceText = new JTextField(15);
	JLabel [] labels = {acctNumLabel, routNumLabel, bankLabel, acctNameLabel, cardNumLabel, balanceLabel};
	static JTextField [] textFields = {acctNumText, routNumText, bankText, acctNameText, cardNumText, balanceText};

	// Build the interface.
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

			try {
				String acctNum = acctNumText.getText();
				String routNum = routNumText.getText();
				//method to see if that account exists
				if (AccountExists(acctNum,routNum) == true){
					//ask if they want to override
					Object [] options = {"Yes","No"};
					int n = JOptionPane.showOptionDialog(null, "Account already exists, do you want to overwrite it?", 
							"Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
					//if user selects YES, delete account and all linked transactions and then send message that account has been deleted
					if (n == JOptionPane.YES_OPTION){
						//delete old instance of the transaction 
						updateAccount();
						System.out.println("updating");
						}
					else if(n == JOptionPane.NO_OPTION){
						//insert new transaction
						insertAccount(buildNewAccountSignature());
					}
				}
				else{
					insertAccount(buildNewAccountSignature());
				}	
			} catch (SQLException e1) {
				//This is a generic exception that indicates there was a problem with insertAccount.
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was a problem with creating your account, please check your input and try again.");
			} catch (InvalidAddAccountInputException e2) {
				//This means that there was a problem with the input from the user.
				e2.printImproperInput();
				e2.showErrorMessage();
			} finally {
				//Return to the account summary screen.
				BudgetApplet.changeScreen("Accounts Summary");
			}
		}
	}
	
	private void updateAccount() throws SQLException{
		Connection conn = get_connection();
    	//delete query
		String sql = "UPDATE UserAccounts SET AccountNumber = ?, RoutingNum = ?, Bank_Name = ?, "
				+ "Account_Title = ?, CardNum = ? WHERE AccountNumber = ? AND Username = ?;";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	try {
			String [] updateValues = buildUpdateAccountSignature();
	    	prepared_statement.setString(1, updateValues[0]);
	    	prepared_statement.setString(2, updateValues[1]);
	    	prepared_statement.setString(3, updateValues[2]);
	    	prepared_statement.setString(4, updateValues[3]);
	    	prepared_statement.setString(5, updateValues[4]);
	    	prepared_statement.setString(6, updateValues[0]);
	    	prepared_statement.setString(7, updateValues[6]);
	    	prepared_statement.executeUpdate();
		} catch (InvalidAddAccountInputException e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}	
		
	}

	private String[] buildUpdateAccountSignature() throws InvalidAddAccountInputException {
		String acctNum = acctNumText.getText();
		String routNum = routNumText.getText();
		String bank = bankText.getText();
		String acctName = acctNameText.getText();
		String cardNum = cardNumText.getText();
		String balance = balanceText.getText();
		
		String [] updateVals = {acctNum, routNum, bank, acctName, cardNum, balance, userName};
		return updateVals;
	}

	private boolean AccountExists(String acctNum, String routNum) throws SQLException{
		Connection conn = get_connection();
		String sql = "Select Account_Title from  UserAccounts where AccountNumber = ? and RoutingNum = ? and Username = ?";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setString(1, acctNum);
		prepared_statement.setString(2, routNum);
		prepared_statement.setString(3, userName);
    	ResultSet rs = prepared_statement.executeQuery();
    	try{
    		rs.next();
    		rs.getString(1);
    		return true;
    	}
    	catch(SQLException e1){
    		return false;
    	}
	}

	public static void populate(String acctNum, String routNum){
		try{
			String [] acct_info = getAcctInfo(acctNum,routNum);
			for (int i=0; i<textFields.length; i++){
				textFields[i].setText(acct_info[i]);
			}
		}
		catch (SQLException e1){
			e1.printStackTrace();
			System.out.println("error");
		}
	}
	
	public static String[] getAcctInfo(String acctNum, String routNum) throws SQLException{
		Connection conn = get_connection();
		String sql = "SELECT AccountNumber, RoutingNum, Bank_Name, Account_Title, CardNum, Balance FROM UserAccounts WHERE AccountNumber = ? AND RoutingNum = ?;";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, acctNum);
    	prepared_statement.setString(2, routNum);
    	ResultSet rs = prepared_statement.executeQuery();
    	rs.next();
    	String [] acctInfo = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)};
    	conn.close();
		return acctInfo;
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

	/** InvalidAddAccountInputException
	 * This exception is thrown when there is invalid input in the 
	 * 
	 *
	 */
	public class InvalidAddAccountInputException extends Exception {
		String cause;

		public InvalidAddAccountInputException(String cause) {
			this.cause = cause;
		}

		public void printImproperInput() {
			System.out.println(cause);
		}

		public void showErrorMessage() {
			JOptionPane.showMessageDialog(null, cause);
		}
	} 

	/** buildNewAccountSignature
	 * This will build the signature of the account to be inserted to the database
	 * using the fields.
	 * @return Signature containing the account's information.
	 * @throws InvalidAddAccountInputException 
	 */
	public String buildNewAccountSignature() throws InvalidAddAccountInputException {
		checkAddAccountInput();

		String acctNum = acctNumText.getText();
		String routNum = routNumText.getText();
		String bank = bankText.getText();
		String acctName = acctNameText.getText();
		String cardNum = cardNumText.getText();
		String balance = balanceText.getText();

		String signature = "\"" + acctNum+ "\","
				+ " \"" +	routNum+ "\","
				+ " \"" + bank+ "\","
				+ " \"" + acctName+ "\","
				+ " \"" + userName+ "\","
				+ " \"" + cardNum+ "\","
				+ " \"" + balance + "\"";

		return signature;
	}

	/** checkInput
	 * This method validates the input from the fields.
	 * @param values - a string that is passed to the database that contains all of the query information.  
	 * @throws inputInvalidException - There was a problem with the field information, the message defines what is wrong.  
	 */
	public void checkAddAccountInput() throws InvalidAddAccountInputException {
		checkAccountNumber();
		checkRoutingNumber();
		checkBankName();
		checkAccountName();
		checkCardNumer();
		checkBalance();
	}

	private void checkAccountNumber() throws InvalidAddAccountInputException {
		// No non-numeric digits
		// Must not be blank
		String acctNum = acctNumText.getText();
		if (isEmpty(acctNum))
			throw new InvalidAddAccountInputException("Account Number cannot be blank");
		else if (isNotNumeric(acctNum))
			throw new InvalidAddAccountInputException("Account Number must be numeric");
	}

	private void checkRoutingNumber() throws InvalidAddAccountInputException{
		// No non-numeric digits
		// Must not be blank
		String routNum = routNumText.getText();
		if (isEmpty(routNum))
			throw new InvalidAddAccountInputException("Account Number cannot be blank");
		else if (isNotNumeric(routNum))
			throw new InvalidAddAccountInputException("Account Numer must be numeric");	
	}

	private void checkBankName() throws InvalidAddAccountInputException {
		// Must not be blank
		if (isEmpty(bankText.getText()))
			throw new InvalidAddAccountInputException("Bank Name cannot be blank");
	}

	private void checkAccountName() throws InvalidAddAccountInputException {
		// Must not be blank		
		if (isEmpty(acctNameText.getText()))
			throw new InvalidAddAccountInputException("Account Name cannot be blank");
	}

	private void checkCardNumer() throws InvalidAddAccountInputException {
		// cannot be blank
		// Must be numeric
		String cardNum = cardNumText.getText();
		if (isEmpty(cardNum))
			throw new InvalidAddAccountInputException("Card Number cannot be blank.");
		else if (isNotNumeric(cardNum))
			throw new InvalidAddAccountInputException("Card Number must be numeric.");
	}

	private void checkBalance() throws InvalidAddAccountInputException {
		// Cannot be blank
		// Must be numeric
		// Must be non-negative
		String balance = balanceText.getText();
		if (isEmpty(balance))
			throw new InvalidAddAccountInputException("Balance cannot be blank.");
		else if (isNotNumeric(balance))
			throw new InvalidAddAccountInputException("Balance has to be numeric.");
		else if (Double.parseDouble(balance) < 0)
			throw new InvalidAddAccountInputException("Balance cannot be negative.");
	}

	// Checks if a string is numeric or not.
	private boolean isNotNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return true;
		}
		return false;
	}
	
	//Check if a field is empty
	private boolean isEmpty(String field) {
		if (field.length() == 0)
			return true;
		else
			return false;
	}

}