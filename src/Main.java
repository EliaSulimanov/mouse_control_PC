import java.awt.*;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

public class Main extends Thread {

    //region ATTRIBUTES
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = gd.getDisplayMode().getWidth();
    private static int height = gd.getDisplayMode().getHeight();
    //endregion

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Mouse control app");
        System.out.println("Elia Sulimanov - 2019");

        //try and generate key
        try {
            String key = GenerateConnectionKey();
            System.out.println("Your key is: " + key);
        }
        //if key could not be generated close the server app
        catch (Exception e) {
            System.out.println("Error occurred while key generation.");
            System.out.println(e.getMessage());
            System.exit(0);
        }

        //if the application is still alive we can start up the server
        new Thread(new ServerThread()).start();
    }

    //region Mouse Control Methods
    /**
     * Move mouse to (x, y)
     *
     * @param x screen x
     * @param y screen y
     */
    static void moveMouse(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove((x * width) / 100, (y * height) / 100);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Left click
     */
    static void leftClick(boolean isDoubleClick) {
        try {
            Robot robot = new Robot();
            if (isDoubleClick) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Right click
     */
    static void rightClick() {
        try {

            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_MASK);
            robot.mouseRelease(InputEvent.BUTTON3_MASK);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Key Generator
    /**
     * Generate connection key for the user to write int the app
     * @return Connection key
     */
    private static String GenerateConnectionKey()
    {
        String ip;
        String key = "";
        try {
            //Get user LAN address in order to generate connection key
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    ip = addr.getHostAddress();
                    if (ip.startsWith("192.168.1")) {
                        key = ip.substring(8);
                        key = key.replace('.', '-');
                        return key;
                    }
                }
                if (key.isEmpty())
                    throw new SocketException("Empty key - Error occurred while generating your personal key.");
            }
        }
        catch(SocketException e){
            throw new RuntimeException(e);
        }
        return "Empty key - Error occurred while generating your personal key.";
    }
    //endregion

    static class ServerThread implements Runnable {
        @Override
        public void run()
        {
            ServerSocket serverSocket = null;
            char operator;
            String message;
            try {
                serverSocket = new ServerSocket(3333);
                //serverSocket.setSoTimeout(1);

                while(true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        BufferedReader inputReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        message = inputReader.readLine();

                        if(message != null && !message.equals(""))
                            operator = message.charAt(0);
                        else
                            operator = 'i';

                        switch (operator)
                        {
                            case 'm':
                                Main.moveMouse(Integer.parseInt(message.substring(message.indexOf(',') + 1, message.lastIndexOf(','))), Integer.parseInt(message.substring(message.lastIndexOf(',') + 1)));
                                break;
                            case 'd':
                                Main.leftClick(true);
                                break;
                            case 'l':
                                Main.leftClick(false);
                                break;
                            case 'r':
                                Main.rightClick();
                                break;
                            default:
                                break;
                        }
                    }
                    catch (SocketTimeoutException e)
                    {
                    }
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            finally {
                try{
                    if(serverSocket != null)
                        serverSocket.close();
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}