package com.gmail.bschneppdev.jvterm;

import java.awt.Color;

import javax.swing.JFrame;
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

	UIManager.put("nimbusBlueGrey", Color.BLUE);

	jf.setSize(800, 600);
	Instance in = new Instance();
	tabbedPane = new TabbedPane();
	tabbedPane.add("JvTerm", in);
	jf.getContentPane().add(tabbedPane);
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
