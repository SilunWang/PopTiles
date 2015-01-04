package allen.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Allen on 14/12/21.
 */
public class ServerThread {

    public static void main(String[] args) {
        Server server = new Server();
        server.init();
        SocketListener listener = new SocketListener(server);
        listener.start();
    }
}

class SocketListener extends Thread		//监听socket连接的线程，负责不断接入socket
{
    Server server;

    public SocketListener(Server server)
    {
        this.server = server;
    }

    public void run()
    {
        while(true)
        {
            try
            {
                Thread.sleep(500);
                System.out.println("waiting for client...");
                Socket socket = server.socket.accept();
                System.out.println("client connected");
                (new ClientListener(socket)).start();
            }
            catch(Exception e)
            {
                System.out.println("Error:" + e);
            }
        }
    }
}

class Server {

    ServerSocket socket;
    static MongoDriver mongoDriver;

    public void init() {
        try {
            socket = new ServerSocket(5658);
            mongoDriver = new MongoDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientListener extends Thread{

    Socket socket;
    InputStream in;
    OutputStream out;
    BufferedReader reader;
    OutputStreamWriter writer;
    UserManager userManager;

    public ClientListener(Socket sock) {
        this.socket = sock;
        try {
            this.in = socket.getInputStream();
            this.out = socket.getOutputStream();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(new InputStreamReader(this.in));
        writer = new OutputStreamWriter(this.out);
    }


    public void run() {

        while (true) {
            try {
                String response = reader.readLine();
                if (response == null)
                    continue;
                String str[] = response.split("-");
                String head = str[0];
                if (head.equals("login:")) {
                    String username = str[1];
                    String password = str[2];
                    userManager = new UserManager(username, password);
                    writer.write("Login " + Server.mongoDriver.authenticate(userManager) + "\n");
                    writer.flush();
                }
                else if (head.equals("register:")) {
                    String username = str[1];
                    String password = str[2];
                    userManager = new UserManager(username, password);
                    writer.write("Register " + Server.mongoDriver.saveUser(userManager) + "\n");
                    writer.flush();
                }
                else if (head.equals("score:")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());    //获取当前时间
                    String timeStr = formatter.format(curDate);
                    userManager.setTimestamp(timeStr);
                    userManager.setHighestScore(Integer.parseInt(str[1]));
                    Server.mongoDriver.saveScore(userManager);
                    System.out.println(str[1]);
                }
                else if (head.equals("exit")) {
                    System.out.println(response);
                    return;
                }
                else
                    System.out.println(response);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
