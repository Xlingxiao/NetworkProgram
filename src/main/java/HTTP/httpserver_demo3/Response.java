package HTTP.httpserver_demo3;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Response {
    private int code;
    private String charset = "UTF-8";
    private String type ="text/html";
    private String returnConnect;
//    服务器将要返回的文件地址
    private String path;
//    客户端传来的相对地址
    private String uri;
    private Socket client;

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public Response(String uri, Socket client) {
        this.client = client;
        this.uri = uri;
    }

    public Response() {

    }

    private void serverException() {
        path = "Exception/500.html";
    }

    private void success() {
        path = "success/"+uri;
    }

    private void notFound() {
        path = "Exception/404.html";
    }
    public void process(){
        code=getCode();
        if (code==404){
            this.notFound();
        }
        else if(code==200){
            this.success();
        }
        else if(code ==500){
            this.serverException();
        }
        this.Sender();
    }

    public int getCode(){
        if(uri.equals("/"))
            uri="/success";
        if(uri.indexOf(".html")==-1)
            uri = uri.substring(1)+".html";
//        URL url = addException.class.getClassLoader().getResource("Exception/"+uri.substring(1));
        URL url = addException.class.getClassLoader().getResource("success/"+uri);
        if (url==null){
            return 404;
        }
        else
            return 200;
    }
    private void Sender(){
        OutputStream out = null;
        try {
            out = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(out);
//        先得到响应内容，设置响应头的时候就能设置内容的字节长度了
        returnConnect = responseContent();
        pw.println(responseHead());
        pw.println();
        pw.println(returnConnect);
        pw.flush();
//        try {
//            pw.close();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
//    返回响应头
    private StringBuilder responseHead(){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK");
        sb.append("\r\n");
        sb.append("Content-Length: "+returnConnect.getBytes().length);
        sb.append("\r\n");
        sb.append("Content-type: "+type+";"+" charset="+charset);
        sb.append("\r\n");
        return sb;
    }
//    返回响应内容
    private String responseContent(){
        String encoding = "UTF-8";
        String url = Response.class.getClassLoader().getResource(path).getPath();
        File file = new File(url);
        Long flenth = file.length();
        byte[] fileConnect = new byte[flenth.intValue()];
        try {
            FileInputStream fi = new FileInputStream(file);
            fi.read(fileConnect);
            fi.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileConnect,encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
