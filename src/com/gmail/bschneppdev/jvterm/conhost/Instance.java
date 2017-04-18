package com.gmail.bschneppdev.jvterm.conhost;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

import com.gmail.bschneppdev.jvterm.term.InputBuffer;
import com.gmail.bschneppdev.jvterm.term.Terminal;

public class Instance extends JPanel
{
    private TermArea area;
    private Terminal term;
    private Color background;

    public Terminal getTerm()
    {
	return term;
    }

    public void setTerm(Terminal term)
    {
	this.term = term;
    }

    private static HashMap<String, Object> termvars = new HashMap<String, Object>();

    public Instance()
    {
	super(new BorderLayout());
	this.area = new TermArea(Color.BLACK, Color.white, this);	//todo
	this.add(new JScrollPane(area));

	this.background = Color.BLACK;
	Color foreground = Color.WHITE;	//Ignored for now, will refactor to make useful later...
	try
	{
	    Scanner settings = new Scanner(new File("registry/system_reg.rgr"));
	    while (settings.hasNextLine())
	    {
		String line = settings.nextLine();
		if (line.contains("RKEY_PROMPT_COLOR = color3:\""))
		{
		    int index = line.lastIndexOf("RKEY_PROMPT_COLOR = color3:\"");
		    int r, g, b;
		    String[] colors = line
		            .substring(index + "RKEY_PROMPT_COLOR = color3:\"".length(), line.length() - 1).trim()
		            .split(",");
		    r = Integer.parseInt(colors[0].trim());
		    g = Integer.parseInt(colors[1].trim());
		    b = Integer.parseInt(colors[2].trim());
		    background = new Color(r, g, b);
		}
		System.out.println(line);
	    }
	    settings.close();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

	UIDefaults defaults = UIManager.getLookAndFeelDefaults();
	defaults.put("TextPane[Enabled].backgroundPainter", new AbstractRegionPainter()
	{

	    @Override
	    public AbstractRegionPainter.PaintContext getPaintContext()
	    {
		return new AbstractRegionPainter.PaintContext(null, null, false);
	    }

	    @Override
	    public void doPaint(Graphics2D g, JComponent c, int width, int height, Object[] extendedCacheKeys)
	    {
		g.setColor(Instance.this.background);
		g.fillRect(0, 0, width, height);
	    }
	});

	defaults.put("defaultFont", new Font("Open Sans", Font.PLAIN, 18));	//Set the font eveeerrrryywwehhheeerrreee...
										//It's a **really** nice lookalike/replacement/thing for Segoe, which is a *really* nice font.
										//And __it's freeeeeeeeeeeee!__
	
	this.area.putClientProperty("Nimbus.Overrides", defaults);
	this.area.putClientProperty("Nimbus.Overrides.InheritDefaults", false);
	this.area.setBackground(Color.BLACK);
    }

    @SuppressWarnings("serial")
    public void exec()
    {
	TermAreaBuffer taberr = new TermAreaBuffer(this.area, Color.RED);
	TermAreaBuffer tabout = new TermAreaBuffer(this.area);

	this.area.setTaberr(taberr);
	this.area.setTabout(tabout);

	PrintStream err = new PrintStream(taberr);
	PrintStream out = new PrintStream(tabout);
	InputStream in = new TermAreaInputBuffer();

	this.area.setErr(taberr);	//Access from TermAreaInputBuffer. Yes, I know this desperately needs refactoring.
	this.area.setOut(tabout);

	this.term = new Terminal(out, err, in);
	boolean keeprunning = true;
	term.dispPrompt();

	//	this.area.addKeyListener(new KeyListener()
	//	{
	//	    private ArrayList<Character> buffer = new ArrayList<Character>();
	//
	//	    @Override
	//	    public void keyTyped(KeyEvent e)
	//	    {
	//
	//	    }
	//
	//	    @Override
	//	    public void keyPressed(KeyEvent e)
	//	    {
	//		Instance.this.area.setEditable(true);
	//		try
	//		{
	//		    if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE)
	//		    {
	//			if (buffer.size() >= 0)
	//			{
	//			    buffer.remove(buffer.size() - 1);
	//			    return;
	//			}
	//			else
	//			{
	//			    Instance.this.area.setEditable(false);
	//			    Toolkit.getDefaultToolkit().beep();
	//			    return;
	//			}
	//		    }
	//		    else if (e.getKeyCode() == KeyEvent.VK_ENTER)
	//		    {
	//			//this.ready = true;
	//			//ignore all that, want this working asap
	//			StringBuilder cnt = new StringBuilder();
	//			for (int i = 0; i < buffer.size(); i++)
	//			{
	//			    cnt.append(buffer.get(i));
	//			}
	//			InputFlush flu = null;
	//			try
	//			{
	//			    flu = term.getFlush();
	//			}
	//			catch (NullPointerException ex)
	//			{
	//			    //ex.printStackTrace();\
	//			    term.setFlush(new InputFlush());
	//			}
	//			flu.setContent(cnt.toString());
	//
	//			String[] outputs = term.runln();
	//
	//			TermAreaBuffer output = Instance.this.area.getTabout();
	//			TermAreaBuffer errput = Instance.this.area.getTaberr();
	//
	//			PrintStream streamout = new PrintStream(output);
	//			PrintStream streamerr = new PrintStream(errput);
	//
	//			streamout.print(outputs[0]);
	//			streamerr.print(outputs[1]);
	//			System.out.print("STUFF CALLED...");
	//
	//			streamerr.close();
	//			streamout.close();
	//			System.out.println(outputs[0]);
	//			System.out.print(outputs[1]);
	//			try
	//			{
	//			    output.close();
	//			    errput.close();
	//			}
	//			catch (IOException exception)
	//			{
	//			    // TODO Auto-generated catch block
	//			    exception.printStackTrace();
	//			}
	//			return;
	//		    }
	//		}
	//		catch (NullPointerException exe)
	//		{
	//		    if (JvTerm.hasExeced)
	//		    {
	//			if (term == null)
	//			{
	//			    System.err.println("Something wrong");
	//			}
	//			term.write(exe.getMessage() + '\n');
	//		    }
	//		    return;
	//		}
	//		this.buffer.add(e.getKeyChar());
	//	    }
	//
	//	    @Override
	//	    public void keyReleased(KeyEvent e)
	//	    {
	//
	//	    }
	//
	//	});

	InputMap inmap = this.area.getInputMap();
	ActionMap acmap = this.area.getActionMap();
	inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");

	acmap.put("enter", new AbstractAction()
	{
	    long position = 0;

	    @Override
	    public void actionPerformed(ActionEvent arg0)
	    {
		StringBuilder cnt = new StringBuilder();
		position += term.getPromptLength();

		//Get the input real fast...
		try
		{
		    cnt.append(area.getText().substring((int) position).trim());
		}
		catch (StringIndexOutOfBoundsException exe)
		{
		    cnt.append(" ");
		}
		System.out.println(cnt.toString());
		position = area.getText().length() + 2;

		//		for (int i = 0; i < buffer.size(); i++)
		//		{
		//		    cnt.append(buffer.get(i));
		//		}
		InputBuffer flu = null;
		try
		{
		    flu = term.getFlush();
		}
		catch (NullPointerException ex)
		{
		    //ex.printStackTrace();\
		    term.setFlush(new InputBuffer());
		}
		flu.setContent(cnt.toString());

		String[] outputs = term.runln();

		TermAreaBuffer output = Instance.this.area.getTabout();
		TermAreaBuffer errput = Instance.this.area.getTaberr();

		PrintStream streamout = new PrintStream(output);
		PrintStream streamerr = new PrintStream(errput);

		streamout.print(outputs[0]);
		streamerr.print(outputs[1]);
		System.out.print("STUFF CALLED...");

		streamerr.close();
		streamout.close();
		System.out.println(outputs[0]);
		System.out.print(outputs[1]);
		try
		{
		    output.close();
		    errput.close();
		}
		catch (IOException exception)
		{
		    // TODO Auto-generated catch block
		    exception.printStackTrace();
		}
		term.write("\n");
		term.dispPrompt();
	    }
	});

	while (keeprunning)
	{
	    keeprunning = this.term.execute();
	}

	err.close();
	out.close();
	try
	{
	    in.close();
	}
	catch (IOException exception)
	{
	    exception.printStackTrace();
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
