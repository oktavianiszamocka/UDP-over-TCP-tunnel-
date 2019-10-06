package Relay;
import other.Message;
import java.io.*;
import java.net.*;

public class Relay  {
	//int port = 6666;
    ServerSocket relay;
    Socket socket;
    DatagramSocket udp;
   DatagramPacket packet;
   ObjectOutputStream out;
   ObjectInputStream in;
    public static void main(String[] args){
       Relay r = new Relay();

    }

    Relay(){
        try {
        	//relay open TCP at port 6666
            relay = new ServerSocket(6666);
            System.out.println("Relay TCP in 6666 port");

            listenTCP();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void listenTCP(){
        try {
            System.out.println("listening");
            
            //connected with agent
            socket = relay.accept();
            InetAddress address = socket.getInetAddress();
            int port = socket.getPort();
            System.out.println("Agent " + address +" " + port + " is connected");
            listen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    synchronized void listen(){
        Message input, output;
        InetAddress target = null;
        try {

            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            
            //opening udp socket at random port
            udp = new DatagramSocket();
            System.out.println("Relay open UDP socket at : " +  udp.getLocalPort());

            //reading the packet TCP that coming
            while((output = (Message) in.readObject()) != null){
            	//if agent send message with inetaddress, do configuration
                if(output.getTarget() != null){
                    target = output.getTarget();
                    System.out.println("target is at "+ target );
                    
                    //opening the thread to help
                    new Thread(new RelayThread(udp, this, target)).start();
                }
                
                //if the message contain destination port
                if(output.getPort() != 0){

                	System.out.println("message TCP coming");
                	String message = output.getFrom() + "-" + output.getMsg();
                    byte [] buf = message.getBytes();
                    packet = new DatagramPacket(buf, buf.length, target, output.getPort());
                    udp.send(packet);
                     
                }
                //if the message is command to quit
                if(output.isDisconnect()){
                   System.out.println("Agent is exit");
                    socket.close();
                    in.close();
                    out.close();
                    break;
                }

            }
            
            //back to listening new agent
            listenTCP();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(Message msg){
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
