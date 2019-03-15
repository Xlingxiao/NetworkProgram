package Socket.tcp.longConnect.CatchException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Socket
 * @description: 通过捕获异常的方式实现长连接
 * @author: Ling
 * @create: 2018/09/11 14:46
 **/
public class Server {


    public static void main(String[] args) {

        int port = 4096;

        try {
            ServerSocket server = new ServerSocket(port);
            start(server);
        } catch (IOException e) {
            System.out.println("创建Socket服务端出错");
        }
    }

    private static void start(ServerSocket server) {
        boolean flag = true;
        ExecutorService clients = Executors.newFixedThreadPool(20);
        System.out.printf("服务端以启动占用本地端口：%d\n",server.getLocalPort());
        while (flag){
            try {
                workMan workMan = new workMan(server.accept());
                clients.submit(workMan);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class workMan implements Runnable{

    private Socket client;
    private StringBuilder stringBuilder;
    workMan(Socket client) {
        this.client = client;
        System.out.printf("已有客户端连接%s\n",client.getInetAddress());
        stringBuilder = new StringBuilder();
    }

    public void run() {
        InputStream is;
        BufferedReader br = null;
        try {
            is = client.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            String msg;
            while (null!=(msg = br.readLine())){
                stringBuilder.append(msg);
                if (stringBuilder.length()>20){
//                        处理逻辑
                    System.out.println(stringBuilder.toString());
                    stringBuilder.delete(0,stringBuilder.length());
                }
            }
        } catch (SocketException e){
            System.out.println("客户端出现异常");
            System.out.println(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                if (!client.isClosed()){
                    client.close();
                }
            } catch (IOException e) {
                System.out.println("输入流已关闭");
            }
        }
    }
}
