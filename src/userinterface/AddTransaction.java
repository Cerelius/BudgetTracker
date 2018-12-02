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

public class AddTransaction extends BasicLayout implements ActionListener{

	JLabel header = new JLabel("Add New Transaction");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Transaction");
	
	JLabel dateLabel = new JLabel("Date:");
	JLabel titleLabel = new JLabel("Title:");
	JLabel locationLabel = new JLabel("Location:");
	JLabel categoryLabel = new JLabel("Category:");
	JLabel accountLabel = new JLabel("Account Name:");
	JLabel amountLabel = new JLabel ("Amount:");
	JLabel frequencyLabel = new JLabel("Frequency:");
	JTextField dateText = new JTextField(10);
	JTextField titleText = new JTextField(10);
	JTextField locationText = new JTextField(10);
	JTextField categoryText = new JTextField(10);
	JTextField accountText = new JTextField(10);
	JTextField amountText = new JTextField(10);
	JTextField frequencyText = new JTextField(10);
	JLabel [] labels = {dateLabel, titleLabel, locationLabel, categoryLabel, accountLabel,amountLabel, frequencyLabel};
	JTextField [] textFields = {dateText, titleText, locationText, categoryText, accountText, amountText, frequencyText};
	
	public AddTransaction(){
		middle.setLayout(new GridBagLayout());
		for (int i=0 ; i< labels.length ; i++){
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = i+1;
			c.gridwidth=1;
			middle.add(labels[i],c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 2;
			d.gridy = i+1;
			d.gridwidth=2;
			middle.add(textFields[i],d);
			textFields[i].addActionListener(this);
		}
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
			String date = dateText.getText();
			String title = titleText.getText();
			String location = locationText.getText();
			String category = categoryText.getText();
			String account = categoryText.getText();
			String amount = categoryText.getText();
			String frequency = frequencyText.getText();
			BudgetApplet.changeScreen("Transaction Summary");
		}
		
		
	}
}
