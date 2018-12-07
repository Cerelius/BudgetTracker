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
				System.out.println(userName);
				insertAccount(buildNewAccountSignature());
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
				+ " \"" + "SuperKoolUser91"+ "\","
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
		else if (Integer.parseInt(balance) < 0)
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
	
//	public void testDB() throws SQLException{
//	// this is a dummy method to show how to connect to the database and execute queries
//	Connection conn = get_connection();
//	Statement stmt = conn.createStatement();
//	String sql = "SELECT * FROM Users where Username = ?";
//	PreparedStatement prepared_statement = conn.prepareStatement(sql);
//	String username = "SuperKoolUser91";
//	prepared_statement.setString(1, username);
//
//	ResultSet rs = prepared_statement.executeQuery();
//	ResultSetMetaData rsmd = rs.getMetaData();
//
//	int columnsNumber = rsmd.getColumnCount();
//
//	while (rs.next()) 
//	{
//		for (int i = 1; i <= columnsNumber; i++) 
//		{
//			if (i > 1) 
//			{
//				System.out.print(",  ");
//			}
//			String columnValue = rs.getString(i);
//			System.out.print(columnValue + " " + rsmd.getColumnName(i));
//		}
//		System.out.println("");
//	}
//	conn.close();
//}
}