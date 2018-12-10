package userinterface;
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

import userinterface.AccountsSummary.Account;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

public class BudgetApplet {
	//create screen panel and all screen classes
	static JPanel screens;
	static Login loginScreen = new Login();
	static BudgetSummary budgetSummary = new BudgetSummary();
	static AccountsSummary accountsSummary = new AccountsSummary();
	static AddAccount addAccount = new AddAccount();
	static CreateLogin createLogin = new CreateLogin();
	static TransactionSummary transactionSummary = new TransactionSummary();
	static AddTransaction addTransaction = new AddTransaction();
	static EditBudget editBudget = new EditBudget();
	
	public void addContentToPane(Container pane){
		//add screens to the screen Panel in a card layout 
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
		if (screen_name == "Accounts Summary"){
			accountsSummary.callCreateMiddle();
		}
		else if (screen_name == "Transaction Summary"){
			transactionSummary.callCreateMiddle();
		}
		else if (screen_name == "Add Transaction"){
			addTransaction.callCreateMiddle();
		}
		else if (screen_name == "Login"){
			loginScreen.username.setText("");
			loginScreen.password.setText("");
		}
		cl.show(screens, screen_name);
	}
	
	/** createAndShowGUI
	 * this method creates a frame with the budget applet added to the frame and displays
	 */
	private static void createAndShowGUI(){
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BudgetApplet bud = new BudgetApplet();
		bud.addContentToPane(frame.getContentPane());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		createAndShowGUI();
	}

}
