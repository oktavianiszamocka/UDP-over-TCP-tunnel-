package other;

import java.io.Serializable;
import java.net.InetAddress;

public class Message implements Serializable {
    private String msg;
    private int to, from;
    private InetAddress target;
    private boolean disconnect;


    public Message(){

    }

    public Message(boolean disconnect){
        this.disconnect = disconnect;
    }
    public Message(InetAddress target){
        this.target = target;
    }

    public Message(int to, int from, String msg){
        this.to = to;
        this.from = from;
        this.msg = msg;
    }

    public Message(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPort() {
        return to;
    }

    public void setto(int to) {
        this.to = to;
    }

    public InetAddress getTarget() {
        return target;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTarget(InetAddress target) {
        this.target = target;
    }

    public boolean isDisconnect() {
        return disconnect;
    }

    public void setDisconnect(boolean disconnect) {
        this.disconnect = disconnect;
    }
}
