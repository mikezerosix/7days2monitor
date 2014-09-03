package org.mikezerosix.handlers;


import java.io.PrintStream;
import java.io.PrintWriter;

/* read all to destination
 */
public class AllHandler implements TelnetOutputHandler {

    PrintWriter out;

    public AllHandler(PrintWriter out) {
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
