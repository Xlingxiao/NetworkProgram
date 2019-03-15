package HTTP.httpserver_demo3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private Socket client;
    private String mode;
    private String url;
    private String ob;
    public Request() {

    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public String getMode() {
        return mode;
    }

    public String getUrl() {
        return url;
    }


    public String getOb() {
        return ob;
    }

    void receive(){
        try {
            String tem = null;
            List<String> msg = new ArrayList<String>();
//            存放客户端请求头的容器
            StringBuilder sb = new StringBuilder();
//            获得客户端请求数据放入缓冲区
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            对缓冲区数据进行读出到容器中
            while ((tem=br.readLine())!=null){
                sb.append(tem);
//                装入列表
                msg.add(tem);
//                读入时添加换行符
                sb.append("\r\n");
                if (tem.length()==0){
                    break;
                }
            }
//            br.close();
//            System.out.println(sb.toString());
//            将数据转换为string类型方便输出并使用trim（）去掉首位的空格
//            String requstInfo = sb.toString().trim();
//            System.out.println(requstInfo);
//            输出请求头
//            System.out.print(sb.toString());
//            System.out.println(msg.get(0));
//            得到请求方法和请求地址
            String[] str ;
            tem = msg.get(0);
            str = tem.split(" ");
//            System.out.printf("%s\n%s\n", str[0], str[1]);
            url = str[1];
            mode = str[0];
            tem = msg.get(1);
            str = tem.split(" ");
            ob = str[1];
//            System.out.printf("请求方式：%s\nurl:%s\nob:%s",mode,url,ob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
