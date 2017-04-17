package com.gmail.bschneppdev.jvterm.term.builtin;

import java.io.File;

import com.gmail.bschneppdev.jvterm.term.Terminal;

public class Chdir implements Command
{
    @Override
    public void execute(String[] argv, Terminal term)
    {
	File file = new File(argv[1]);
	term.setCwd(file);
	term.write("\n");
    }
}
