package HTTP.httpServer_demo06;

import java.io.*;
import java.net.Socket;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class Client_Get {
    public static void main(String[] args) {
        for(int i = 0 ;i<1;i++){
            new Thread(new myThread()).start();
        }

    }
    static class myThread implements Runnable{
        private String firstLine = "GET /index.html HTTP/1.1";
        private String secondLine = "Host: localhost:8888";
        public void run() {
            Socket socket = null;
            ThreadLocal<Long> startTime = new ThreadLocal<Long>();
            ThreadLocal<Long> endTime = new ThreadLocal<Long>();
            startTime.set(System.currentTimeMillis());
            for (int i = 0 ;i <1;i++){
                try {
                    socket = new Socket("127.0.0.1", 8888);
                    OutputStream out = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(out);
                    pw.println(firstLine);  // 请求的第一行Request-Line，需要写请求的URL(/Test/test.jsp)
                    pw.println(secondLine);  // 请求头，Host是必须的
                    pw.println();  // 一定要有个空行表示请求结束
                    pw.flush();  // 提交请求
                    BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String msg ;
                    StringBuilder sb = new StringBuilder();
                    while (true){
                        msg = br.readLine();
                        if(msg==null)
                            break;
                        System.out.println(msg);
//                        sb.append(msg);
//                        sb.append("\r\n");
                    }
//                    System.out.println(sb.toString());
                    br.close();
                    pw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 请求服务器
            }
            endTime.set(System.currentTimeMillis());
            long t = startTime.get()-endTime.get();
            System.out.println(Thread.currentThread().getName()+"运行了 "+t/1000+" 秒");
        }
    }
}
