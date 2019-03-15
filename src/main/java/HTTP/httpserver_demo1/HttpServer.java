package HTTP.httpserver_demo1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private ServerSocket serverSocket ;
    public static void main(String[] args) {
        HttpServer httpServer =new HttpServer();
        httpServer.start();
        httpServer.stop();
    }
//    启动方法
    public void start(){
        try {
            serverSocket = new ServerSocket(8888);
            this.receive();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    接受消息
    private void receive(){
        try {
            Socket client = serverSocket.accept();
            String msg = null;
//            存放客户端请求头的容器
            StringBuilder sb = new StringBuilder();
//            获得客户端请求数据放入缓冲区
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            对缓冲区数据进行读出到容器中
            while ((msg=br.readLine())!=null){
                sb.append(msg);
//                读入时添加换行符
                sb.append("\r\n");
//                System.out.println(msg.length());
                if (msg.length()==0){
                    break;
                }
            }
//            将数据转换为string类型方便输出并使用trim（）去掉首位的空格
            String requstInfo = sb.toString().trim();
            System.out.println(requstInfo);
//            System.out.print(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    停止服务
    public void stop(){

    }
}
