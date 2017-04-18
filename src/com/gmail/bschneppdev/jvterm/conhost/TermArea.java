package com.gmail.bschneppdev.jvterm.conhost;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JTextPane;

public class TermArea extends JTextPane
{
    private Instance instance;
    private TermAreaBuffer out;
    private TermAreaBuffer err;
    
    public TermArea(Color bg, Color fg, Instance parent)
    {
	super();
	this.instance = parent;
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

    public Instance getInstance()
    {
	return instance;
    }

    public void setInstance(Instance instance)
    {
	this.instance = instance;
    }
    
    public void setOut(TermAreaBuffer out)
    {
	this.out = out;
    }
    
    public void setErr(TermAreaBuffer err)
    {
	this.err = err;
    }
    
    public TermAreaBuffer getTaberr()
    {
	return err;
    }

    public void setTaberr(TermAreaBuffer taberr)
    {
	this.err = taberr;
    }

    public TermAreaBuffer getTabout()
    {
	return out;
    }

    public void setTabout(TermAreaBuffer tabout)
    {
	this.out = tabout;
    }
}
