package com.gmail.bschneppdev.jvterm.conhost;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import com.gmail.bschneppdev.jvterm.term.Terminal;

public class Instance extends JPanel
{
    private TermArea area;
    private Terminal term;
    private static HashMap<String, Object> termvars = new HashMap<String, Object>();
    
    public Instance()
    {
	super(new BorderLayout());
	this.area = new TermArea(Color.BLACK, Color.white);	//todo
	this.term = new Terminal();
	this.add(area);
    }
    
    public void exec()
    {
	boolean keeprunning = true;
	while (keeprunning)
	{
	    keeprunning = this.term.execute();
	}
    }
    
    public static Object checkIfVariable(String n)
    {
	if (Instance.termvars.containsKey(n))
	{
	    return termvars.get(n);
	}
	return null;
    }
    
    public static void addVariable(String k, Object v)
    {
	termvars.put(k, v);
    }
}
