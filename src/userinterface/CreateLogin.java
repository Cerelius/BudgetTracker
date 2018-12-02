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
		String userName = userNameText.getText();
		String firstName = firstNameText.getText();
		String lastName = lastNameText.getText();
		String middleInitial = middleInitialText.getText();
		String email = emailText.getText();
		String password = passwordText.getText();
		String pass2 = pass2Text.getText();
		BudgetApplet.changeScreen("Login");
		
	}
}
