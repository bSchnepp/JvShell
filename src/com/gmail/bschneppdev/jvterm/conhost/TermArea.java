package com.gmail.bschneppdev.jvterm.conhost;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JTextPane;

public class TermArea extends JTextPane
{
    public TermArea(Color bg, Color fg)
    {
	super();
	super.setBackground(bg);
	super.setForeground(fg);
	Scanner settings = null;
	try
	{
	    settings = new Scanner(new File("registry/jv_fontsz.set"));
	}
	catch (FileNotFoundException exception)
	{
	    // TODO Auto-generated catch block
	    exception.printStackTrace();
	}
	String fontname = settings.nextLine();
	int size = settings.nextInt();
	settings.close();
	this.setFont(new Font(fontname, Font.PLAIN, size));
    }
}
