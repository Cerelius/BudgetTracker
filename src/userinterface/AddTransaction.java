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

public class AddTransaction extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Add New Transaction");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Transaction");
	
	public AddTransaction(){
		//use db transaction table to create labels
			JPanel input_example = new JPanel();
			input_example.setLayout(new GridLayout(3,2,10,10));
			Label date = new Label("Date:");
			TextField date_input = new TextField("10/30/1997");
			input_example.add(date);
			input_example.add(date_input);
			Label loc = new Label("Location:");
			TextField loc_input = new TextField("Taco Bell");
			input_example.add(loc);
			input_example.add(loc_input);
			Label pmt = new Label("Payment Account:");
			TextField pmt_input = new TextField("Credit");
			input_example.add(pmt);
			input_example.add(pmt_input);
			middle.add(input_example);	
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
		//if cancel is pressed 
		if (label == "Cancel"){
			BudgetApplet.changeScreen("Transaction Summary");
		}
		//if save is pressed
		if (label == "Save Transaction"){
			BudgetApplet.changeScreen("Transaction Summary");
		}
		
	}
}
