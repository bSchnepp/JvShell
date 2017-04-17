# JvShell
A (better) JavaShell made just to brush up on Java.
___

JvShell is a simple terminal emulator written in pure Java. It's (currently) __not__ working as intended, but I'm fixing it.
It's intended to be a sort of "prototype" for Waypoint's shell.
___

Built-in commands:
```
  chdir   -- Change the current working directory.
  pdir    -- Print the current working directory.
  export -- Exports a variable to the system, in the format 'export variable=value'. The equals must connect them.
```

___
JvShell supports changing the prompt displayed on every newline.
To do this, simply open registry/system_reg.rgr, and modify the value.

Any character not preceeded with a percent sign is printed.

The following combinations are supported:

```
%n -- Name of user
%l -- current working directory
```
___
TODO features:
- Everything!
- Get the shell input/output to actually go on the TermArea
- Be decent!
