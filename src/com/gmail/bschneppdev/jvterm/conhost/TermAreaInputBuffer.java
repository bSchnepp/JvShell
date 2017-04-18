package com.gmail.bschneppdev.jvterm.conhost;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TermAreaInputBuffer extends InputStream
{
    private ArrayList<Character> buffer = new ArrayList<Character>();
    private int pointer = 0;
    private boolean ready = false;
    
    public boolean isReady()
    {
	return ready;
    }
    
    @Override
    public int read() throws IOException
    {
	if (ready)
	{
	    if (pointer >= buffer.size())
	    {
		return -1;
	    }
	    ready = false;	//Lock the input stream...
	    return this.buffer.get(pointer++);
	}
	return -1;
    }

    @Override
    public int read(byte[] b)
    {
	int c = 0;
	for (int i = 0; i < b.length; i++)
	{
	    b[i] = (byte) buffer.get(i).charValue();
	    c++;
	}
	return c;
    }
}
