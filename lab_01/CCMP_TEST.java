package lab_01;

import lab_01.ccmp.CCMP;
import lab_01.utils.Utils;


class Router{
    private CCMP ccmp;
    private String secret;

    public Router(String secret){
        this.ccmp=new CCMP(Utils.ofFile());
        this.secret=secret;
    }

    public void sendPackets(){
        this.ccmp.match(this.secret);
    }
    public void receivePackets(){
        this.ccmp.match(secret);
    }
}
public class CCMP_TEST {
    public static void main(String[] args) {
        String secret = "IB2021_01";
        Router router=new Router(secret);
        router.sendPackets();
        router.receivePackets();

    }
}
