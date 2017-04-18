package com.gmail.bschneppdev.jvterm.term;

public class InputBuffer
{

    private String content = "";
    
    public String getContent()
    {
	return content;
    }
    
    public void clear()
    {
	this.content = null;
    }
    
    public void setContent(String content)
    {
	this.content = content;
    }

}
