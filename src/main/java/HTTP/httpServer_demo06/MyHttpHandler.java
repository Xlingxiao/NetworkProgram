package HTTP.httpServer_demo06;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class MyHttpHandler implements HttpHandler {

    ExecutorService clientConnectPool = null;

    public MyHttpHandler() {
        this.clientConnectPool = Executors.newFixedThreadPool(20);
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        workMan work = new workMan(httpExchange);
        clientConnectPool.submit(work);
    }




}
