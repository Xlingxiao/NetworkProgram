package Socket.tcp.demo_03;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
        int clientNum = 1;
        System.out.printf("准备启动%d个客户端 \n",clientNum);
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
        Writer writer;
        public void run() {
            try {
                client = new Socket("127.0.0.1",port);
                heartbeatDog heart = new heartbeatDog(client);
                writer = new OutputStreamWriter(client.getOutputStream());
                for (int i = 0;i<retries;i++){
                    handler();
                    heart.setFlag(true);
                    Thread thread = new Thread(heart);
                    thread.start();
                    Thread.sleep(1000);
                    heart.flag = false;
                }
                writer.write("Goodbye\r\n");
                writer.flush();
                System.out.println("发送完毕");
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void handler(){
            request();
//            serverResponse();
//            sb.delete(0,sb.length());
        }

        private void request(){
            try {
                writer.write("你好\n");
                writer.flush();
                Thread.sleep(50);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    static class heartbeatDog implements Runnable{

        private Socket heartbeat;
        private boolean flag;

        public heartbeatDog(Socket heartbeat) {
            this.heartbeat = heartbeat;
        }

        public boolean getFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void run() {
            Writer writer ;
            try {
                writer = new OutputStreamWriter(heartbeat.getOutputStream());
                while (getFlag()) {
                    writer.write("heartbeat\n");
                    writer.flush();
                    Thread.sleep(150);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
