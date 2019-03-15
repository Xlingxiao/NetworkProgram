package FTP.test3;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

public class ftptest {

    public static void main(String[] args) throws IOException {
        //ftp服务器地址
        String hostname = "127.0.0.1";
        //ftp服务器端口号默认为21
        Integer port = 21 ;
        //ftp登录账号
        String username = "hadoop";
        //ftp登录密码
        String password = "123456";
        FTPClient ftpClient = null;
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        System.out.println("connecting...ftp服务器:"+hostname+":"+port);
        ftpClient.connect(hostname, port); //连接ftp服务器
        ftpClient.login(username, password); //登录ftp服务器
        int replyCode = ftpClient.getReplyCode(); //是否成功登录服务器
        if(!FTPReply.isPositiveCompletion(replyCode)){
        System.out.println("connect failed...ftp服务器:"+hostname+":"+port);
        }
        else{
            System.out.println("connect successfu...ftp服务器:"+hostname+":"+port);
            String[] flist=ftpClient.listNames();
            for (String fname:flist){
                System.out.println(fname);
                String[] SecFList=ftpClient.listNames(fname);
                for(String name:SecFList){
                    System.out.println(name);
                }
            }
            System.out.println("ok");
            ftpClient.logout();
        }
    }

}
