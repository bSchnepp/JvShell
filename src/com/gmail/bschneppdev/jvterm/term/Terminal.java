package com.gmail.bschneppdev.jvterm.term;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.gmail.bschneppdev.jvterm.conhost.Instance;

public class Terminal
{
    private File cwd;
    private ArrayList<File> path;
    private PrintStream termout, termerr;
    private InputStream termin;

    public Terminal(File file)
    {
	this(file, System.out, System.err);
    }

    public Terminal()
    {
	this(new File(System.getProperty("user.home")));
	//On Waypoint, this will usually be A:\Users\$(USERNAME)\
	// Stuff in there will be things like Desktop, Documents, Settings, and whatever random non-system files you want.
    }

    public Terminal(PrintStream out, PrintStream err)
    {
	this(new File(System.getProperty("user.home")), out, err);
    }

    public Terminal(PrintStream out, PrintStream err, InputStream in)
    {
	this(new File(System.getProperty("user.home")), out, err, in);
    }

    public Terminal(File file, PrintStream out, PrintStream err)
    {
	this(file, out, err, System.in);
    }

    public Terminal(File file, PrintStream out, PrintStream err, InputStream in)
    {
	this.cwd = file;
	this.path = new ArrayList<File>();
	path.add(this.cwd);	//FIXME for changing directories!!!
	//termout = new File("$jvterm" + numterm);

	termout = out;
	termerr = err;
	termin = in;

	//try
	//{
	//writer = new PrintWriter(termout);
	//}
	//catch (FileNotFoundException exception)
	//{
	//    exception.printStackTrace();
	//}
    }

    public boolean execute()
    {

	try
	{
	    dispPrompt();
	    @SuppressWarnings("resource")	//This closes after 'exit' command issued. I think.
	    Scanner input = new Scanner(System.in);
	    String in = null;
	    if (input.hasNextLine())
	    {
		in = input.nextLine();
	    }
	    while (!in.equalsIgnoreCase("exit"))
	    {
		//We'll dump the standard output of the file here...
		if (!checkIntegrated(in))
		{
		    String cmd, rest;
		    if (in.indexOf(' ') > 0)
		    {
			cmd = in.substring(0, in.indexOf(' '));
			rest = in.substring(in.indexOf(' '));
		    }
		    else
		    {
			cmd = in;
			rest = "";
		    }
		    if (cmd.equals(""))
		    {
			break;
		    }
		    ProcessBuilder pb = new ProcessBuilder(cmd, rest);
		    try
		    {
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectInput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			pb.start();
		    }
		    catch (IOException exception)
		    {
			exception.printStackTrace();
		    }
		}
		break;
	    }
	    if (in.equalsIgnoreCase("exit"))
	    {
		input.close();
		return false;
	    }
	}
	catch (NoSuchElementException exception)	//'exit' command issued...
	{
	    termerr.println(exception.getMessage());
	    termout.flush();
	    termerr.flush();
	    System.exit(0);	//BUG: Still prompting after shutdown...
	}
	return true;
    }

    private boolean checkIntegrated(String cmd)
    {
	String[] cmdlist = cmd.split(" ");
	for (int i = 0; i < cmdlist.length; i++)
	{
	    String nclr = cmdlist[i].trim();
	    if (nclr.equalsIgnoreCase("pdir") || nclr.equalsIgnoreCase("chdir"))
	    {
		return true;
	    }
	    if (nclr.equalsIgnoreCase("export"))
	    {
		int equals = cmdlist[i + 1].indexOf('=');
		String key = cmdlist[i + 1].substring(0, equals);
		String val = cmdlist[i + 1].substring(equals + 1);
		Instance.addVariable(key, val);
		return true;
	    }
	}
	return false;
    }

    public void write(String str)
    {
	termout.print(str);
    }

    public int getPromptLength()
    {
	return this.getPrompt().length();
    }

    public String getPrompt()
    {
	StringBuilder sb = new StringBuilder();
	File rgr = new File("registry/system_reg.rgr");

	Scanner line = null;
	try
	{
	    if (!rgr.exists())
	    {
		line = new Scanner("RKEY_PROMPT_NAME = string:\"<%n@%l\\>!:\"");
	    }
	    else
	    {
		line = new Scanner(rgr);
	    }
	}
	catch (FileNotFoundException exception)
	{
	    exception.printStackTrace();
	}
	String ln = line.nextLine();
	line.close();

	//Check for variables like "%l" or "%n".
	//To emulate Waypoint, we'll also flip the slashes.

	int indexok = ln.lastIndexOf("string:") + "string:".length() + 1;

	char[] arr = ln.substring(indexok).toCharArray();
	for (int i = 0; i < arr.length - 1; i++)
	{
	    //messy code :( TODO refactor
	    if (arr[i] == '\\')
	    {
		sb.append('/');
	    }
	    else if (arr[i] != '%')
	    {
		sb.append(arr[i]);
	    }
	    else
	    {
		switch (arr[i + 1])
		{
		    case 'n':
			sb.append(System.getProperty("user.name"));
			i++;
			break;

		    case 'l':
			try
			{
			    char[] cwd = this.cwd.getCanonicalPath().toCharArray();
			    for (char element : cwd)
			    {
				if (element != '\\')
				{
				    sb.append(element);
				}
				else
				{
				    sb.append('/');
				}
			    }
			}
			catch (IOException exception)
			{
			    exception.printStackTrace();
			}
			i++;
			break;

		    default:
			break;
		}

	    }
	}
	return sb.toString();
    }

    public void dispPrompt()
    {
	// We'll link  to the registry...
	// Because we're not actually on Waypoint, we'll kind of 'emulate' it.
	// As this is a quick and dirty hack, it's done through files.
	termout.print(this.getPrompt());
    }

    public File getCwd()
    {
	return cwd;
    }

    public void setCwd(File cwd)
    {
	this.cwd = cwd;
    }

    public ArrayList<File> getPath()
    {
	return path;
    }

    public void setPath(ArrayList<File> path)
    {
	this.path = path;
    }

    public void setTermcontent(File termcontent)
    {
	try
	{
	    this.termout = new PrintStream(termcontent);
	}
	catch (FileNotFoundException exception)
	{
	    // TODO Auto-generated catch block
	    exception.printStackTrace();
	}
    }

}
