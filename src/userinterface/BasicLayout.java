package userinterface;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class BasicLayout extends JPanel {
	
	JPanel top = new JPanel();
	JPanel middle = new JPanel();
	JPanel bottom = new JPanel();
	static String userName;
	
	public BasicLayout() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;//fill whole horizontal space given
		c.anchor= GridBagConstraints.PAGE_START;//start at top left of page
		c.gridx= 0;//top left is on first column
		c.gridy= 0;//top left is on first row
		c.gridwidth = 4;// width is 4 squares
		c.gridheight = 1;// height is 1 square
		add(top,c);
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL;
		d.ipady = 200;// height in pixels
		d.gridx= 0;
		d.gridy= 1;
		d.gridwidth = 4;
		d.gridheight= 4;
		add(middle,d);
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		e.gridx=0;
		e.gridy= 10;
		e.gridwidth = 4;
		e.gridheight = 1;
        add(bottom,e);
		}
	
	
	public static void setUserName(String user){
		userName = user;
	}
	
	public String getUserName(){
		return userName;
	}
	
	public  static Connection get_connection() throws SQLException{
		MysqlDataSource dataSource = new MysqlDataSource();
    	dataSource.setUser("jdk8334");
    	dataSource.setPassword("0RRaL2f9E");
    	dataSource.setServerName("satoshi.cis.uncw.edu");
    	dataSource.setDatabaseName("jdk8334");
    	Connection conn = dataSource.getConnection();
    	return conn;
	}
	
}