package org.mikezerosix;

import junit.framework.TestCase;
import org.mikezerosix.telnet.TelnetService;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TelnetServiceTest extends TestCase {

    public void testRun() throws Exception {
        TelnetService telnetService = new TelnetService("", 0, "");
        final Thread thread = new Thread(telnetService);
        thread.start();
        while (!telnetService.isConnected()) {
            Thread.sleep(1000);
        }
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(telnetService.getInputStream()));

        telnetService.write("lp");
        while (true) {
            boolean end_loop = false;
            Thread.sleep(1000);

            System.out.println(bufferedReader.readLine());
        }

    }



}