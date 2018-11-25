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

public class EditBudget extends BasicLayout implements ActionListener {


	JLabel header = new JLabel("Set Budget");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Changes");
	
	public EditBudget(){
			JPanel budget_info = new JPanel();
			budget_info.setLayout(new GridLayout(1,2));
			//for each budget category 
			Label food = new Label("Food");
			TextField food_input = new TextField();
			budget_info.add(food);
			budget_info.add(food_input);
			middle.add(budget_info);
		top.add(header);
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
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if save is pressed 
		else if (label == "Save Changes"){
			BudgetApplet.changeScreen("Budget Summary");
		}
		
	}
}
