package com.gmail.bschneppdev.jvterm.term;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.gmail.bschneppdev.jvterm.conhost.Instance;
import com.gmail.bschneppdev.jvterm.conhost.TermAreaInputBuffer;

public class Terminal
{
    private File cwd;
    private ArrayList<File> path;
    private PrintStream termout, termerr;
    private InputStream termin;
    private static int procid = 0;

    private InputBuffer inbuffer = new InputBuffer();

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

    public String readInputBuffer()
    {
	//Hack to get this thing working because messing with inputstreams is agghhdgfdggfdgfgfdgfdgffgdfg
	Scanner line = new Scanner(this.inbuffer.getContent());
	String ln = "";
	if (line.hasNextLine())
	{
	    ln = line.nextLine();
	}
	else
	{
	    ln = " ";
	}
	line.close();
	System.out.println("READING FLUSH...");
	return ln;
    }

    public String[] runln()
    {
	String ln = this.readInputBuffer();

	StringBuilder bfrout = new StringBuilder();
	StringBuilder bfrerr = new StringBuilder();
	if (!checkIntegrated(ln))
	{
	    String cmd, rest;
	    if (ln.indexOf(' ') > 0)
	    {
		cmd = ln.substring(0, ln.indexOf(' '));
		rest = ln.substring(ln.indexOf(' '));
	    }
	    else
	    {
		cmd = ln;
		rest = "";
	    }
	    if (cmd.equals(""))
	    {
		return new String[]
		{
		        "", ""
		};
	    }
	    if (cmd.equals("exit"))
	    {
		//Absolutely make sure we clean up our mess before exit.
		File tmp = new File("$");
		try
		{
		    tmp.createNewFile();
		}
		catch (IOException exception)
		{
		    // TODO Auto-generated catch block
		    exception.printStackTrace();
		}
		try
		{
		for (File n : tmp.getParentFile().listFiles())
		{
		    if (n.getName().contains("$std"))
		    {
			n.delete();
		    }
		}
		}
		catch (NullPointerException e)
		{
		    e.printStackTrace();
		    e.printStackTrace(termerr);
		}
		tmp.delete();
		System.exit(0);
	    }
	    JvProcess jv = new JvProcess(cmd, rest, bfrout, termin, bfrerr);
	    jv.setProc(procid);
	    //Thread t = new Thread(jv);
	    //t.start();
	    jv.run();//remove threading for now...
	}
	try
	{
	    Scanner outp = new Scanner(new File("$stdout" + procid));
	    Scanner errp = new Scanner(new File("$stderr" + procid));

	    while (outp.hasNextLine())
	    {
		bfrout.append(outp.nextLine() + '\n');
	    }
	    while (errp.hasNextLine())
	    {
		bfrerr.append(errp.nextLine() + '\n');
	    }

	    outp.close();
	    errp.close();
	    procid++;
	}
	catch (FileNotFoundException exception)
	{
	    // TODO Auto-generated catch block
	    exception.printStackTrace();
	}
	String[] output = new String[]
	{
	        bfrout.toString(), bfrerr.toString()
	};
	return output;
    }

    public boolean execute()
    {
	try
	{
	    if (((TermAreaInputBuffer) termin).isReady())
	    {
		dispPrompt();
		Scanner input = new Scanner(termin);
		String in = null;
		if (input.hasNextLine())
		{
		    in = input.nextLine();
		}
		while (!in.equalsIgnoreCase("exit"))
		{
		    //We'll dump the standard output of the file here...
		    runln();
		    break;
		}
		input.close();
	    }
	    else
	    {
		//...
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
	while (line.hasNextLine())
	{
	    System.out.println(line.nextLine());
	}
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

		    case 'd':
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			sb.append(dateFormat.format(date));
			i++;
			break;

		    default:
			break;
		}

	    }
	}
	sb.append(' ');
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

    public InputBuffer getFlush()
    {
	return inbuffer;
    }

    public void setFlush(InputBuffer flush)
    {
	this.inbuffer = flush;
    }

}
