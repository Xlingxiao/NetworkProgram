package Socket.tcp.longConnect.ReadEveryMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Socket
 * @description: 服务端通过识别每条客户端消息实现长连接
 * @author: Ling
 * @create: 2018/09/11 15:44
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
        ExecutorService clients = Executors.newFixedThreadPool(20);
        System.out.printf("服务端已启动占用本地端口：%d\n",server.getLocalPort());
        while (true){
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
        BufferedReader br = null;
        try {
            client.setSoTimeout(3000);
            br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String HEARTBEAT_CONTENT = "HEARTBEAT_CONTENT";
            String msg;
            while (null!=(msg = br.readLine())){
                if (!msg.equals(HEARTBEAT_CONTENT)){
                    stringBuilder.append(msg);
                    if (stringBuilder.length()>20){
//                        处理逻辑
                        System.out.println(stringBuilder.toString());
                        stringBuilder.delete(0,stringBuilder.length());
                    }
                }
            }
        } catch (SocketTimeoutException e){
            System.out.println("客户端发送消息超时");
            System.out.println(stringBuilder.toString());
        } catch (SocketException e){
            System.out.println("客户端出现异常");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                if (!client.isClosed())
                    client.close();
            } catch (IOException e) {
                System.out.println("输入流已关闭");
            }
        }
    }
}