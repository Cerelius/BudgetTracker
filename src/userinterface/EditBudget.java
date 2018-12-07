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
	
	JLabel header = new JLabel("Set Budget");
	JButton add = new JButton("Add New Budget");
	JButton cancel = new JButton("Cancel");
	JButton save = new JButton("Save Changes");
	
	ArrayList<TextField> textfields = new ArrayList<TextField>();
	ArrayList<Integer> ids = new ArrayList<Integer>();
	
	public void create_budgets(JPanel panel) throws SQLException
	{
		Connection conn = get_connection();
		
		String sql = "SELECT Budget_ID, Category, Amount FROM Category where Username = 'NotSoKoolUser11'";
		
		PreparedStatement prepared_statement = conn.prepareStatement(sql);

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
        	panel.add(category);
        	panel.add(category_input);
        	
    	}
	}
	
	public void add_budget(String category, float amount) throws SQLException
	{
		Connection conn = get_connection();
		String sql = "INSERT INTO Budgets VALUES ('0005', ?, 'NotSoKoolUser11');";
		String sql2 = "INSERT INTO Category Values( ?, '0005', 'NotSoKoolUser11', ?)";

		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		prepared_statement.setString(1, dateFormat.format(date));
		prepared_statement.executeUpdate();
		
		prepared_statement = conn.prepareStatement(sql2);
		prepared_statement.setString(1, category);
		prepared_statement.setFloat(2, amount);
    	prepared_statement.executeUpdate();
	}
	
	public void modify_budget(int id, Float amount) throws SQLException
	{
		Connection conn = get_connection();
		String sql = "UPDATE Category SET Amount = ? where Budget_ID = ? and Username = 'NotSoKoolUser11'";
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setFloat(1, amount);
		prepared_statement.setInt(2, id);
    	prepared_statement.executeUpdate();
	}
	
	public EditBudget()
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
		bottom.add(new JLabel("Category: "));
		bottom.add(add_category);
		bottom.add(new JLabel("Amount: "));
		bottom.add(add_amount);
		bottom.add(new JLabel(""));
		
		bottom.add(add);
		add.addActionListener(this);
		
		top.add(header);
		bottom.add(cancel);
		cancel.addActionListener(this);
		bottom.add(save);
		save.addActionListener(this);
		

		
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
				int id = ids.get(i);
				
				try 
				{
					modify_budget(id, Float.parseFloat(textfield.getText()));
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
			
			
			BudgetApplet.changeScreen("Budget Summary");
		}
		else if (label == "Add New Budget")
		{
			String category = add_category.getText();
			Float amount = Float.parseFloat(add_amount.getText());
			
			try 
			{
				add_budget(category, amount);
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
			
			BudgetApplet.changeScreen("Edit Budget");
		}
		
	}
}
