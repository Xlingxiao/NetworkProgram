package HTTP.httpServer_demo06;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: HttpServer
 * @description: 使用post方式测试httpServer
 * @author: Ling
 * @create: 2018/08/29 10:58
 **/
public class Client_Post {
//    测试线程数量
    private static int threadNum = 20;
//    每个线程测试次数
    private static int testNum = 100;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threadList = new ArrayList<Thread>(threadNum);
        for(int i = 0 ;i<threadNum;i++){
            Thread thread = new Thread(new myThread());
            threadList.add(thread);
            thread.start();
        }
        ThreadLocal<Long> startTime = new ThreadLocal<Long>();
        ThreadLocal<Long> endTime = new ThreadLocal<Long>();
        startTime.set(System.currentTimeMillis());
        for (Thread thread :
                threadList) {
            thread.join();
        }
        endTime.set(System.currentTimeMillis());
        long t = endTime.get() - startTime.get();
        System.out.println(Thread.currentThread().getName()+"运行了 "+t+" ms");
    }
    static class myThread implements Runnable{
        private String firstLine = "POST /index.html HTTP/1.1";
        private String secondLine = "Host: localhost:8888";
        private PrintWriter pw ;
        private StringBuilder sb ;

        public void run() {
            Socket socket;
            for (int i = 0 ;i <testNum;i++){
                try {
                    socket = new Socket("127.0.0.1", 8888);
                    request(socket,i);
                    response(socket);
                    socket.close();
                    System.out.println(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private void request(Socket socket,int i){
            OutputStream out;
            String msg = Thread.currentThread().getName()+"----"+i;
            long contentLength = msg.getBytes().length;
            try {
                out = socket.getOutputStream();
                pw = new PrintWriter(out);
                pw.println(firstLine);  // 请求的第一行Request-Line，需要写请求的URL(/Test/test.jsp)
                pw.println(secondLine);  // 请求头，Host是必须的
                pw.println("Content-Length: "+contentLength);
                pw.println();  // 一定要有个空行表示请求结束
                pw.println(msg);
                pw.flush();  // 提交请求
                socket.shutdownOutput();
            } catch (IOException e) {
                System.out.println("请求失败");
            }
        }
        private void response(Socket socket){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg ;
                sb = new StringBuilder();
                while (null!=(msg = br.readLine())){
                    sb.append(msg).append("\r\n");
                }
                socket.shutdownInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
