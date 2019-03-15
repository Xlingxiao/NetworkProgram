package Socket.tcp.demo_02;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: Socket
 * @description: 多线程测试Socket Server
 * @author: Ling
 * @create: 2018/08/26 13:34
 **/
public class Client {

//    每个线程重发请求次数
    static int retries = 10;

    public static void main(String[] args) {
//        客户端线程数量
        int clientNum = 30;
        System.out.printf("准备启动%d个客户端 ",clientNum);
        List<Thread> threadList = new ArrayList<Thread>(clientNum);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < clientNum; i++) {
            Thread thread = new Thread(new myClient());
            threadList.add(thread);
            thread.start();
        }
        for (Thread t:
             threadList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("%d个线程，总共发送%d次请求，整个过程使用了%dms",clientNum,clientNum*retries,(endTime-startTime));
    }
    static class myClient implements Runnable{
        Socket client;
        int port = 8888;
        StringBuilder sb = new StringBuilder();
        public void run() {
            try {
                for (int i = 0;i<retries;i++){
                    client = new Socket("127.0.0.1",port);
                    handler(i);
                    Thread.sleep(5);
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        private void handler(int i){
            request(i);
            serverResponse();
        }
        private void request(int i){
            Writer writer;
            try {
                writer = new OutputStreamWriter(client.getOutputStream());
                writer.write("");
                writer.flush();
                client.shutdownOutput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private void serverResponse(){
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(client.getInputStream(),"utf-8"));
                String tmp;
                while (null!=(tmp = br.readLine())){
                    sb.append(tmp).append("\r\n");
                }
                System.out.println(sb.toString());
                client.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
