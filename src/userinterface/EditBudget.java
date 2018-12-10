package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

public class EditBudget extends BasicLayout implements ActionListener 
{
	TextField add_category = new TextField();
	TextField add_amount = new TextField();
	JLabel errors = new JLabel();
	JLabel category_label = new JLabel("Category: ");
	JLabel amount_label = new JLabel("Amount: ");
	
	JLabel header = new JLabel("Set Budget");
	JButton add = new JButton("Add New Budget");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Changes");
	JButton refresh = new JButton("Refresh");
	
	ArrayList<TextField> textfields = new ArrayList<TextField>();
	ArrayList<Integer> ids = new ArrayList<Integer>();
	ArrayList<String> categories = new ArrayList<String>();
	
	
	public void create_budgets(JPanel panel) throws SQLException
	{
		Connection conn = get_connection();
		
		String sql = "SELECT Budget_ID, Category, Amount FROM Category where Username = ?";
		
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setString(1, getUserName());

    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	int rows = rs.last() ? rs.getRow() : 0;
    	int columns = rsmd.getColumnCount();
    	rs.beforeFirst();
    	
    	panel.setLayout(new GridLayout(rows, columns));
    	
    	while (rs.next())
    	{
        	int id = rs.getInt("Budget_ID");
        	Label category = new Label(rs.getString("Category"));
        	
        	TextField category_input = new TextField();
        	category_input.setText(rs.getString("Amount"));
        
        	textfields.add(category_input);
        	ids.add(id);
        	categories.add(rs.getString("Category"));
        	panel.add(category);
        	panel.add(category_input);
        	
    	}
    	conn.close();
	}
	
	public void add_budget(String category, float amount) throws SQLException
	{
		Connection conn = get_connection();
		String sql0 = "select max(Budget_ID) from Budgets";
		String sql1 = "INSERT INTO Budgets VALUES (?, ?, ?)";
		String sql2 = "INSERT INTO Category Values( ?, ?, ?, ?)";

		PreparedStatement prepared_statement = conn.prepareStatement(sql0);
		ResultSet rs = prepared_statement.executeQuery();
		rs.first();
		int id = rs.getInt(1) + 1;
		
		prepared_statement = conn.prepareStatement(sql1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		prepared_statement.setInt(1, id);
		prepared_statement.setString(2, dateFormat.format(date));
		prepared_statement.setString(3, getUserName());
		prepared_statement.executeUpdate();
		
		prepared_statement = conn.prepareStatement(sql2);
		prepared_statement.setString(1, category);
		prepared_statement.setInt(2, id);
		prepared_statement.setString(3, getUserName());
		prepared_statement.setFloat(4, amount);
    	prepared_statement.executeUpdate();
    	conn.close();
    	
	}
	
	public void modify_budget(String category, int id, Float amount) throws SQLException
	{
		Connection conn = get_connection();
		String sql = "UPDATE Category SET Amount = ? where Category = ? and Budget_ID = ? and Username = ?";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setFloat(1, amount);
		prepared_statement.setString(2, category);
		prepared_statement.setInt(3, id);
		prepared_statement.setString(4, getUserName());
		//System.out.println(prepared_statement);
    	prepared_statement.executeUpdate();
    	conn.close();
	}
	
	public void update_ui()
	{
		top.removeAll();
		middle.removeAll();
		bottom.removeAll();
		create_ui();
	}
	
	public void create_ui()
	{
		JPanel budget_info = new JPanel();
		
		try 
		{
			create_budgets(budget_info);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		middle.add(budget_info);
		
		
		bottom.setLayout(new GridLayout(4, 2));
		bottom.add(category_label);
		bottom.add(add_category);
		bottom.add(amount_label);
		bottom.add(add_amount);
		bottom.add(refresh);
		
		
		bottom.add(add);
		
		
		top.add(header);
		bottom.add(cancel);
		
		bottom.add(save);
		
	}
	
	public EditBudget()
	{
		create_ui();
		add.addActionListener(this);
		cancel.addActionListener(this);
		save.addActionListener(this);
		refresh.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if cancel is pressed 
		if (label == "Cancel")
		{
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if save is pressed 
		else if (label == "Save Changes")
		{
			
			for(int i = 0; i < textfields.size(); i++)
			{
				TextField textfield = textfields.get(i);
				Float amount = null;
				try
				{
					amount = Float.parseFloat(textfield.getText());
				}
				catch (Exception error)
				{
					//System.out.println("amount must be float");
				}
				
				int id = ids.get(i);
				String category = categories.get(i);
				if (amount != null)
				{
					try 
					{	
						
						modify_budget(category, id, Float.parseFloat(textfield.getText()));
					} 
					catch (SQLException e1) 
					{
						e1.printStackTrace();
					}
				}
				
				
			}
			BudgetApplet.changeScreen("Budget Summary");
		}
		//if add budget is pressed
		else if (label == "Add New Budget")
		{
			String category = add_category.getText();
			
			if (category.trim().length() == 0)
			{
				category_label.setText("Category: Category cannot be empty ");
				//System.out.println("category cannot be empty");
			}
			else
			{
				category_label.setText("Category: ");
			}
			
			Float amount = null;
			try
			{
				amount_label.setText("Amount: ");
				amount = Float.parseFloat(add_amount.getText());
			}
			catch (Exception error)
			{
				if (add_amount.getText().trim().length() == 0)
				{
					//System.out.println(amount);
					amount_label.setText("Amount: Amount cannot be empty ");
					//System.out.println("amount cannot be empty");
				}
				else
				{
					amount_label.setText("Amount: Amount must be float ");
					//System.out.println("amount must be float");
				}
				
			}
			
			
			
			if (amount != null && category.trim().length() != 0)
			{
				try 
				{
					add_budget(category, amount);
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
			}
		}
		else if (label == "Refresh")
		{
			update_ui();
			BudgetApplet.changeScreen("Budget Summary");
			BudgetApplet.changeScreen("Edit Budget");
		}
	}
}
