package HTTP.httpServer_demo06;

import com.sun.net.httpserver.HttpExchange;

import java.io.*;

/**
 * @创建人
 * @创建时间
 * @描述
 */
public class workMan implements Runnable {

    HttpExchange httpExchange ;
//    请求方式
    String requestMethod ;
//    请求地址
    String requestUrl ;
//    响应地址
    String responseUrl;
//    请求内容
    StringBuffer sb = new StringBuffer();

    public workMan(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public void run() {
        try {
            httpRequest(httpExchange);
            httpResponse(httpExchange);
            httpExchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void httpRequest(HttpExchange exchange) throws IOException {
        requestMethod = exchange.getRequestMethod();
        requestUrl = exchange.getRequestURI().getPath();
        responseUrl = "success"+requestUrl;
        System.out.printf("请求方式: %s \t请求路径: %s\n", requestMethod, requestUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String body;
        while (null != (body = br.readLine())) {
            System.out.println(body);
            sb.append(body);
        }
        br.close();
    }

    private void httpResponse(HttpExchange exchange) {
        File file = null;
        Long flenth = null;
        byte[] fileConnect = null;
        String url;
        try {
            url = MyHttpHandler.class.getClassLoader().getResource(responseUrl).getPath();
            file = new File(url);
            flenth = file.length();
            fileConnect = new byte[flenth.intValue()];
        }catch (Exception e){
            System.out.println("请求地址在上下文中有但是没有响应的html");
            responseUrl = "Exception/404.html";
            httpResponse(exchange);
        }
        FileInputStream fi = null;
        try {
//            String requestUrl = exchange.getRequestURI().getPath();
//            System.out.println(requestUrl);
            fi = new FileInputStream(file);
            fi.read(fileConnect);
            fi.close();
            exchange.sendResponseHeaders(200, fileConnect.length);
            OutputStream os = exchange.getResponseBody();
            os.write(fileConnect);
//            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
