package no.arkivverket.helsearkiv.nhareg.util;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.Socket;

/**
 * Created by haraldk on 08/12/2017.
 */
public class SocketPrinter {

    public void print(final String content, String hostIp, Integer printerPort) throws IOException {
        // The line below illustrates the default port 6101 for mobile printers 9100 is the default port number
        // for desktop and tabletop printers
        Socket clientSocket = new Socket(hostIp, printerPort);

        DataOutputStream outToPrinter = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream fileInput = new DataInputStream(new FileInputStream(content));
        IOUtils.copy(fileInput, outToPrinter);
        clientSocket.close();
        outToPrinter.close();
        fileInput.close();
    }
}
