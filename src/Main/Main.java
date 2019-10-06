package Main;
import Agent.Agent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args){

        List<Integer> port = Arrays.asList(1234, 5678);
        try {
            InetAddress localhost = InetAddress.getByName("localhost");
            Agent agent1 = new Agent(localhost, localhost, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }
}
