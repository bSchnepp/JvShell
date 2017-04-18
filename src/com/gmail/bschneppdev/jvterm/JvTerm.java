package com.gmail.bschneppdev.jvterm;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.gmail.bschneppdev.jvterm.conhost.Instance;

//Quick and dirty prototype for LightningPrompt for Waypoint. Kinda.

public class JvTerm
{
    private static JTabbedPane tabbedPane;
    private static JFrame jf = new JFrame("JvTerm");
    
    public static boolean hasExeced = false;
    
    public static void main(String[] argv)
    {
	jf.setSize(800, 600);
	Instance in = new Instance();
	tabbedPane = new JTabbedPane();
	tabbedPane.add("JvTerm", in);
	jf.getContentPane().add(tabbedPane);
	jf.setVisible(true);
	in.exec();
	hasExeced = true;
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
