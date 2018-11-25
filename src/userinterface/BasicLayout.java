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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BasicLayout extends JPanel {
	
	JPanel top = new JPanel();
	JPanel middle = new JPanel();
	JPanel bottom = new JPanel();
	
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
}