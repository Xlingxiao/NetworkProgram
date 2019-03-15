package HTTP.httpserver_demo3;

//负责监听端口
//得到请求后交给startwork

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class addException {
    private ServerSocket serverSocket ;
    private int ServerPort = 8888;
    public static void main(String[] args) {
        addException haveReturnValue =new addException();
        haveReturnValue.start();
//        haveReturnValue.stop();
    }
    //    启动方法
    public void start() {
//        线程池
        ExecutorService clientConnectPool = null;
        Socket client = null;
        int StartWorkNum = 1000;
        try {
            serverSocket = new ServerSocket(ServerPort);
            System.out.printf("服务端启动成功，开始监听%d端口\n", ServerPort);
            List<StartWork> startWorkList = new ArrayList<StartWork>(StartWorkNum);
            StartWork startWork = null;
            clientConnectPool = Executors.newFixedThreadPool(100);
            while (true) {
                client = serverSocket.accept();
                startWork = new StartWork(client);
                clientConnectPool.submit(startWork);
//                Thread t = new Thread(startWork);
//                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientConnectPool.shutdown();
        }

    }
    //    停止服务
    public void stop(){
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
