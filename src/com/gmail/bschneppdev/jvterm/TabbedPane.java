package com.gmail.bschneppdev.jvterm;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

class TabActionHandler
{
}

class Tab extends JPanel
{
    private static final long serialVersionUID = -8567048571651398359L;
    private JButton closebutton;
    private JLabel label;
    private TabbedPane parent;

    public Tab(TabbedPane parent) throws IOException
    {
	this(parent, "New tab");
    }

    public Tab(TabbedPane parent, String title) throws IOException
    {
	this(parent, title, new ImageIcon(ImageIO.read(new File("close.gif"))));
    }

    public Tab(TabbedPane parent, String title, Icon closeicon)
    {
	this.label = new JLabel(title);
	this.closebutton = new JButton();
	this.closebutton.setBorder(BorderFactory.createEmptyBorder());
	this.label.setBorder(BorderFactory.createEmptyBorder());
	this.closebutton.setIcon(closeicon);
	this.setLayout(new FlowLayout());
	this.add(closebutton);
	this.add(label);
	label.setForeground(Color.BLACK);	//For now...
	this.setBorder(BorderFactory.createEmptyBorder());

	this.closebutton.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		int index = parent.indexOfTab(Tab.this.label.getText());
		if (index >= 0)
		{
		    parent.removeTabAt(index);
		}
		if (parent.getTabCount() == 0 )
		{
		    JvTerm.shutdowntotal();
		}
	    }
	});

    }

    public JButton getButton()
    {
	return this.closebutton;
    }

    public void renameTab(String newName)
    {
	this.label.setText(newName);
    }

}

public class TabbedPane extends JTabbedPane
{
    private static final long serialVersionUID = -9112758451008732171L;

    @Override
    public Component add(String title, Component c)
    {
	Component comp = super.add(title, c);
	Tab tab = null;
	try
	{
	    tab = new Tab(this, title);
	}
	catch (IOException exception)
	{
	    exception.printStackTrace();
	}
	super.setTabComponentAt(super.getTabCount() - 1, tab);
	return comp;	//Don't know why you need this return value... but okay.
    }
}
