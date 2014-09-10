package org.mikezerosix.telnet.handlers;


import java.io.PrintWriter;
import java.util.regex.Matcher;

/* read all to destination
 */
public class AllHandler implements TelnetOutputHandler {

    PrintWriter out;

    public AllHandler(PrintWriter out) {
        this.out = out;
    }


    @Override
    public Matcher[] matcher(String line) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleInput(String input) {
        out.println(input);
    }
}
