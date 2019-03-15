package FTP.test4;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;

import java.io.*;

public class Upload {
    /**
     * Description: 向FTP服务器上传文件
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(url, port);//连接FTP服务器
            ftp.login(username, password);//登录
//            获取链接状态码
            reply = ftp.getReplyCode();
//            验证链接状态码
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.enterLocalPassiveMode();
//            改变FTP工作路径为指定路径
            ftp.changeWorkingDirectory(path);
//            上传服务器有相同的文件名进行覆盖
//            两个参数分别是文件路径、文件输入流
            ftp.storeFile(filename, input);

            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }
//    下面两个的主要原理
//    根据不同的方式（读入文件、输入字符串）创建输入流
//    将输入流交给工具类
//    工具类使用输入流和文件路径创建文件将输入流中的内容传入文件
    @Test
    public void testUpLoadFromDisk(){
        try {
            //ftp服务器地址
            String hostname = "xx.xx.xx.xx";
            //ftp服务器端口号默认为21
            Integer port = 21 ;
            //ftp登录账号
            String username = "hadoop";
            //ftp登录密码
            String password = "123456";
//            字节型
            FileInputStream in=new FileInputStream(new File("E:/Code/GitHubCode/HttpServer.zip"));
            boolean flag = uploadFile(hostname, port, username, password, "./code/jupyter/tmp/", "proxies.txt", in);
            System.out.println(flag);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
//    在服务器上创建一个文件 将字符串写入文件中
    @Test
    public void testUpLoadFromString(){
        try {
//            字节型
            InputStream input = new ByteArrayInputStream("test ftp".getBytes("utf-8"));
            boolean flag = uploadFile("127.0.0.1", 21, "test", "test", "D:/ftp", "test.txt", input);
            System.out.println(flag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
