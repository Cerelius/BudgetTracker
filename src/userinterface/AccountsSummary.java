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

public class AccountsSummary extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Payment Accounts");
	JButton addAcct = new JButton("Add Account");
	JButton budSum = new JButton("Budget Summary");
	
	public AccountsSummary(){
		top.add(header);
		// put this in loop and do it for each account in DB
			JLabel acct_info = new JLabel("Savings	$2000");
			JButton edit_button = new JButton("Edit");
			JButton delete_button = new JButton("Delete");
			edit_button.addActionListener(this);
			JPanel acct = new JPanel();
			acct.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 4;
			acct.add(acct_info, c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 5;
			d.gridy = 1;
			d.gridwidth = 1;
			acct.add(edit_button, d);
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 6;
			e.gridy = 1;
			e.gridwidth = 1;
			acct.add(delete_button, e);
			middle.add(acct);
		
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(budSum);
		budSum.addActionListener(this);
		bottom.add(addAcct);
		addAcct.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if budSum is pressed
		if (label == "Budget Summary"){
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if addAcct is pressed 
		if (label == "Add Account"){
			BudgetApplet.changeScreen("Add Account");
		}
		
	}
}
