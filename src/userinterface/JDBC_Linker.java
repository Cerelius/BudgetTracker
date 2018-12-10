package userinterface;

import java.sql.*;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.io.Console;

public class JDBC_Linker
{
	static Connection conn;

	
	
    public static void main(String[] Args) throws SQLException
    {
    	MysqlDataSource dataSource = new MysqlDataSource();
    	dataSource.setUser("jdk8334");
    	dataSource.setPassword("0RRaL2f9E");
    	dataSource.setServerName("satoshi.cis.uncw.edu");
    	dataSource.setDatabaseName("jdk8334");

    	Connection conn = dataSource.getConnection();
    	
    	
    	Statement stmt = conn.createStatement();
    	String sql = "SELECT * FROM Users where Username = ?";
    	PreparedStatement prepared_statement = conn.prepareStatement(sql);
    	String username = "SuperKoolUser91";
    	prepared_statement.setString(1, username);
    
    	ResultSet rs = prepared_statement.executeQuery();
    	ResultSetMetaData rsmd = rs.getMetaData();
    	
    	int columnsNumber = rsmd.getColumnCount();
    	
    	while (rs.next()) 
    	{
    	    for (int i = 1; i <= columnsNumber; i++) 
    	    {
    	        if (i > 1) 
    	        {
    	        	System.out.print(",  ");
    	        }
    	        String columnValue = rs.getString(i);
    	        System.out.print(columnValue + " " + rsmd.getColumnName(i));
    	    }
    	    System.out.println("");
    	}
    	
    	
    	
    	conn.close();
    }
}



