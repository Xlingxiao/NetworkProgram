package HTTP.httpServer_demo06;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class httpServer {
    //    初始化url上下文
    private List<String> initContext() throws IOException {
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
    //    启动服务
    private void startServer(){
        try {
//            创建http服务器，指定绑定端口为8888
//            100是阻塞请求的数量
            HttpServer server = HttpServer.create(new InetSocketAddress(8888), 100);
//            createContext中第一个参数指定url
//            url指定为“/”时表示接受所有请求路径
//            读取文件中所有的url来创建上下文
            List<String> contexts = initContext();
            MyHttpHandler handler = new MyHttpHandler();
            for (String con : contexts){
                server.createContext(con,handler);
            }
            server.start();
            System.out.printf("HTTP服务器启动成功，请在浏览器打开%s\n",server.getAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        httpServer thisServer = new httpServer();
        thisServer.startServer();
    }
}