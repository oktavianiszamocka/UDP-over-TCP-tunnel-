package Agent;

import com.sun.corba.se.impl.io.InputStreamHook;
import other.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Agent {
    private InetAddress relay, target;
    private List<Integer> listofUDP;
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    volatile boolean online = true;

    public Agent(InetAddress relay, InetAddress target, List<Integer> listofUDP){
        this.relay = relay;
        this.target = target;
        this.listofUDP = listofUDP;
        connectRelay();
    }


    public void connectRelay(){
        try {
        	//connecting TCP connection
            socket = new Socket(relay, 6666);
            System.out.println("Agent has connected with relay");
            configurationRelay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configurationRelay(){
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            //sending the configuration of IP destination
            out.writeObject(new Message(target));
            out.flush();
             openUDP();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUDP() {
        for (Integer port : listofUDP) {
        	//opening thread to each udp socket
            new Thread(new AgentUDPlisten(port, socket, target, in, this, online)).start();
        }
        quit();

    }

    public void sendMessage(Message msg){
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(){
    	System.out.println("type QUIT if you want to quit");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.next();
        
        if(command.equals("QUIT")){
            sendMessage(new Message(true));
            online = false;
            try {
                socket.close();
                System.out.println("closing the connection");
                System.exit(1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    public List<Integer> getListofUDP() {
        return listofUDP;
    }

    public void setListofUDP(List<Integer> listofUDP) {
        this.listofUDP = listofUDP;
    }
}
