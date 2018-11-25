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

public class AddAccount extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Add Account");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Account");
	
	public AddAccount(){
		// for all account info in db, create JPanel
			JPanel input_example = new JPanel();
			input_example.setLayout(new GridLayout(1,2));
			Label name = new Label("Account Name:");
			TextField name_input = new TextField("Credit");
			input_example.add(name);
			input_example.add(name_input);
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
			BudgetApplet.changeScreen("Accounts Summary");
		}
		//if save is pressed 
		if (label == "Save Account"){
			BudgetApplet.changeScreen("Accounts Summary");
		}
		
	}
}
