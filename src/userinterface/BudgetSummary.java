package userinterface;
import java.awt.Button;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.sql.*;


public class BudgetSummary extends BasicLayout implements ActionListener {
	
	JLabel header = new JLabel("Budget Summary");
	JButton logout = new JButton("Logout");
	JButton transSum = new JButton("Transaction Summary");
	JButton addTrans = new JButton("Add Transaction");
	JButton acctSum = new JButton("Accounts Summary");
	JButton editBud = new JButton("Edit Budget");
	
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
		
		String sql = "Select Category.Category as Category, Category.Amount as Balance, coalesce(Credit.Amount, 0) as Spent, coalesce(Category.Amount - Credit.Amount, Category.Amount) as Remaining from Category left join Credit on Category.Category = Credit.Category where Category.Username = 'NotSoKoolUser11'";
		
		PreparedStatement prepared_statement = conn.prepareStatement(sql);

    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	ResultSetMetaData metaData = rs.getMetaData();

		JTable table  = new JTable(build_table_model(rs));
		
		return table;
	}
	
	public BudgetSummary(){
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
		
		bottom.setLayout(new GridLayout(1,4));
		bottom.add(transSum);
		transSum.addActionListener(this);
		bottom.add(addTrans);
		addTrans.addActionListener(this);
		bottom.add(acctSum);
		acctSum.addActionListener(this);
		bottom.add(editBud);
		editBud.addActionListener(this);
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

		
	}

}