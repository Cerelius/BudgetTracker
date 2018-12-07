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


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateLogin extends JPanel implements ActionListener {
	JLabel userLabel = new JLabel("USERNAME:");
	JLabel fNLabel = new JLabel("FIRST NAME:");
	JLabel lNLabel = new JLabel("LAST NAME:");
	JLabel mNLabel = new JLabel("MIDDLE INITIAL:");
	JLabel emailLabel = new JLabel("EMAIL:");
	JLabel passLabel = new JLabel("PASSWORD:");
	JLabel repeatPassLabel = new JLabel ("REPEAT PASSWORD:");
	JTextField userNameText = new JTextField();
	JTextField firstNameText = new JTextField();
	JTextField lastNameText = new JTextField();
	JTextField middleInitialText = new JTextField();
	JTextField emailText = new JTextField();
	JTextField passwordText = new JTextField();
	JTextField pass2Text = new JTextField();
	JButton createButton = new JButton("   CREATE   ");
	JLabel [] labels = {userLabel,fNLabel, lNLabel, mNLabel, emailLabel, passLabel, repeatPassLabel };
	JTextField [] textFields = {userNameText, firstNameText, lastNameText, middleInitialText, emailText, passwordText, pass2Text};
	
	public CreateLogin(){
		setLayout(new GridBagLayout());
		for (int i=0 ; i< labels.length ; i++){
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = i+1;
			c.gridwidth=1;
			add(labels[i],c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 2;
			d.gridy = i+1;
			d.gridwidth=1;
			add(textFields[i],d);
			textFields[i].addActionListener(this);
		}
		GridBagConstraints i = new GridBagConstraints();
		i.fill = GridBagConstraints.HORIZONTAL;
		i.gridx = 2;
		i.gridy = labels.length +1;
		i.gridwidth=1;
		add(createButton,i);
		createButton.addActionListener(this);
		setSize(600, 400);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if createButton is pressed
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		
		String userName = userNameText.getText();
		String firstName = firstNameText.getText();
		String lastName = lastNameText.getText();
		String middleInitial = middleInitialText.getText();
		String email = emailText.getText();
		String password = passwordText.getText();
		String pass2 = pass2Text.getText();
		
		if (label == "   CREATE   "){	

			try {
				insertUser(buildNewUserSignature());
			} catch (SQLException e1) {
				//This is a generic exception that indicates there was a problem with insertAccount.
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "There was a problem with creating your login, please check your input and try again.");
			} catch (InvalidAddUserInputException e2) {
				//This means that there was a problem with the input from the user.
				e2.printImproperInput();
				e2.showErrorMessage();
			} finally {
				//Return to the account summary screen.
				BudgetApplet.changeScreen("Login");
			}
		}
		
		
		
	}
	
	public void insertUser(String values) throws SQLException{
		// execute update to add account into database
		Connection conn = BasicLayout.get_connection();
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO Users (Username, First_name, Last_name, Middle_initial, Email_address, Password)"
				+ "VALUES (" + values + ")";
		System.out.println(sql);
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.executeUpdate();
		conn.close();
	}
	
	public String buildNewUserSignature() throws InvalidAddUserInputException {
		checkAddUserInput();

		String userName = userNameText.getText();
		String firstName = firstNameText.getText();
		String lastName = lastNameText.getText();
		String middleInitial = middleInitialText.getText();
		String email = emailText.getText();
		String password = passwordText.getText();
		String pass2 = pass2Text.getText();

		String signature = "\"" + userName+ "\","
				+ " \"" +	firstName+ "\","
				+ " \"" + lastName+ "\","
				+ " \"" + middleInitial+ "\","
				+ " \"" + email+ "\","
				+ " \"" + password + "\"";

		return signature;
	}
	
	public void checkAddUserInput() throws InvalidAddUserInputException {
		checkUsername();
		checkFirst();
		checkLast();
		checkEmail();
		checkPass();
	}

	private void checkUsername() throws InvalidAddUserInputException {
		// No non-alphanumeric characters
		// Must not be blank
		String user = userNameText.getText();
		if (isEmpty(user))
			throw new InvalidAddUserInputException("User Name cannot be blank");
		else if (isNotAlphanumeric(user))
			throw new InvalidAddUserInputException("User Name must be alphanumeric");
	}

	private void checkFirst() throws InvalidAddUserInputException {
		// Must not be blank
		if (isEmpty(firstNameText.getText()))
			throw new InvalidAddUserInputException("First Name cannot be blank");
	}

	private void checkLast() throws InvalidAddUserInputException {
		// Must not be blank		
		if (isEmpty(lastNameText.getText()))
			throw new InvalidAddUserInputException("Last Name cannot be blank");
	}

	private void checkEmail() throws InvalidAddUserInputException {
		// cannot be blank
		// Must have @ and .
		String email = emailText.getText();
		if (isEmpty(email))
			throw new InvalidAddUserInputException("Email cannot be blank.");
		else if (email.contains("@") == false || email.contains(".") == false)
			throw new InvalidAddUserInputException("Invalid Email Format");
	}

	private void checkPass() throws InvalidAddUserInputException {
		// Cannot be blank
		// Must be same
		String password = passwordText.getText();
		String pass2 = pass2Text.getText();
		if (isEmpty(password) || isEmpty(pass2))
			throw new InvalidAddUserInputException("Password cannot be blank.");
		else if (password.equals(pass2) == false)
			throw new InvalidAddUserInputException("Password fields do not match");
	}

	// Checks if a string is alphanumeric or not.
	private boolean isNotAlphanumeric(String str) { 
		for (int i = 0; i < str.length(); i++) {
        	if (Character.isDigit(str.charAt(i)) == false && Character.isLetter(str.charAt(i)) == false)
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
	
	public class InvalidAddUserInputException extends Exception {
		String cause;

		public InvalidAddUserInputException(String cause) {
			this.cause = cause;
		}

		public void printImproperInput() {
			System.out.println(cause);
		}

		public void showErrorMessage() {
			JOptionPane.showMessageDialog(null, cause);
		}
	} 
}
