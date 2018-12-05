package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
public class TransactionSummary extends BasicLayout implements ActionListener {

	JLabel header = new JLabel("Transactions");
	JButton addTrans = new JButton("Add Transaction");
	JButton budSum = new JButton("Budget Summary");
	
	public TransactionSummary(){
		top.add(header);
		// loop to do this for each transaction 
			JLabel trans_info = new JLabel("11/11/18 Chipotle $21.25 Credit");
			JButton edit_button = new JButton("Edit");
			JButton delete_button = new JButton("Delete");
			edit_button.addActionListener(this);
			JPanel trans = new JPanel();
			trans.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = 1;
			c.gridwidth = 4;
			trans.add(trans_info, c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 5;
			d.gridy = 1;
			d.gridwidth = 1;
			trans.add(edit_button, d);
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 6;
			e.gridy = 1;
			e.gridwidth = 1;
			trans.add(delete_button, e);
		middle.add(trans);
		bottom.add(budSum);
		budSum.addActionListener(this);
		bottom.add(addTrans);	
		addTrans.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if budSum is pressed 
		if (label == "Budget Summary"){
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if addTrans is pressed 
		else if (label == "Add Transaction"){
			BudgetApplet.changeScreen("Add Transaction");
		}
		else if (label == "Edit"){
			//other if statement to get info about transaction
			BudgetApplet.changeScreen("Add Transaction");
			//other stuff to make it edit not add
		}
		else if (label == "Delete"){
			//do stuff, maybe ask if they are sure that they want to delete this transaction
		}
		
	}
}
