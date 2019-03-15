package HTTP.httpserver_demo3;

import java.io.IOException;
import java.net.Socket;

public class StartWork implements Runnable{
    private volatile Socket client ;
    private Request request = new Request();
    private Response response = new Response();

    public StartWork(Socket client) {
        this.client = client;
    }
    public void run(){

        request.setClient(client);

        request.receive();
//            得到客户端的输入url
        String uri = request.getUrl();
//            将uri传给response
        response.setClient(client);
        response.setUri(uri);
        response.process();
        try {
//            if (!client.isClosed())
                client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
