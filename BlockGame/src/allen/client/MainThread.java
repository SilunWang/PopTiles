package allen.client;


import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

/**
 * Created by Allen on 14/12/20.
 */

public class MainThread extends Thread{

    MainFrame mainFrame;
    LoginPanel loginPanel;
    Socket socket;
    InputStream in;
    OutputStream out;
    BufferedReader reader;
    public static boolean connected;
    public static OutputStreamWriter writer;
    String ip = "127.0.0.1";
    int port = 5658;

    public MainThread(MainFrame clientFrame, LoginPanel loginPanel) {
        this.mainFrame = clientFrame;
        this.loginPanel = loginPanel;
    }

    public void init() {
        try {
            socket = new Socket(ip, port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            writer = new OutputStreamWriter(out);
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
            connected = false;
        }
    }

    public void run() {

        while (true) {

            try {
                String response = reader.readLine();
                if (response == null)
                    continue;
                if (response.equals("Login true")) {
                    System.out.println(response);
                    mainFrame.usernameField.setText(loginPanel.getUsername());
                    mainFrame.usernameField.setVisible(true);
                    loginPanel.setVisible(false);
                    connected = true;
                    JOptionPane.showMessageDialog(mainFrame, "Login Success!", "Message", JOptionPane.PLAIN_MESSAGE);
                }
                else if (response.equals("Login false")) {
                    JOptionPane.showMessageDialog(mainFrame, "Authentication invalid", "Message", JOptionPane.PLAIN_MESSAGE);
                    loginPanel.setVisible(true);
                }
                else if (response.equals("Register true")) {
                    mainFrame.usernameField.setText(loginPanel.getUsername());
                    loginPanel.setVisible(false);
                    mainFrame.usernameField.setVisible(true);
                    JOptionPane.showMessageDialog(mainFrame, "Register Success", "Message", JOptionPane.PLAIN_MESSAGE);
                }
                else if (response.equals("Register false")) {
                    loginPanel.setVisible(true);
                    JOptionPane.showMessageDialog(mainFrame, "Already registered. Please Login", "Message", JOptionPane.PLAIN_MESSAGE);
                }
                else if (response.equals("err")) {
                    System.out.println("error");
                    System.exit(0);
                    return;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class MainActivity {

    public static void main(String[] args) {

        final MainFrame mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                if (!MainThread.connected)
                    return;
                try {
                    MainThread.writer.write("exit");
                    MainThread.writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        LoginPanel loginPanel = new LoginPanel();
        MainThread clientThread = new MainThread(mainFrame, loginPanel);

        try {
            clientThread.init();
            clientThread.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
