package userinterface;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.*;


public class BudgetSummary extends BasicLayout implements ActionListener
{
	JLabel header = new JLabel("Budget Summary");
	JButton logout = new JButton("Logout");
	JButton transSum = new JButton("Transaction Summary");
	JButton addTrans = new JButton("Add Transaction");
	JButton acctSum = new JButton("Accounts Summary");
	JButton editBud = new JButton("Edit Budget");
	JButton refresh = new JButton("Refresh");
	
	public static DefaultTableModel build_table_model(ResultSet rs)
	        throws SQLException 
	{

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) 
	    {
	        columnNames.add(metaData.getColumnLabel(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) 
	    {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) 
	        {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	public JTable create_summary_table() throws SQLException
	{
		Connection conn = get_connection();
		String sql = "Select temp.Budget_ID, temp.Category as Category, temp.Amount as Balance, coalesce(Credit.Amount, 0) as Spent, coalesce(temp.Amount - Credit.Amount, temp.Amount) as Remaining from (select * from Category where Username = ?) temp left join Credit on Credit.Username = ? and temp.Username = ? and Credit.Budget_ID = temp.Budget_ID and Credit.Category = temp.Category ";
		
		PreparedStatement prepared_statement = conn.prepareStatement(sql);
		prepared_statement.setString(1, getUserName());
		prepared_statement.setString(2, getUserName());
		prepared_statement.setString(3, getUserName());

    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	ResultSetMetaData metaData = rs.getMetaData();

		JTable table  = new JTable(build_table_model(rs));
		conn.close();
		
		return table;
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
		top.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx=0;
		c.gridy= 0;
		c.weightx = 0.8;
		top.add(header,c);
		GridBagConstraints d = new GridBagConstraints();
		d.gridx=1;
		d.gridy= 0;
		d.weightx = 0.2;
		top.add(logout);
		logout.addActionListener(this);
		
		JTable table = null;
		try 
		{
			table = create_summary_table();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		//table.setEnabled(false);
		table.setDefaultEditor(Object.class, null);

		JScrollPane scroll = new JScrollPane(table);
		middle.add(scroll);  
		middle.add(refresh);
		
		
		bottom.setLayout(new GridLayout(1,4));
		bottom.add(transSum);
		bottom.add(addTrans);
		bottom.add(acctSum);
		bottom.add(editBud);
		
	}
	

	public BudgetSummary()
	{
		create_ui();
		transSum.addActionListener(this);
		addTrans.addActionListener(this);
		acctSum.addActionListener(this);
		editBud.addActionListener(this);
		refresh.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		String label = button.getText();
		//if transSum is pressed 
		if (label == "Transaction Summary"){
			BudgetApplet.changeScreen("Transaction Summary");
		}
		//if addTrans is pressed
		else if (label == "Add Transaction"){
			BudgetApplet.changeScreen("Add Transaction");
		}
		//if acctSum is pressed
		else if (label == "Accounts Summary"){
			BudgetApplet.changeScreen("Accounts Summary");
		}
		//if editBud is pressed
		else if (label == "Edit Budget"){
			BudgetApplet.changeScreen("Edit Budget");
		}
		//if logout is pressed 
		else if (label == "Logout"){
			BudgetApplet.changeScreen("Login");
		}
		
		else if (label == "Refresh"){
			update_ui();
			BudgetApplet.changeScreen("Edit Budget");
			BudgetApplet.changeScreen("Budget Summary");

			
		}

		
	}

}