package userinterface;
import java.applet.Applet;
import java.awt.Button;
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
		middle.add(new Label("*add accounts stuff here*"));
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
