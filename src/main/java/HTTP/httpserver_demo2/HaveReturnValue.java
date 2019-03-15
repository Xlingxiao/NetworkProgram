package HTTP.httpserver_demo2;//package httpserver_demo2;
//
//import com.sun.security.ntlm.Server;
//
//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class HaveReturnValue {
//    private ServerSocket serverSocket ;
//    private int ServerPort = 8888;
//    public static void main(String[] args) {
//        HaveReturnValue haveReturnValue =new HaveReturnValue();
//        haveReturnValue.start();
//        haveReturnValue.stop();
//    }
//    //    启动方法
//    public void start(){
//        try {
//            serverSocket = new ServerSocket(ServerPort);
//            System.out.printf("服务端启动成功，开始监听%d端口\n",ServerPort);
//            Socket client = serverSocket.accept();
//            this.receive(client);
//            this.returnMessage(client);
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    //    接受消息
//    private void receive(Socket client){
//        try {
//            String msg = null;
////            存放客户端请求头的容器
//            StringBuilder sb = new StringBuilder();
////            获得客户端请求数据放入缓冲区
//            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
////            对缓冲区数据进行读出到容器中
//            while ((msg=br.readLine())!=null){
//                sb.append(msg);
////                读入时添加换行符
//                sb.append("\r\n");
//                if (msg.length()==0){
//                    break;
//                }
//            }
////            将数据转换为string类型方便输出并使用trim（）去掉首位的空格
//            String requstInfo = sb.toString().trim();
//            System.out.println(requstInfo);
////            System.out.print(sb.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void returnMessage(Socket client){
//        String type = "text/html";
//        String charset = "UTF-8";
//        String msg = getReturnData();
//        System.out.println(msg);
//        try {
//            OutputStream out = client.getOutputStream();
//            PrintWriter pw = new PrintWriter(out);
////            pw.println("HTTP/1.1 200 OK");
//////            pw.println("Server:zx Server/0.1");
//////            pw.println("Date: "+new Date());
////            pw.println("Content-Length: "+msg.getBytes().length);
////            pw.println("Content-type: "+type+";"+" charset="+charset);
//            StringBuilder sb = new StringBuilder();
//            sb.append("HTTP/1.1 200 OK");
//            sb.append("\r\n");
//            sb.append("Content-Length: "+msg.getBytes().length);
//            sb.append("\r\n");
//            sb.append("Content-type: "+type+";"+" charset="+charset);
//            sb.append("\r\n");
//            pw.println(sb);
//            pw.println();
//            pw.println(msg);
//            pw.flush();
//            pw.close();
//            out.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//    private String getReturnData(){
//        String encoding = "UTF-8";
//        String path = "success.html";
//        String uri = HaveReturnValue.class.getClassLoader().getResource(path).getPath();
//        File file = new File(uri);
//        Long flenth = file.length();
//        byte[] fileConnect = new byte[flenth.intValue()];
//        try {
//            FileInputStream fi = new FileInputStream(file);
//            fi.read(fileConnect);
//            fi.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            return new String(fileConnect,encoding);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//    //    停止服务
//    public void stop(){
//        try {
//            this.serverSocket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
