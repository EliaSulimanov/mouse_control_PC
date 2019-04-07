import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {

    private ServerSocket serverSocket;
    private Socket socket;
    private char operator;

    public ServerThread()
    {
        try{
            serverSocket = new ServerSocket(9000);
            System.out.println("Opened socket at 9000");
            socket = serverSocket.accept();
            System.out.println("Client connected");
        }
        catch (Exception e){
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
            catch (Exception e){
            }
        }
    }
}
