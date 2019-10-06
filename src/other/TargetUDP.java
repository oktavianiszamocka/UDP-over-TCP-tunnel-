package other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class TargetUDP {
    static DatagramPacket packet;
    static DatagramSocket socket;
    public static void main(String[] args){
        configureUDP();

    }

    public static void configureUDP(){
        try {
            socket = new DatagramSocket();
            System.out.println("UDP socket is opened at " + socket.getLocalPort());
            listen();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public static void listen(){


        //System.out.println(socket.getLocalAddress().getHostAddress());
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int dest;
        InetAddress address;
        try {
        	
			while(true){
				byte [] buf = new byte[1024];
		        packet = new DatagramPacket(buf, buf.length);
			    	System.out.println("waiting packet");
			    	
			    	//receiving packet
			        socket.receive(packet);
			        printpacket(packet);
                    String data = new String(packet.getData(), 0, packet.getLength());
                    String[] split = data.split("-");
                    //get the sender of port
                    int targetPort = Integer.parseInt(split[0]);

			        System.out.println("--type message to be send");
			        line = input.readLine();
			        address = packet.getAddress();
			        String message = String.valueOf(targetPort) + "-" + line;
	                byte[] buffer = message.getBytes();
	                
	                //send to relay
	                packet = new DatagramPacket(buffer, buffer.length, address, packet.getPort());
	                socket.send(packet);
	                
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
