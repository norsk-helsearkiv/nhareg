package no.arkivverket.helsearkiv.nhareg.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by haraldk on 08/12/2017.
 */
public class SocketPrinter {

    public void print(final String content, final String hostIp, final Integer printerPort) throws IOException {
        // The line below illustrates the default port 6101 for mobile printers 9100 is the default port number
        // for desktop and tabletop printers
        final Socket clientSocket = new Socket(hostIp, printerPort);

        final DataOutputStream outToPrinter = new DataOutputStream(clientSocket.getOutputStream());
        final PrintStream print = new PrintStream(outToPrinter);
        print.print(content);

        clientSocket.close();
        outToPrinter.close();
        print.close();
    }

}