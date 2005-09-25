package yajhfc;
/*
 * YAJHFC - Yet another Java Hylafax client
 * Copyright (C) 2005 Jonas Wolz
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/*
 * Launcher.java:
 * This is a (somewhat half-baked) implementation to detect already running instances
 * and to pass the command line parameters/data (in the case of --stdin) to the already
 * running instance.
 * I'm using a combination of a lock file and a ServerPort here to ensure that 
 * every user on a multi-user machine can run his own instance (I want to restrict it to 
 * one instance *per user* and _not_ one instance per machine).
 * The current implementation is far from perfect, so if someone with more java
 * experience knows a better way to accomplish this functionality, please let me know.
 */

import java.io.*;
import java.net.*;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Launcher {

    private static ServerSocket sockBlock = null;
    private static SockBlockAcceptor blockThread;
    private static mainwin application;
    static boolean isLocking = false;
    
    final static int codeSubmitStream = 1;
    final static int codeSubmitFile = 2;
    
    private static InetAddress getLocalhost() 
        throws UnknownHostException {
            final byte[] addr = {127, 0, 0, 1};
            return InetAddress.getByAddress(addr);
    }
    
    
    private static Socket checkLock() {
        File lock = new File(utils.getConfigDir() + "lock");
        if (lock.exists()) {
            try {
                BufferedReader filin = new BufferedReader(new FileReader(lock));
                String strport = filin.readLine();
                filin.close();
                
                int port = Integer.parseInt(strport);
                Socket cli = new Socket(getLocalhost(), port);
                return cli;
            } catch (Exception e) {
               // do nothing
            }
        } 
            
        createLock(lock);
        return null;
    }
    
    private static void createLock(File lock) {
        final int portStart = 64007;
        final int portEnd = 65269;
        int port;
        
        try {
            for (port = portStart; port <= portEnd; port++) {
                try {
                    sockBlock = new ServerSocket(port, 50, getLocalhost());
                    break;
                } catch (Exception e) {
                    // do nothing, try next port
                }
            }
            if (sockBlock != null) {
                FileWriter filout = new FileWriter(lock);
                filout.write(String.valueOf(port) + "\n");
                filout.close();
                lock.deleteOnExit();
                isLocking = true;
            }
        } catch (IOException e) {
            System.err.println("Could not create lock: " + e.getMessage());
        }
    }
    
    public static void releaseLock() {
        try {
            isLocking = false;
            if (sockBlock != null) {
                sockBlock.close();
                sockBlock = null;
            }
        } catch (IOException e) {
            // do nothing
        }
    }
    
    /**
     * Launches this application
     */
    public static void main(String[] args) {
        // parse command line
        String fileName = "";
        boolean useStdin = false;
        
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) { // command line argument
                if (args[i].equals("--stdin"))
                    useStdin = true;
                else
                    System.err.println(utils._("Unknown command line argument: ") + args[i]);
            } else if (args[i].startsWith("-"))
                System.err.println(utils._("Unknown command line argument: ") + args[i]);
            else // treat argument as file name to send
                fileName = args[i];
        }
        
        Socket oldinst = checkLock();
        
        if (oldinst == null) {
            SwingUtilities.invokeLater(new NewInstRunner(fileName, useStdin));
            blockThread = new SockBlockAcceptor();
            blockThread.start();
        } else {            
            try {
                if (useStdin) { 
                    BufferedOutputStream bufOut = new BufferedOutputStream(oldinst.getOutputStream());
                    BufferedInputStream bufIn = new BufferedInputStream(System.in);
                    bufOut.write(codeSubmitStream);
                    byte[] buf = new byte[16000];
                    int bytesRead = 0;
                    do {
                        bytesRead = bufIn.read(buf);
                        if (bytesRead > 0)
                            bufOut.write(buf, 0, bytesRead);
                    } while (bytesRead >= 0);
                    bufIn.close();
                    bufOut.close();                
                } else if ( fileName.length() > 0) {
                    OutputStream outStream = oldinst.getOutputStream();
                    outStream.write(codeSubmitFile);
                    BufferedWriter bufOut = new BufferedWriter(new OutputStreamWriter(outStream));
                    File f = new File(fileName);
                    bufOut.write(f.getAbsolutePath() + "\n");
                    bufOut.close();
                } else {
                    System.err.println(utils._("There already is a running instance!"));
                    System.exit(1);
                }       
                oldinst.close();
            } catch (IOException e) {
                System.err.println("An error occured communicating with the old instance: " + e.getMessage());
            }
        }
    }
    
    static class NewInstRunner implements Runnable {
        String fileName;
        boolean useStdin;
        
        public void run() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't load native look&feel.");
            }
            
            application = new mainwin();
            application.setVisible(true);
            
            if (fileName.length() > 0 || useStdin) {
                SendWin sw = new SendWin(application.hyfc, application);
                sw.setModal(true);
                if (useStdin)
                    sw.setInputStream(System.in);
                else
                    sw.setFilename(true, fileName);
                sw.setVisible(true);
                application.dispose();
            }
        }   
        
        public NewInstRunner(String fileName, boolean useStdin) {
            super();
            this.fileName = fileName;
            this.useStdin = useStdin;
        }
    }
    
    static class SockBlockAcceptor extends Thread {
        @Override
        public void run() {
            while (isLocking) {
                try {
                    Socket srv = sockBlock.accept();
                    InputStream strIn = srv.getInputStream();
                    switch (strIn.read()) {
                    case codeSubmitStream:
                        SwingUtilities.invokeAndWait(new SubmitRunner(strIn)); // Accept new faxes only sequentially
                        srv.close();
                        break;
                    case codeSubmitFile:
                        BufferedReader bufR = new BufferedReader(new InputStreamReader(strIn));
                        String fileName = bufR.readLine();
                        SwingUtilities.invokeAndWait(new SubmitRunner(fileName)); // Accept new faxes only sequentially
                        bufR.close();
                        strIn.close();
                        srv.close();
                        break;
                    case -1: // Connection closed without sending any data
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                application.toFront();
                            };
                        });
                        break;
                    default:
                        System.err.println("Unknown code received.");
                        strIn.close();
                        srv.close();
                    }
                } catch (Exception e) {
                    //System.err.println("Error listening for connections: "  + e.getMessage());
                }
            }
        }
        
        public SockBlockAcceptor() {
            super(SockBlockAcceptor.class.getName());
        }
    }
    
    static class SubmitRunner implements Runnable {
        InputStream strIn = null;
        String fileName = null;
        
        public void run() {
            try {
                application.toFront();
                
                SendWin sw = new SendWin(application.hyfc, application);
                sw.setModal(true);
                if (strIn != null)
                    sw.setInputStream(strIn);
                else
                    sw.setFilename(true, fileName);
                
                sw.setVisible(true);
                if (strIn != null)
                    strIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public SubmitRunner(String fileName) {
            super();
            this.fileName = fileName;
        }
        
        public SubmitRunner(InputStream strIn) {
            super();
            this.strIn = strIn;
        }
    }
}