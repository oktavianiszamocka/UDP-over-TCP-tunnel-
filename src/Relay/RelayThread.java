package Relay;

import other.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RelayThread extends Thread {
    DatagramSocket udp;
    Relay relay;
    InetAddress target;

    public RelayThread(DatagramSocket udp, Relay relay, InetAddress target){
        this.udp = udp;
        this.relay = relay;
        this.target = target;
    }

    public void run(){
        DatagramPacket packet;
        byte [] buffer = new byte[256];
        try {
            while(true){
                packet = new DatagramPacket(buffer, buffer.length);

                udp.receive(packet);
                System.out.println("UDP receive packet");

                InetAddress from = packet.getAddress();
                
                //comparing the incoming packet address with configuration address
                if(from.equals(target)) {
                    System.out.println("access granted");
                    String data = new String(packet.getData(), 0, packet.getLength());
                    String[] split = data.split("-");
                    
                    //get the port target
                    int targetPort = Integer.parseInt(split[0]);
                    String msg = split[1];
                    
                    //send to tcp agent
                    relay.sendMessage(new Message(targetPort, packet.getPort(), msg));
                   
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


}
