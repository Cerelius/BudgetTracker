import java.applet.Applet;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BudgetApplet {
	
	//create screen panel and all screen classes
	static JPanel screens;
	JPanel loginScreen = new Login();
	JPanel budgetSummary = new BudgetSummary();
	JPanel accountsSummary = new AccountsSummary();
	JPanel addAccount = new AddAccount();
	JPanel createLogin = new CreateLogin();
	JPanel transactionSummary = new TransactionSummary();
	JPanel addTransaction = new AddTransaction();
	JPanel editBudget = new EditBudget();
	
	public void addContentToPane(Container pane){
		//create add screens to the screen Panel
		screens = new JPanel(new CardLayout());
		screens.add(loginScreen, "Login");
		screens.add(budgetSummary, "Budget Summary");
		screens.add(accountsSummary, "Accounts Summary");
		screens.add(addAccount, "Add Account");
		screens.add(createLogin, "Create Login");
		screens.add(transactionSummary, "Transaction Summary");
		screens.add(addTransaction, "Add Transaction");
		screens.add(editBudget,"Edit Budget");
		//add screen panel to window pane
		pane.add(screens, BorderLayout.CENTER);
	}

	public static void changeScreen(String screen_name){
		//method to change which screen is in view
		CardLayout cl = (CardLayout)(screens.getLayout());
		cl.show(screens, screen_name);
	}
	private static void createAndShowGUI(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BudgetApplet bud = new BudgetApplet();
		bud.addContentToPane(frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		createAndShowGUI();
	}

}
