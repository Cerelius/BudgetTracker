package userinterface;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BudgetSummary extends BasicLayout implements ActionListener {
	
	JLabel header = new JLabel("Budget Summary");
	JButton logout = new JButton("Logout");
	JButton transSum = new JButton("Transaction Summary");
	JButton addTrans = new JButton("Add Transaction");
	JButton acctSum = new JButton("Accounts Summary");
	JButton editBud = new JButton("Edit Budget");
	
	public BudgetSummary(){
		top.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy= 0;
		c.weightx = 0.8;
		top.add(header,c);
		GridBagConstraints d = new GridBagConstraints();
		d.gridx=1;
		d.gridy= 0;
		d.weightx = 0.2;
		top.add(logout);
		logout.addActionListener(this);
		middle.add(new Label("*insert table here*"));
		bottom.setLayout(new GridLayout(1,4));
		bottom.add(transSum);
		transSum.addActionListener(this);
		bottom.add(addTrans);
		addTrans.addActionListener(this);
		bottom.add(acctSum);
		acctSum.addActionListener(this);
		bottom.add(editBud);
		editBud.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if transSum is pressed 
		if (label == "Transaction Summary"){
			BudgetApplet.changeScreen("Transaction Summary");
		}
		//if addTrans is pressed
		else if (label == "Add Transaction"){
			BudgetApplet.changeScreen("Add Transaction");
		}
		//if acctSum is pressed
		else if (label == "Accounts Summary"){
			BudgetApplet.changeScreen("Accounts Summary");
		}
		//if editBud is pressed
		else if (label == "Edit Budget"){
			BudgetApplet.changeScreen("Edit Budget");
		}
		//if logout is pressed 
		else if (label == "Logout"){
			BudgetApplet.changeScreen("Login");
		}

		
	}

}
