package org.mikezerosix.telnet;

import org.mikezerosix.handlers.TelnetOutputHandler;
import org.mikezerosix.telnet.TelnetConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TelnetListener implements Runnable {

    private TelnetConnection telnetConnection;
    private List<TelnetOutputHandler> handlers = new ArrayList<>();

    public TelnetListener(TelnetConnection telnetConnection) {
        this.telnetConnection = telnetConnection;
    }

    public void addHandler(TelnetOutputHandler handler) {
        handlers.add(handler);
    }


    @Override
    public void run() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(telnetConnection.getInputStream()));
        try {
            while (telnetConnection.isConnected()) {
                final String line = bufferedReader.readLine();
                for (TelnetOutputHandler handler : handlers) {
                    handler.handleInput(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log("** Listener detected connection is closed **");

    }

    private void log(String message) {
        System.out.println(message);
    }

}
