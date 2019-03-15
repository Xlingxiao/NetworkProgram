package Socket.tcp.longConnect.ReadEveryMsg;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: Socket
 * @description: 两个线程一个发送消息，一个定期向服务器发送心跳信息
 * @author: Ling
 * @create: 2018/09/11 15:51
 **/
public class Client {
    public static void main(String[] args) {
        String ip = "127.0.0.1";
        int port = 4096;
        try {
            Socket client = new Socket(ip,port);
            System.out.printf("开始连接服务器%s:%d\n",ip,port);
            myJob(client);
        } catch (IOException e) {
            System.out.println("连接服务器失败！");
            e.printStackTrace();
        }
    }

    private static void myJob(Socket client){
        ExecutorService heartbeatThread = Executors.newSingleThreadExecutor();
        MyFlag flag = new MyFlag();
        MyHeart heart = new MyHeart(client,flag);
        try {
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
            int sleepDuration = 1000;
            for (int i = 0; i < 3; i++) {
                writer.write(format.format(new Date()));
                writer.write("\n");
                writer.flush();
                Thread.sleep(sleepDuration);
            }
            flag.setFlag(true);
            heartbeatThread.submit(heart);
            Thread.sleep(sleepDuration*5);

            flag.setFlag(false);
            writer.write("second  ");
            writer.write(format.format(new Date()));
            writer.write("\n");
            writer.flush();
            flag.setFlag(true);
            heartbeatThread.submit(heart);
            Thread.sleep(sleepDuration * 4);

            flag.setFlag(false);
            writer.write("end\n");
            writer.flush();
            System.out.println("客户端发送完毕");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(!heartbeatThread.isShutdown()){
                heartbeatThread.shutdown();
            }

        }
    }
}

class MyFlag {

    boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}

class MyHeart implements Runnable{

    Socket client;
    MyFlag flag;

    public MyHeart(Socket client,MyFlag flag) {
        this.client = client;
        this.flag = flag;
    }

    public void run() {
        try {
            Writer writer = new OutputStreamWriter(client.getOutputStream());
            while (flag.isFlag()){
                writer.write("HEARTBEAT_CONTENT\n");
                writer.flush();
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}