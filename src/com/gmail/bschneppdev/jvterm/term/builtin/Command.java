package com.gmail.bschneppdev.jvterm.term.builtin;

import com.gmail.bschneppdev.jvterm.term.Terminal;

public interface Command
{
    public void execute(String[] argv, Terminal term);
}
