package Socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        Socket client =null;
        InputStream is = null;
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            System.out.println("尝试连接："+inetAddress.toString()+" ...");
            client = new Socket(inetAddress,8989);
            System.out.println("连接成功！");
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String msg = null;
            while ((msg=br.readLine())!=null){
                sb.append(msg);
                sb.append("\r\n");
                if (msg==null){
                    break;
                }
            }
            System.out.printf("这是服务器返回的消息：\n  %s",sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void start(){

    }
}
