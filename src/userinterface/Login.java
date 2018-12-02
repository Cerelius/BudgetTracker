package userinterface;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Login extends JPanel implements ActionListener{
	JLabel userLabel = new JLabel("USERNAME:");
	JLabel passLabel = new JLabel("PASSWORD:");
	JTextField username = new JTextField();
	JTextField password = new JTextField();
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
			BudgetApplet.changeScreen("Budget Summary");}
		//if addNew is pressed 
		else if (label == "NEW ACCOUNT"){
		BudgetApplet.changeScreen("Create Login");
		}
		
		
	}
}
