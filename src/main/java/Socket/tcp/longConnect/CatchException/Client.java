package Socket.tcp.longConnect.CatchException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: Socket
 * @description: 匹配长连接的客户端
 * @author: Ling
 * @create: 2018/09/11 15:17
 **/
public class Client {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 4096;
        try {
            Socket client = new Socket(ip,port);
            System.out.printf("开始连接服务器%s:%d\n",ip,port);
            major(client);
        } catch (IOException e) {
            System.out.println("连接服务器失败！");
            e.printStackTrace();
        }
    }

    private static void major(Socket client){
        try {
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            int sleepDuration = 3000;
            while (true){
                writer.write(format.format(new Date()));
                writer.write("\n");
                writer.flush();
                Thread.sleep(sleepDuration);
                sleepDuration += 1000;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
