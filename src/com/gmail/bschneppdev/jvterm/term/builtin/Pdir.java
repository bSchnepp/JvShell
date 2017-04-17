package com.gmail.bschneppdev.jvterm.term.builtin;

import com.gmail.bschneppdev.jvterm.term.Terminal;

public final class Pdir implements Command
{
    @Override
    public void execute(String[] argv, Terminal term)
    {
	term.write(term.getCwd().toString() + '\n');	//TODO: Fix backslashes
    }

}
