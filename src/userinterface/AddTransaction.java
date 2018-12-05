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
import javax.swing.JComboBox;
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
	JTextField dateText = new JTextField(10);
	JTextField titleText = new JTextField(10);
	JTextField locationText = new JTextField(10);
	JTextField amountText = new JTextField(10);
	JComboBox<String> categoryList;
	JComboBox<String> accountList;
	JLabel [] labels = {dateLabel, titleLabel, locationLabel,amountLabel};
	JTextField [] textFields = {dateText, titleText, locationText, amountText};
	JLabel [] comboLabels;
	
	public AddTransaction(){
		dateText.setText("MM/DD/YYYY");
		middle.setLayout(new GridBagLayout());
		for (int i=0 ; i< labels.length ; i++){
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 1;
			c.gridy = i+1;
			c.gridwidth=2;
			middle.add(labels[i],c);
			GridBagConstraints d = new GridBagConstraints();
			d.fill = GridBagConstraints.HORIZONTAL;
			d.gridx = 3;
			d.gridy = i+1;
			d.gridwidth=2;
			middle.add(textFields[i],d);
			textFields[i].addActionListener(this);
		}
		//get these from the database
		String [] categoryStrings = {"Household","Food","Emergency"};
		JComboBox<String> categoryList = new JComboBox<String>(categoryStrings);
		categoryList.setSelectedIndex(0);
		categoryList.addActionListener(this);
		//get these from the database
		String [] accountStrings = {"Savings","Checking","Credit", "Cash"};
		JComboBox<String> accountList = new JComboBox<String>(accountStrings);
		accountList.setSelectedIndex(0);
		accountList.addActionListener(this);
		
		JLabel [] comboLabels = {categoryLabel, accountLabel};
		JComboBox [] catANDacctLists = {categoryList, accountList};
		
		for(int i=0; i<2;i++){
			GridBagConstraints e = new GridBagConstraints();
			e.fill = GridBagConstraints.HORIZONTAL;
			e.gridx = 1;
			e.gridy = labels.length +i+1;
			e.gridwidth=2;
			middle.add(comboLabels[i],e);
			GridBagConstraints f = new GridBagConstraints();
			f.fill = GridBagConstraints.HORIZONTAL;
			f.gridx = 3;
			f.gridy = labels.length +i+1;
			f.gridwidth=2;
			middle.add(catANDacctLists[i],f);
		}
		
		top.add(header);
		bottom.setLayout(new GridLayout(1,2));
		bottom.add(cancel);
		cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				BudgetApplet.changeScreen("Transaction Summary");
			}
		});
		bottom.add(save);
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String date = dateText.getText();
				String title = titleText.getText();
				String location = locationText.getText();
				String amount = amountText.getText();
				String category = "";
				if (categoryList.getSelectedIndex() != -1) {
					category = (String)categoryList.getItemAt(categoryList.getSelectedIndex());
				}
				String account = "";
				if (accountList.getSelectedIndex() != -1) {
					account = (String)accountList.getItemAt(accountList.getSelectedIndex());
				}
				
				// this is where you would insert into database with these values 
				System.out.println(date+title+location+amount+category+account);
				BudgetApplet.changeScreen("Transaction Summary");
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}		
}