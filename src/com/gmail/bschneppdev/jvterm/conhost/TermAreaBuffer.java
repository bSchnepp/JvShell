package com.gmail.bschneppdev.jvterm.conhost;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class TermAreaBuffer extends OutputStream
{

    private TermArea area;
    private Color color;
    
    public TermAreaBuffer(TermArea area)
    {
	this.area = area;
	this.color = Color.WHITE;
    }
    
    public TermAreaBuffer(TermArea area, Color color)
    {
	this.area = area;
	this.color = color;
    }
    
    
    @Override
    public void write(int b) throws IOException
    {
	DefaultStyledDocument doc =(DefaultStyledDocument)area.getDocument();
	StyleContext cont = new StyleContext();
	Style style = cont.addStyle(null, null);
	StyleConstants.setForeground(style, color);
	try
	{
	    doc.insertString(doc.getLength(), (char)b + "", style);
	}
	catch (BadLocationException exception)
	{
	    exception.printStackTrace();
	}
    }
    

}
