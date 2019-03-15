package HTTP.httpserver_demo04;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class httpServer {
    private static List<String> initContext() throws IOException {
        InputStream is = httpServer.class.getClassLoader().getResourceAsStream("conf/contextRout");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String content ;
        List<String> contexts = new LinkedList<String>();
        while (null!=(content=br.readLine())){
            contexts.add(content);
        }
        return contexts;
//        for (String con:contexts) {
//            System.out.println(con);
//        }
    }
    public static void main(String[] args) {

        try {
//            创建http服务器，指定绑定端口为8888
//            100是阻塞请求的数量
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 100);
//            createContext中第一个参数指定url
//            url指定为“/”时表示接受所有请求路径
//            读取文件中所有的url来创建上下文
            List<String> contexts = initContext();
            TestHandler handler = new TestHandler();
            for (String con : contexts){
                server.createContext(con,handler);
            }
            server.start();
            System.out.printf("HTTP服务器启动成功，请在浏览器打开%s\n",server.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static class TestHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
            String head ;
            while(null!=(head=br.readLine())){
                System.out.println(head);
            }
            String path = "success/index.html";
            String url = httpServer.class.getClassLoader().getResource(path).getPath();
            File file = new File(url);
            Long flenth = file.length();
            byte[] fileConnect = new byte[flenth.intValue()];
            FileInputStream fi = null;
            try {
                String requestUrl = exchange.getRequestURI().getPath();
                System.out.println(requestUrl);
                fi = new FileInputStream(file);
                fi.read(fileConnect);
                fi.close();
                    exchange.sendResponseHeaders(200, fileConnect.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileConnect);
                os.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static class Test2Handler implements HttpHandler{
        public void handle(HttpExchange exchange) throws IOException {
            String response = "hello world";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
