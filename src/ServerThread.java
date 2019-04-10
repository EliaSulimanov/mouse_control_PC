import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;

public class ServerThread implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private char operator;

    public ServerThread()
    {
        try{
            String ip;
            String key = "";
            serverSocket = new ServerSocket(9000);
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    if (iface.isLoopback() || !iface.isUp())
                        continue;

                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        ip = addr.getHostAddress();
                        if (ip.startsWith("192.168."))
                            if (!ip.startsWith("192.168.1.")) {
                                key = ip.substring(8);
                                key = key.replace('.', '-');
                                System.out.println("Your key is: " + key);
                            }
                    }
                }
                if(key.isEmpty())
                    System.out.println("Empty key - Error occurred while generating your personal key.");
            }
            catch (SocketException e) {
                System.out.println("Address error - Error occurred while generating your personal key.");
                throw new RuntimeException(e);
            }

            socket = serverSocket.accept();
            System.out.println("Client connected");
        }
        catch (Exception e) {
            System.out.println("Socket error - client can not connect to your server.");
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true){
            try
            {
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in client
                String message = inputStream.readLine();
                operator = message.charAt(0);
                switch (operator)
                {
                    case 'm':
                        Main.moveMouse(Integer.parseInt(message.substring(2, 4)), Integer.parseInt(message.substring(6, 8)));
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

                socket.close();
            }
            catch (Exception e)
            {
                System.out.println("Communication Error - Error occurred while getting commands from the smartphone app.");
                throw new RuntimeException(e);
            }
        }
    }
}
