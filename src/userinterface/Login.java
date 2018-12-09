import java.applet.Applet;
import java.awt.Button;
import java.awt.CardLayout;
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
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.sql.SQLException;
//import BasicLayout;



public class Login extends JPanel implements ActionListener{
	JLabel userLabel = new JLabel("USERNAME:");
	JLabel passLabel = new JLabel("PASSWORD:");
	JTextField username = new JTextField();
	JTextField password = new JPasswordField();
	JButton loginButton = new JButton("LOGIN");
	JButton addNew = new JButton("NEW ACCOUNT");
	
	public Login(){
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth=1;
		add(userLabel,c);
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL;
		d.gridx = 2;
		d.gridy = 1;
		d.gridwidth = 1;
		add(username,d);
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		e.gridx = 1;
		e.gridy = 2;
		e.gridwidth = 1;
		add(passLabel,e);
		GridBagConstraints f = new GridBagConstraints();
		f.fill = GridBagConstraints.HORIZONTAL;
		f.gridx = 2;
		f.gridy = 2;
		f.gridwidth = 1;
		add(password,f);
		loginButton.addActionListener(this);
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 1;
		g.gridy = 3;
		add(loginButton,g);
		GridBagConstraints h = new GridBagConstraints();
		h.fill = GridBagConstraints.HORIZONTAL;
		h.gridx = 2;
		h.gridy = 3;
		add(addNew,h);
		loginButton.addActionListener(this);
		addNew.addActionListener(this);
		setSize(600, 400);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if login is pressed 
		if (label == "LOGIN"){
			String user = username.getText();
			String pass = password.getText();
			
			try {
				boolean success = doLogin(buildNewLoginSignature());
				if  (success == false) {
					JOptionPane.showMessageDialog(null, "Invalid Username and/or Password");
				}
				else if (success){
					BasicLayout.setUserName(user);
					BudgetApplet.changeScreen("Budget Summary");
				}
			} catch (SQLException e1) {
				//This is a generic exception that indicates there was a problem with the login.
				e1.printStackTrace();
				JOptionPane.showMessageDialog(null, "Invalid Username and/or Password");
			} catch (InvalidLoginException e2) {
				//This means that there was a problem with the input from the user.
				e2.printImproperInput();
				e2.showErrorMessage();
			} finally {
				//Return to the login screen.
			}
			
			
		}
		
		//if addNew is pressed 
		if (label == "NEW ACCOUNT"){
		BudgetApplet.changeScreen("Create Login");
		}
	}
	
	public boolean doLogin(String[] values) throws SQLException{
		// execute update to add account into database
		Connection conn = BasicLayout.get_connection();
		Statement stmt = conn.createStatement();
		String sql = "SELECT 'UserCorrect' FROM Users WHERE Username = ? AND Password = ?";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setString(1, values[0]);
		prepared_statement.setString(2, values[1]);
		ResultSet rs =prepared_statement.executeQuery();
		rs.next();
		try{
			String r = rs.getString(1);
			conn.close();
			return true;
		} catch (java.sql.SQLException E1){
			conn.close();
			return false;
		}
		
		
		
	}
	
	public String [] buildNewLoginSignature() throws InvalidLoginException {
		checkLoginInput();

		String user = username.getText();
		String pass = password.getText();

		String[] signature = {user,pass};

		return signature;
	}
	
	private void checkLoginInput() throws InvalidLoginException {
		// Must not be blank
		if ( isEmpty(username.getText()) || isEmpty(password.getText()) )
			throw new InvalidLoginException("Username and password must not be blank");
	}
	
	public class InvalidLoginException extends Exception {
		String cause;

		public InvalidLoginException(String cause) {
			this.cause = cause;
		}

		public void printImproperInput() {
			System.out.println(cause);
		}

		public void showErrorMessage() {
			JOptionPane.showMessageDialog(null, cause);
		}
	}
	
	private boolean isEmpty(String field) {
		if (field.length() == 0)
			return true;
		else
			return false;
	}
}