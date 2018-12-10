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
import java.util.ArrayList;
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
	static JTextField [] textFields ={creditIdText, dateText, titleText, locationText, budgetIdText, amountText};
	JLabel [] comboLabels;
	static ArrayList<Category> categories = new ArrayList<>();
	ArrayList<Account> accounts = new ArrayList<>();
	static ArrayList<Category> cats = new ArrayList<>();
	static ArrayList<Account> acctS = new ArrayList<>();
	static String [] catS;
	ArrayList<Category> catNames = new ArrayList<>();	
	String catPicked = "";
	String acctPicked = "";
	
	// Build transaction interface.
	public AddTransaction(){
		dateText.setText("YYYY/MM/DD");
		callCreateMiddle();
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
				String credit = creditIdText.getText();
				if (creditExists(credit) == true){
					//ask if they want to override
					Object [] options = {"Yes","No"};
					int n = JOptionPane.showOptionDialog(null, "Transaction " + credit+ " already exists, do you want to overwrite it?", 
							"Delete Account", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, options, options[0]);
					//if user selects YES, delete account and all linked transactions and then send message that account has been deleted
					if (n == JOptionPane.YES_OPTION){
						//delete old instance of the transaction 
						updateTransaction();
						}
					else if(n == JOptionPane.NO_OPTION){
						//insert new transaction
						insertTransaction(buildNewTransactionSignature());
					}
				}
				else{
					insertTransaction(buildNewTransactionSignature());
				}
			} catch (SQLException e1) {
				//This is a generic exception that indicates there was a problem with insertAccount.
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was a problem with creating your account, please check your input and try again.");
			} catch (InvalidAddTransactionInputException e2) {
				//This means that there was a problem with the input from the user.
				e2.printImproperInput();
				e2.showErrorMessage();
			} catch(Exception otherException){ 
				System.out.println(otherException.getMessage());
			}
			finally {
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
			System.out.println(tran_info[6]);
			System.out.println(getCompIndex(tran_info[6]));
			categoryList.setSelectedIndex(getCompIndex(tran_info[6]));
		}
		catch (SQLException e1){
			e1.printStackTrace();
			System.out.println("error");
		}
	}
	
	public static int getCompIndex(String categoryName){
		for (Category i : categories){
			System.out.println(categoryName+ "  "+ i.getCat());
			if (categoryName.equals(i.getCat())){
				return categories.indexOf(i);
			}
		}
		return 0;
	}
	
	public boolean creditExists(String credit) throws SQLException{
		Connection conn = get_connection();
		String sql = "Select Title from  Credit where CreditID = ? and Username = ?";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setString(1, credit);
		prepared_statement.setString(2, userName);
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
	
	/** deleteTransaction
	 * this method queries the database to delete a transaction base don the creditID and username of user
	 */
	public void updateTransaction() throws SQLException{
		Connection conn = get_connection();
    	//delete query
    	String sql = "UPDATE Credit SET Title = ?, Budget_ID = ?, Category = ?, Description = "
    			+ "?, Amount = ? WHERE CreditID = ? AND Username = ?;";
    	//String sql = "DELETE FROM Credit WHERE CreditID = ? AND Username = ?;";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	try {
			String [] updateValues = buildUpdateTransactionSignature();
	    	prepared_statement.setString(1, updateValues[0]);
	    	prepared_statement.setString(2, updateValues[1]);
	    	prepared_statement.setString(3, updateValues[2]);
	    	prepared_statement.setString(4, updateValues[3]);
	    	prepared_statement.setString(5, updateValues[4]);
	    	prepared_statement.setString(6, updateValues[5]);
	    	prepared_statement.setString(7, updateValues[6]);
	    	prepared_statement.executeUpdate();
		} catch (InvalidAddTransactionInputException e) {
			e.printStackTrace();
		} finally{
			conn.close();
		}	
	}
	
	public ArrayList<Category> getCategory() throws SQLException{
		Connection conn = get_connection();
		String sql = "Select Category, Budget_ID, Username from Category where Username = ? ";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, userName);
    	ResultSet rs = prepared_statement.executeQuery();
    	categories.clear();
    	while(rs.next()){
    		//category name, budget_id, username 
    		categories.add(new Category(rs.getString(1), rs.getString(2), rs.getString(3)));
    	}
    	conn.close();
    	return categories;
	}
	
	public ArrayList<Account> getAccount() throws SQLException{
		Connection conn = get_connection();
		String sql = "Select AccountNumber, RoutingNum, Account_Title from UserAccounts where Username = ? ";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	prepared_statement.setString(1, userName);
    	ResultSet rs = prepared_statement.executeQuery();
    	accounts.clear();
    	while(rs.next()){
    		String acct_num = rs.getString(1);
    		String rout_num = rs.getString(2);
    		String acct_string = rs.getString(3);
    		Account newAccount = new Account(acct_num, rout_num, acct_string);
    		accounts.add(newAccount);
    	}
    	conn.close();
    	return accounts;
	}
	
	
	public void callCreateMiddle(){
		middle.setLayout(new GridBagLayout());
		middle.removeAll();
		try{
			ArrayList<Category> cats= getCategory();
			ArrayList<Account> accts = getAccount();
			createMiddle(cats,accts);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
	}
	
	public void createMiddle(ArrayList<Category> cats, ArrayList<Account> accts){
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
		//get categories from database 
		//JComboBox<String> categoryList;
		ArrayList<String> catNames =new ArrayList<>();
		try{
			catNames.clear();
			for (Category c: cats){
				catNames.add(c.getCat());
			}
			int num = catNames.size();
			catS = new String[num];
			for( int i=0 ; i<num; i++){
				catS[i] = catNames.get(i);
			}
			categoryList = new JComboBox<String>(catS);
			categoryList.setSelectedIndex(0);
			categoryList.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JComboBox cb = (JComboBox)e.getSource();
					catPicked = (String)cb.getSelectedItem();	
				}
			});
		}
		catch(java.lang.IllegalArgumentException e2){
			String [] categoryStrings = {"Household","Food","Emergency"};
			categoryList = new JComboBox<String>(categoryStrings);
			categoryList.setSelectedIndex(0);
			categoryList.addActionListener(this);
		}

		
		//get categories from database 
		//JComboBox<String> accountList;
		ArrayList<String> acctNames =new ArrayList<>();
		try{
			acctNames.clear();
			for (Account a: accts){
				acctNames.add(a.getName());
			}
			int num = acctNames.size();
			String [] acctS = new String[num];
			for( int i=0 ; i<num; i++){
				acctS[i] = acctNames.get(i);
			}
			accountList = new JComboBox<String>(acctS);
			accountList.setSelectedIndex(0);
			accountList.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JComboBox cb = (JComboBox)e.getSource();
					acctPicked = (String)cb.getSelectedItem();	
				}
			});
		}
		catch(java.lang.IllegalArgumentException e2){
			String [] accountStrings = {"Household","Food","Emergency"};
			accountList = new JComboBox<String>(accountStrings);
			accountList.setSelectedIndex(0);
			accountList.addActionListener(this);
		}
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
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO Credit (CreditID, DateCreated, Username, Title, Budget_ID, Category,"
				+ " Description, Amount,AccountNumber)" + "VALUES (" + values + ")";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.executeUpdate();
		conn.close();
	}

	private String [] buildUpdateTransactionSignature() throws InvalidAddTransactionInputException{
		checkAddTransactionInput();
		
		String credit = creditIdText.getText();
		String budget = budgetIdText.getText();
		String date = dateText.getText();
		String title = titleText.getText();
		String location = locationText.getText();
		String amount = amountText.getText();
		String category = catPicked;
		String accountName = "";
		try{
			accountName = getAccountNum(acctPicked);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		String [] updateVals = {title, budget, category, location, amount, credit, userName};
		return updateVals;
	}
	
	private String buildNewTransactionSignature() throws InvalidAddTransactionInputException{
		checkAddTransactionInput();
		
		String credit = creditIdText.getText();
		String budget = budgetIdText.getText();
		String date = dateText.getText();
		String title = titleText.getText();
		String location = locationText.getText();
		String amount = amountText.getText();
		String category = catPicked;
		String accountName = "";
		try{
			accountName = getAccountNum(acctPicked);
		}catch(SQLException e1){
			e1.printStackTrace();
		}
		
		
		String signature = " \""+ credit+"\","
				+ " \"" + date + "\","
				+ " \"" +	userName+ "\","
				+ " \"" + title + "\","
				+ " \""+ budget +"\","
				+ " \"" + category+ "\","
				+ " \"" + location + "\","
				+ amount + ","
				+ " \""+ accountName +"\"";

		return signature;
	}	
	
	public String getAccountNum(String accountName) throws SQLException{
		Connection conn = get_connection();
		String sql = "Select AccountNumber from UserAccounts where Account_Title = ? and Username = ?";
		PreparedStatement pm = conn.prepareStatement(sql);
		pm.setString(1, accountName);
		pm.setString(2, userName);
		System.out.println(pm);
		ResultSet rs = pm.executeQuery();
		rs.next();
		return rs.getString(1);
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
		String date = dateText.getText();
		if (isEmpty(date))
			throw new InvalidAddTransactionInputException("Date cannot be blank");
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
		else if (Double.parseDouble(amount) < 0)
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
	
	public class Category{
		private String category;
		private String budgetID;
		private String username;
		
		public Category(String cat, String bud, String un){
			this.category = cat;
			this.budgetID = bud;
			this.username = un;
		}
		public String getCat(){
			return category;
		}
		public String getBUd(){
			return budgetID;
		}
		public String getUN(){
			return username;
		}
		
	}
	
	public class Account{
		private String account_number;
		private String routing_number;
		private String name;
		
		public Account(String acct_num, String rout_num, String acct_name){
			this.account_number = acct_num;
			this.routing_number = rout_num;
			this.name = acct_name;
		}
		public String getNum(){
			return account_number;
		}
		public String getRout(){
			return routing_number;
		}
		public String getName(){
			return name;
		}
		
	}
}