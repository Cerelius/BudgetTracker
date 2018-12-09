package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Component;
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
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import userinterface.AddAccount.InvalidAddAccountInputException;

public class AddTransaction extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Add New Transaction");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Transaction");
	
	JLabel creditIdLabel = new JLabel ("CreditId:");
	JLabel dateLabel = new JLabel("Date:");
	JLabel titleLabel = new JLabel("Title:");
	JLabel locationLabel = new JLabel("Location:");
	JLabel categoryLabel = new JLabel("Category:");
	JLabel accountLabel = new JLabel("Account Name:");
	JLabel amountLabel = new JLabel ("Amount:");
	JLabel budgetIdLabel = new JLabel ("Budget ID:");
	static JTextField budgetIdText = new JTextField(10);
	static JTextField creditIdText = new JTextField(10);
	static JTextField dateText = new JTextField(10);
	static JTextField titleText = new JTextField(10);
	static JTextField locationText = new JTextField(10);
	static JTextField amountText = new JTextField(10);
	static JComboBox<String> categoryList;
	static JComboBox<String> accountList;
	JLabel [] labels = {creditIdLabel, dateLabel, titleLabel, locationLabel, budgetIdLabel, amountLabel};
	static JTextField [] textFields = {creditIdText, dateText, titleText, locationText, budgetIdText, amountText};
	JLabel [] comboLabels;
	
	// Build transaction interface.
	public AddTransaction(){
		dateText.setText("MM/DD/YYYY");
		middle.setLayout(new GridBagLayout());
		for (int i=0 ; i< labels.length ; i++){
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = i+1;
			c.gridwidth=2;
			middle.add(labels[i],c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 3;
			d.gridy = i+1;
			d.gridwidth=2;
			middle.add(textFields[i],d);
			textFields[i].addActionListener(this);
		}
		//get these from the database
		String [] categoryStrings = {"Household","Food","Emergency"};
		JComboBox<String> categoryList = new JComboBox<String>(categoryStrings);
		categoryList.setSelectedIndex(0);
		categoryList.addActionListener(this);
		//get these from the database
		String [] accountStrings = {"Savings","Checking","Credit", "Cash"};
		JComboBox<String> accountList = new JComboBox<String>(accountStrings);
		accountList.setSelectedIndex(0);
		accountList.addActionListener(this);
		
		JLabel [] comboLabels = {categoryLabel, accountLabel};
		JComboBox [] catANDacctLists = {categoryList, accountList};
		
		for(int i=0; i<2;i++){
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 1;
			e.gridy = labels.length +i+1;
			e.gridwidth=2;
			middle.add(comboLabels[i],e);
			GridBagConstraints f = new GridBagConstraints();
			f.fill = GridBagConstraints.HORIZONTAL;
			f.gridx = 3;
			f.gridy = labels.length +i+1;
			f.gridwidth=2;
			middle.add(catANDacctLists[i],f);
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
		// if Cancel is pressed.
		if (label == "Cancel") {
			BudgetApplet.changeScreen("Transaction Summary");
		}
		// if Save Transaction is pressed. 
		else if (label == "Save Transaction") {
			try {
				insertTransaction(buildNewTransactionSignature());
			} catch (SQLException e1) {
				//This is a generic exception that indicates there was a problem with insertAccount.
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was a problem with creating your account, please check your input and try again.");
			} catch (InvalidAddTransactionInputException e2) {
				//This means that there was a problem with the input from the user.
				e2.printImproperInput();
				e2.showErrorMessage();
			} finally {
				BudgetApplet.changeScreen("Transaction Summary");
			}
		}
	}

	public static void populate(String tranID){
		try{
			String [] tran_info = getTranInfo(tranID);
			for (int i=0; i<6; i++){
				textFields[i].setText(tran_info[i]);
			}
			System.out.println(tran_info[7]);
			for (Component i: categoryList.getComponents()){
				System.out.println(i.getName());
			}
		}
		catch (SQLException e1){
			e1.printStackTrace();
			System.out.println("error");
		}
	}
	
	public static String[] getTranInfo(String tranID )throws SQLException{
		Connection conn = get_connection();
		String sql = "SELECT CreditID, DateCreated, Title, Description, Budget_ID, Amount, Category, AccountNumber FROM Credit WHERE CreditID =?;";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, tranID);
    	ResultSet rs = prepared_statement.executeQuery();
    	rs.next();
    	String sql2 = "Select Account_Title from UserAccounts where AccountNumber = ?;";
		PreparedStatement prepared_statement2 = conn.prepareStatement(sql2);
    	prepared_statement2.setString(1, rs.getString(8));
    	ResultSet rs2 = prepared_statement2.executeQuery();
    	rs2.next();
    	String acct_name = rs2.getString(1);
 	    	 	
    	String [] tranInfo = { rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), acct_name};
    	conn.close();
		return tranInfo;
	}
	
	
	private void insertTransaction(String values) throws SQLException{
		// execute update to add account into database
		Connection conn = get_connection();
		String sql = "INSERT INTO Credit (CreditID, Username, Title, Budget_ID, Category,"
				+ " Description, Amount,AccountNumber)" + "VALUES (" + values + ")";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.executeUpdate();
		conn.close();
	}

	private String buildNewTransactionSignature() throws InvalidAddTransactionInputException{
		checkAddTransactionInput();
		
		String creditId = creditIdText.getText();
		String date = dateText.getText();
		String title = titleText.getText();
		String location = locationText.getText();
		String amount = amountText.getText();
		String budgetId = budgetIdText.getText();
		String category = categoryList.getItemAt(categoryList.getSelectedIndex());
		String accountName = accountList.getItemAt(accountList.getSelectedIndex());
		
		String signature = "\"" + creditId + "\","
				+ " \"" + userName + "\","
				+ " \"" + title + "\","
				+ " \"" + budgetId + "\","
				+ " \"" + category + "\","
				+ " \"" + location + "\","
				+ amount + ","
				+ " \"" + date + "\""
				+ " \"" + accountName + "\",";

		return signature;
	}	
	
	/** checkAddTransactionInput
	 * This method validates the input from the fields.
	 * @param values - a string that is passed to the database that contains all of the query information.  
	 * @throws inputInvalidException - There was a problem with the field information, the message defines what is wrong.  
	 */
	public void checkAddTransactionInput() throws InvalidAddTransactionInputException {
		checkCreditID();
		checkDate();
		checkTitle();
		checkLocation();
		checkAmount();
		checkBudgetID();
		checkCategory();
		checkAccountName();
	}
	
	private void checkCreditID() throws InvalidAddTransactionInputException {
		// Must not be blank
		// Must be numeric
		String creditId = creditIdText.getText();
		if (isEmpty(creditId))
			throw new InvalidAddTransactionInputException("CreditId cannot be blank");
		else if (isNotNumeric(creditId))
			throw new InvalidAddTransactionInputException("CreditId must be numeric");
	}
	
	private void checkDate() throws InvalidAddTransactionInputException {
		// Must not be blank
		// Must be MM/DD/YYYY
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
		String date = dateText.getText();
		if (isEmpty(date))
			throw new InvalidAddTransactionInputException("Date cannot be blank");
		try {
			Date t = ft.parse(date);
		} catch (ParseException e) {
			throw new InvalidAddTransactionInputException("Date format must be dd/MM/yyyy");
		}
	}

	private void checkTitle() throws InvalidAddTransactionInputException {
		String title = titleText.getText();
		if(isEmpty(title))
			throw new InvalidAddTransactionInputException("Title cannot be blank");
	}
	
	private void checkLocation() throws InvalidAddTransactionInputException {
		// Must not be blank
		if (isEmpty(locationText.getText()))
			throw new InvalidAddTransactionInputException("Location cannot be blank.");
	}

	private void checkAmount() throws InvalidAddTransactionInputException {
		// Must be positive
		// Must be numeric
		// Must not be blank
		String amount = amountText.getText();
		if (isEmpty(amount))
			throw new InvalidAddTransactionInputException("Amount cannot be blank");
		else if (isNotNumeric(amount))
			throw new InvalidAddTransactionInputException("Amount must be numeric");
		else if (Integer.parseInt(amount) < 0)
			throw new InvalidAddTransactionInputException("Amount must be postive");
	}

	private void checkBudgetID() throws InvalidAddTransactionInputException {
		// Must not be blank
		// Must be numeric
		String budgetId = budgetIdText.getText();
		if (isEmpty(budgetId))
			throw new InvalidAddTransactionInputException("budgetId cannot be blank");
		else if (isNotNumeric(budgetId))
			throw new InvalidAddTransactionInputException("budgetId must be numeric");
	}
	
	private void checkCategory() throws InvalidAddTransactionInputException {
		// Must not be blank
		String category = categoryList.getItemAt(categoryList.getSelectedIndex());
		if (isEmpty(category))
			throw new InvalidAddTransactionInputException("Category cannot be blank.");
	}

	private void checkAccountName() throws InvalidAddTransactionInputException {
		// Must not be blank
		String accountName = accountList.getItemAt(accountList.getSelectedIndex());
		if (isEmpty(accountName))
			throw new InvalidAddTransactionInputException("Account Name cannot be blank.");
	}

	/** InvalidAddAccountInputException
	 * This exception is thrown when there is invalid input in the fields.
	 */
	public class InvalidAddTransactionInputException extends Exception {
		String cause;

		public InvalidAddTransactionInputException(String cause) {
			this.cause = cause;
		}

		public void printImproperInput() {
			System.out.println(cause);
		}

		public void showErrorMessage() {
			JOptionPane.showMessageDialog(null, cause);
		}
	} 
	
	//Check if a field is empty
	private boolean isEmpty(String field) {
		if (field.length() == 0)
			return true;
		else
			return false;
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
}