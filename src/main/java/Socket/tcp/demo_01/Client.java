package Socket.tcp.demo_01;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        Socket client =null;
        InputStream is = null;
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            System.out.println("尝试连接："+inetAddress.toString()+" ...");
            client = new Socket(inetAddress,8888);
            System.out.println("连接成功！");
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            writer.write("你好！我是客户端");
            writer.flush();
            client.shutdownOutput();
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String msg =null;
            while ((msg=br.readLine())!=null){
                sb.append(msg);
                sb.append("\r\n");
            }
            client.shutdownInput();
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
