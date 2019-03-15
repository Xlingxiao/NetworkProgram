package Socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class udpReceive {
    public static void main(String[] args) {
        DatagramSocket client;
        try {
            System.out.println("建立监听端口");
            client = new DatagramSocket(8888);
            System.out.println("已经准备好，可以接收数据");
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf,buf.length);
            client.receive(packet);
            InetAddress inetAddress = packet.getAddress();
            System.out.println("数据来源：    "+inetAddress.toString());
            byte[] data = packet.getData();
//            java将字节转为字符时需要使用字节对象新建立一个字符对象不能使用tostring（）方法
            String msg = new String(data);
            System.out.println("数据内容：  "+msg);
            int datalenth = packet.getLength();
            System.out.println("数据长度：    "+datalenth);
            int port = packet.getPort();
            System.out.println("接收端口号：    " +port);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
