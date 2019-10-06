package Agent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClientProccess {

    static DatagramPacket packet;
    static DatagramSocket socket;
    static volatile boolean on = true;
    
    public static void main(String[] args){
        configureUDP();

    }

    public static void configureUDP(){
        try {
        	//opening udp socket on random number
            socket = new DatagramSocket();
            System.out.println("opening udp at " + socket.getLocalPort());
            sendPacket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    public static void sendPacket (){
        
        System.out.println("specify the agent udp port you want to connect with");
        Scanner sc = new Scanner(System.in);
      
        //the agent udp port
		int port = sc.nextInt();
		System.out.println("Specify the udp port of your destination");
		
		//the target destination port
		int target = sc.nextInt();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        String line;
        InetAddress address ;
        try {
            address  = InetAddress.getByName("localhost");
            System.out.println("type the message");
            
            //reading message from keyboard
            while((line = input.readLine()) != null) {
            	
               //the message = porttarget-message
                String msg = String.valueOf(target) + "-" + line;
                byte[] buf = msg.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port);
                
                //sending the packet to agent
                socket.send(packet);
                System.out.println("message sent to agent");

                //waiting for reply
                System.out.println("waiting packet");
                byte [] buffer = new byte[1024];
                packet = new DatagramPacket(buffer, buffer.length);
                
                //getting the packet
                socket.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
               
                printpacket(packet);
                
        
            }
            
            sc.close();
            input.close();
            socket.close();
        } catch (IOException e) {
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
