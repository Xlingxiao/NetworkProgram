package Socket.tcp.demo_03;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Socket
 * @description: 使用Socket内置的心跳机制检测client是否活着
 * @author: Ling
 * @create: 2018/09/09 09:35
 **/
public class MyServer {

    public static void main(String[] args) throws IOException {
        MyServer myServer = new MyServer();
        int port = 8888;
        ServerSocket server = new ServerSocket(port);
        ServerSocket heartbeatServer = new ServerSocket(port+1);
        System.out.printf("服务端以启动请访问：%s\n",String.valueOf(server.getInetAddress()));
        System.out.printf("心跳服务器以启动：%s\n",String.valueOf(heartbeatServer.getInetAddress()));
        myServer.start(server,heartbeatServer);
    }

    void start(ServerSocket server , ServerSocket heartbeat){
        ExecutorService clients = Executors.newFixedThreadPool(20);
        ExecutorService heatbeatPool = Executors.newFixedThreadPool(20);
        while (true){
            try {
                Socket client = server.accept();
                Socket heartbeatObj = heartbeat.accept();
                heatbeatPool.submit(new heartbeatDog(heartbeatObj));
                clients.submit(new workMan(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class workMan implements Runnable{

    Socket client;
    boolean flag;

    public workMan(Socket client) {
        this.client = client;
        flag = true;
    }

    public void run() {

    }
}

class heartbeatDog implements Runnable{

    private boolean flag;
    Socket heartbeat;

    public heartbeatDog(Socket heartbeat) {
        flag = true;
        this.heartbeat = heartbeat;
    }

    public void run() {

    }
}
