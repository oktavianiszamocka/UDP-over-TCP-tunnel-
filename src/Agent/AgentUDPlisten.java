package Agent;

import other.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class AgentUDPlisten extends Thread {
    int port;
    DatagramSocket udp;
    DatagramPacket packet;
    Socket socket;
    InetAddress target;
    ObjectInputStream in;
    Agent agent;
    boolean online;

    AgentUDPlisten(int port, Socket socket, InetAddress target, ObjectInputStream in, Agent agent, boolean online){
        this.port = port;
        this.socket = socket;
        this.target = target;
        this.in = in;
        this.agent = agent;
        this.online = online;

    }

    public void run(){
        startUDP();
        listening();

    }

    public void startUDP(){
        try {
        	//opening socket udp
            udp = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    synchronized void listening(){
        byte [] buf = new byte[1024];
        packet = new DatagramPacket(buf, buf.length);
        int to = 0;
        byte[] buffer;
        InetAddress address;
        Message message;
        int userport;
        
        try {
            address = InetAddress.getByName("localhost");
        while(online) {
            System.out.println("UDP AGENT " + port + " waiting for packet");

            //recieving udp packet from client proccess
            udp.receive(packet);
            System.out.println("message UDP coming");
            
            //remembering the client proccess that connect with this udp socket
            userport = packet.getPort();
            
            String data = new String(packet.getData(), 0, packet.getLength());
            
            String[] split = data.split("-");
            //get the target udp port
            int targetPort = Integer.parseInt(split[0]);
            String msg = split[1];
            
            //agent tcp send message to relay
            agent.sendMessage(new Message(targetPort,udp.getLocalPort(), msg));
           

            //message tcp from relay coming
            message = (Message) in.readObject();
            System.out.println("message TCP is coming");
            
            //convert message to udp
            String reply = String.valueOf(message.getFrom()) + "-" +message.getMsg();
            buffer = reply.getBytes();
            packet = new DatagramPacket(buffer, buffer.length, address, userport);
            
            //sending to client proccess
            udp.send(packet);
           

        }

            
            udp.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


    }
    
    public static void printpacket(DatagramPacket p){
        String data = new String(p.getData(), 0, p.getLength());
        InetAddress address = p.getAddress();
        int port = p.getPort();
        String msg = "message from"+ address.toString() + " " + String.valueOf(port) + " :" +data;
        System.out.println(msg);
        System.out.println("-----");
    }


}

