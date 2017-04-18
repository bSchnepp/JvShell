package com.gmail.bschneppdev.jvterm.term;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Scanner;

public class JvProcess implements Runnable
{

    private String cmd;
    private String rest;

    private StringBuilder out;
    private StringBuilder err;
    //private InputStream in;
    private SequenceInputStream min;
    private int proc;	//id for stdout and stderr

    public JvProcess(String cmd, String rest, StringBuilder bfrout, InputStream in, StringBuilder bfrerr)
    {
	this.cmd = cmd;
	this.rest = rest;

	this.out = bfrout;
	//this.in = in;
	this.err = bfrerr;
    }

    public void setProc(int proc)
    {
	this.proc = proc;
    }

    public InputStream getInput()
    {
	return this.min;
    }

    @Override
    public void run()
    {
	ProcessBuilder pb = new ProcessBuilder(cmd, rest);
	try
	{
	    pb.redirectInput(Redirect.INHERIT);

	    File stderr = new File("$stderr" + proc);
	    File stdout = new File("$stdout" + proc);
	    if (!stderr.exists() || !stdout.exists())
	    {
		stderr.createNewFile();
		stdout.createNewFile();
	    }
	    else
	    {
		stderr.delete();
		stdout.delete();
		stderr.createNewFile();
		stdout.createNewFile();
	    }
	    pb.redirectError(stderr);
	    pb.redirectOutput(stdout);
	    pb.redirectInput(Redirect.INHERIT);
	    pb.start();
	    Scanner stderrscn = new Scanner(stderr);
	    Scanner stdoutscn = new Scanner(stdout);
	    while (stderrscn.hasNextLine())
	    {
		this.err.append(stderrscn.nextLine());
	    }
	    while (stdoutscn.hasNextLine())
	    {
		this.out.append(stdoutscn.nextLine());
	    }
	    stderrscn.close();
	    stdoutscn.close();
	    stderr.deleteOnExit();
	    stdout.deleteOnExit();
	    Thread.currentThread().interrupt();
	}
	catch (IOException exception)
	{
	    exception.printStackTrace();
	}
    }

}
