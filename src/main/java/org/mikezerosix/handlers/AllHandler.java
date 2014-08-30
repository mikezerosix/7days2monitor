package org.mikezerosix.handlers;


import java.io.PrintStream;

/* read all to destination
 */
public class AllHandler implements TelnetOutputHandler {

    PrintStream out;

    public AllHandler(PrintStream out) {
        this.out = out;
    }

    @Override
    public boolean match(String line) {
        return true;
    }

    @Override
    public void handleInput(String input) {
        out.println(input);
    }
}
