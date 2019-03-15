package Socket.tcp.demo_01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket Sserver ;
    int port = 8989;
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        server.stop();
    }
    public void start(){
        try {
            Sserver = new ServerSocket(port);
            System.out.printf("服务器开始启动：\n监听: %d 端口...\n",port);
            Socket client= Sserver.accept();
            System.out.println("已有客户端连接。");
//            this.receive(client);
            this.send(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void receive(Socket client){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String msg = null;
            while ((msg=br.readLine()).length()>0){
                sb.append(msg);
                sb.append("\r\n");
                if(null == msg){
                    break;
                }
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void send(Socket client){
        OutputStream outputStream = null;
        try {
            outputStream= client.getOutputStream();
            String returnMsg = "你已经连接上服务器..." ;
            outputStream.write(returnMsg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stop(){
        try {
            Sserver.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
