package com.gmail.bschneppdev.jvterm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.gmail.bschneppdev.jvterm.conhost.Instance;

// Quick and dirty prototype for LightningPrompt for Waypoint. Kinda.

public class JvTerm
{
    private static TabbedPane tabbedPane;
    private static JFrame jf = new JFrame("JvTerm");

    public static boolean hasExeced = false;

    public static void main(String[] argv)
    {

	for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
	{
	    if ("Nimbus".equals(info.getName()))
	    {
		try
		{
		    UIManager.setLookAndFeel(info.getClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException
		        | UnsupportedLookAndFeelException exception)
		{
		    //Nimbus not available. Ignore that.
		}
		break;
	    }
	}

	UIManager.put("nimbusBlueGrey", Color.BLACK);
	UIManager.put("TabbedPane.background", Color.BLACK);

	jf.setSize(800, 600);
	Instance in = new Instance();
	tabbedPane = new TabbedPane();
	tabbedPane.add("JvTerm", in);
	jf.setLayout(new BorderLayout());
	JMenuBar menubar = new JMenuBar();
	JMenu file = new JMenu("File");

	JMenuItem file_new = new JMenuItem("New");
	file_new.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
		//Yeah yeah the terminal thing is all messed up right now. Let's get a pretty GUI first before fixing it.
		Instance instance = new Instance();
		tabbedPane.addTab("JvTerm", instance);
		instance.exec();	//In theory, later, this will work.
	    }
	});

	file.setForeground(Color.WHITE);
	file.add(file_new);
	menubar.add(file);
	jf.getContentPane().add(menubar, BorderLayout.NORTH);
	jf.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	jf.setVisible(true);
	in.exec();
	hasExeced = true;
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void shutdowntotal()
    {
	System.exit(0);
    }
}
