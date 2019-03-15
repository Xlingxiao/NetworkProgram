package Socket.tcp.demo_02;/**
 * @创建人
 * @创建时间
 * @描述
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Socket
 * @description: 加入线程池的Socket Server
 * @author: Ling
 * @create: 2018/08/26 13:34
 **/
public class Server {

    public static void main(String[] args) throws IOException {
        int port = 8888;
        Socket client;
        ServerSocket server = new ServerSocket(port);
        ExecutorService clientPool = Executors.newFixedThreadPool(5);
        System.out.printf("Socket 服务端已经启动，占用本地端口：%d",server.getLocalPort());
        while (true){
            client = server.accept();
            clientPool.submit(new myThread(client));
        }
    }

    static class myThread implements Runnable{
        Socket client ;
        StringBuilder sb = new StringBuilder();

        public myThread(Socket client) { this.client = client; }

        public void run() {
            handler();
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void handler(){
            clientRequest();
            response();
        }
        private void clientRequest(){
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream(),"utf-8"));
                String tmp;
                while (null != (tmp=br.readLine())){
                    sb.append(tmp);
                }
                System.out.println(sb.toString());
                client.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void response(){
            try {
                Writer writer = new OutputStreamWriter(client.getOutputStream());
                writer.write("收到消息\r\nSUCCESS");
                writer.flush();
                client.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
