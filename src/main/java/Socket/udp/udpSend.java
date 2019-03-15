package Socket.udp;

import java.io.IOException;
import java.net.*;

public class udpSend {
    public static void main(String[] args) {
        DatagramSocket datagramSocket = null;
        String address = "127.0.0.1";
        try {
            datagramSocket = new DatagramSocket();
            InetAddress addre ;
            addre= InetAddress.getByName("127.0.0.1");
            int port = 8989;
//            创建测试数据由于传输只能用字节传输所以要转为字节形式
            String test ="hello UDP";
            byte[] buf = test.getBytes();
//            使用ctrl+P可以查看方法所需要的参数
//            我们可以看到DatagramPacket（）需要传输的数据、数据长度、目标地址、目标端口
            System.out.println("开始打包数据");
            DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length,addre,port);
            System.out.println("开始发送数据");
            datagramSocket.send(datagramPacket);
            System.out.println("数据发送成功");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            datagramSocket.close();
        }
    }
}
