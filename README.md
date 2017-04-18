# JvShell
A (better) JavaShell made just to brush up on Java.
___

JvShell is a simple terminal emulator written in pure Java. It's (currently) __not__ working as intended, but I'm fixing it.
The code is absolutely horrible, a spaghetti mess. It's not meant to be very easy to work on or anything, just something quick, dirty, and _fast_ to write a better one in C++ or C later on for Waypoint. Just something easy enough to add ideas and stuff onto and add code to.

Contributions, though, provided they're under the MIT License (see LICENSE) are welcome.
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
%d -- Current date in yyyy/MM/dd HH:mm:ss format
%h -- Hostname of the computer (What network will see)
%s -- Name of the shell (JvShell)
%v -- Java version
```
___
TODO features:
- Be decent!
- Support VT100 stuff
- Make it actually usable!
- Fix the resource leaks _everywhere_!
