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
	JLabel passLabel = new JLabel("PASSWORD:");
	JTextField username = new JTextField();
	JTextField password = new JTextField();
	JLabel repeatPassLabel = new JLabel ("REPEAT PASSWORD:");
	JTextField pass2 = new JTextField();
	JButton createButton = new JButton("   CREATE   ");
	
	public CreateLogin(){
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
		d.gridwidth=1;
		add(username,d);
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		e.gridx = 1;
		e.gridy = 2;
		e.gridwidth=1;
	    add(passLabel,e);
		GridBagConstraints f = new GridBagConstraints();
		f.fill = GridBagConstraints.HORIZONTAL;
		f.gridx = 2;
		f.gridy = 2;
		f.gridwidth=1;
		add(password,f);
		GridBagConstraints g = new GridBagConstraints();
		g.fill = GridBagConstraints.HORIZONTAL;
		g.gridx = 1;
		g.gridy = 3;
		g.gridwidth=1;
		add(repeatPassLabel,g);
		GridBagConstraints h = new GridBagConstraints();
		h.fill = GridBagConstraints.HORIZONTAL;
		h.gridx = 2;
		h.gridy = 3;
		h.gridwidth=1;
		add(pass2,h);
		GridBagConstraints i = new GridBagConstraints();
		i.fill = GridBagConstraints.HORIZONTAL;
		i.gridx = 2;
		i.gridy = 4;
		i.gridwidth=1;
		add(createButton,i);
		createButton.addActionListener(this);
		setSize(600, 400);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//if createButton is pressed 
		BudgetApplet.changeScreen("Login");
		
	}
}
