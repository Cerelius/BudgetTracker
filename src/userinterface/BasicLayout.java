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

/** BasicLayout
 * This class builds the basic layout of a JPanel for all screens except 
 * Login and create login
 * 
 * The basic layout contains:
 * 		- top : title of the screen
 *  	- middle : main content of the screen
 *  	- bottom : menu items to navigate to other screens
 */
public class BasicLayout extends JPanel {
	JPanel top = new JPanel();
	JPanel middle = new JPanel();
	JPanel bottom = new JPanel();
	static String userName;
	
	public BasicLayout() {
		setLayout(new GridBagLayout());
		// add constraint for the top and add it to the JPanel 
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;//fill whole horizontal space given
		c.anchor= GridBagConstraints.PAGE_START;//start at top left of page
		c.gridx= 0;//top left is on first column
		c.gridy= 0;//top left is on first row
		c.gridwidth = 4;// width is 4 squares
		c.gridheight = 1;// height is 1 square
		add(top,c);
		// add constraint for the middle and add it to the JPanel
		GridBagConstraints d = new GridBagConstraints();
		d.fill = GridBagConstraints.HORIZONTAL;
		d.ipady = 200;// height in pixels
		d.gridx= 0;
		d.gridy= 1;
		d.gridwidth = 4;
		d.gridheight= 4;
		add(middle,d);
		// add constraint for the bottom and add it to the JPanel
		GridBagConstraints e = new GridBagConstraints();
		e.fill = GridBagConstraints.HORIZONTAL;
		e.gridx=0;
		e.gridy= 10;
		e.gridwidth = 4;
		e.gridheight = 1;
        add(bottom,e);
		}
	
	/** setUserName
	 * this method sets a variable accessible by all screens which implement basic layout 
	 * so that they can know who the current user is 
	 */
	public static void setUserName(String user){
		userName = user;
	}
	
	public String getUserName(){
		return userName;
	}
	
	/** get_connection
	 * this method gets the database connection and returns a pointer to the connection 
	 */
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