package HTTP.httpserver_demo3;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class testClient {
    private static ExecutorService clientpool ;
    public static void main(String[] args) throws InterruptedException {
//        clientpool = Executors.newFixedThreadPool(20);
        for (int i = 0 ;i<20;i++) {
            myThreadClient client = new myThreadClient();
//            clientpool.submit(client);
            new Thread(client).start();
        }
    }
}

class myThreadClient implements Runnable{

    private String firstLine = "GET /index.html HTTP/1.1";
    private String secondLine = "Host: localhost:8888";

    public void run() {
        Socket socket = null;
        ThreadLocal<Long> startTime = new ThreadLocal<Long>();
        ThreadLocal<Long> endTime = new ThreadLocal<Long>();
        startTime.set(System.currentTimeMillis());
        try {
            for(int i = 0;i<100;i++){
                socket = new Socket("localhost", 8888);
                // 请求服务器
                OutputStream out = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(out);
                pw.println(firstLine);  // 请求的第一行Request-Line，需要写请求的URL(/Test/test.jsp)
                pw.println(secondLine);  // 请求头，Host是必须的
                pw.println();  // 一定要有个空行表示请求结束
                pw.flush();  // 提交请求
                InputStream is = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(reader);
                // 输出响应内容
                String msg = null;
                StringBuilder sb = new StringBuilder();
                while ((msg=br.readLine())!= null) {
    //            System.out.print((char)reader.read());
                    sb.append(msg);
                    sb.append("\r\n");
                    System.out.println(msg);
                }
                pw.close();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        endTime.set(System.currentTimeMillis());
        long t = startTime.get()-endTime.get();
        System.out.println(Thread.currentThread().getName()+"---"+t);
    }
}