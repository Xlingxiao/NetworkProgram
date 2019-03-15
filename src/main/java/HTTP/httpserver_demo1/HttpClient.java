package HTTP.httpserver_demo1;

import java.io.*;
import java.net.Socket;


public class HttpClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8888);
        // 请求服务器  
        OutputStream out = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(out);
        pw.println("GET /index.html HTTP/1.1");  // 请求的第一行Request-Line，需要写请求的URL(/Test/test.jsp)
        pw.println("Host: localhost:8080");  // 请求头，Host是必须的
        pw.println();  // 一定要有个空行表示请求结束
        pw.flush();  // 提交请求

//         获取服务器响应
//        我们得到的二进制输入流一般情况下是 InputStream
//        应该将它转为字符流才能进行输出或者保存 就会用到转换流 InputStreamReader
//        使用转换流直接输出效率不高我们需要使用一个缓冲区装载转换流的输出
//        就会用到缓冲流 BufferedReader
//        如下三句代码也可以整合为一句
//        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
//        System.out.print(sb.toString());
    }

}
