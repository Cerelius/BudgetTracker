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
	
	JLabel dateLabel = new JLabel("Date:");
	JLabel titleLabel = new JLabel("Title:");
	JLabel locationLabel = new JLabel("Location:");
	JLabel categoryLabel = new JLabel("Category:");
	JLabel accountLabel = new JLabel("Account Name:");
	JLabel amountLabel = new JLabel ("Amount:");
	JTextField dateText = new JTextField(10);
	JTextField titleText = new JTextField(10);
	JTextField locationText = new JTextField(10);
	JTextField amountText = new JTextField(10);
	JComboBox<String> categoryList;
	JComboBox<String> accountList;
	JLabel [] labels = {dateLabel, titleLabel, locationLabel,amountLabel};
	JTextField [] textFields = {dateText, titleText, locationText, amountText};
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

	private void insertTransaction(String values) throws SQLException{
		// execute update to add account into database
		Connection conn = get_connection();
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO Credit VALUES ('CreditID', 'Username', 'Title', 'Budget_ID', 'Category',"
				+ " 'Description', 'Amount', 'DateCreated', 'AccountNumber')" + "VALUES (" + values + ")";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.executeUpdate();
		conn.close();
	}

	private String buildNewTransactionSignature() throws InvalidAddTransactionInputException{
		checkAddTransactionInput();

		JTextField dateText = new JTextField(10);
		JTextField titleText = new JTextField(10);
		JTextField locationText = new JTextField(10);
		JTextField amountText = new JTextField(10);
		
		String date = dateText.getText();
		String title = titleText.getText();
		String location = locationText.getText();
		String amount = amountText.getText();
		String category = "Food";
		String accountName = "accountName";
		
		String signature = "\"" + date + "\","
				+ " \"" +	title+ "\","
				+ " \"" + location + "\","
				+ " \"" + amount + "\","
				+ " \"" + "SuperKoolUser91"+ "\","
				+ " \"" + amount + "\","
				+ " \"" + accountName + "\"";

		return signature;
	}	
	
	/** checkAddTransactionInput
	 * This method validates the input from the fields.
	 * @param values - a string that is passed to the database that contains all of the query information.  
	 * @throws inputInvalidException - There was a problem with the field information, the message defines what is wrong.  
	 */
	public void checkAddTransactionInput() throws InvalidAddTransactionInputException {
		checkDate();
		checkTitle();
		checkLocation();
		checkAmount();
		checkCategory();
		checkAccountName();
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

	private void checkCategory() throws InvalidAddTransactionInputException {
		// Must not be blank
		// TODO: Add category String.
		String category = " ";
		if (isEmpty(category))
			throw new InvalidAddTransactionInputException("Category cannot be blank.");
	}

	private void checkAccountName() throws InvalidAddTransactionInputException {
		// Must not be blank
		// TODO: Add account name string
		String accountName = " ";
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