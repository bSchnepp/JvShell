package com.gmail.bschneppdev.jvterm;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.gmail.bschneppdev.jvterm.conhost.Instance;

//Quick and dirty prototype for LightningPrompt for Waypoint. Kinda.

public class JvTerm
{
    private static JTabbedPane tabbedPane;
    private static JFrame jf = new JFrame("JvTerm");
    
    public static void main(String[] argv)
    {
	jf.setSize(800, 600);
	Instance in = new Instance();
	tabbedPane = new JTabbedPane();
	tabbedPane.add("JvTerm", in);
	in.exec();
	jf.getContentPane().add(tabbedPane);
	jf.setVisible(true);
    }
}
