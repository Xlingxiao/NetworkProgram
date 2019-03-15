package FTP.test4;


import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;

public class ShowFileName {
    /**
     * Description: 查看服务器指定路径下所有文件
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @return
     */
    public boolean ShowFileNameUtil(String url, int port, String username, String password, String remotePath){
        boolean success = false;
        FTPClient ftpClient = new FTPClient();
        try{
//            状态码
            int reply ;
//            设置编码
            ftpClient.setControlEncoding("UTF-8");
//            设置链接ip
            ftpClient.connect(url,port);
//            登录
            ftpClient.login(username,password);
//            得到状态码
            reply = ftpClient.getReplyCode();
//            匹配状态码
            if(!FTPReply.isPositiveCompletion(reply)){
                System.out.println("登录失败");
                ftpClient.disconnect();
                return success;
            }
            ftpClient.enterLocalPassiveMode();
            printListName(ftpClient,remotePath);
            success = true;
            ftpClient.logout();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }
    private void printListName(FTPClient ftpClient, String path){
        try {
            boolean ff = ftpClient.changeWorkingDirectory(path);
            if (ff){
                FTPFile[] fs = ftpClient.listFiles();
//                for(FTPFile file :fs){
                for (int i = 0;i<7;i++){
                    if (fs.length<=i)
                        break;
                    FTPFile file = fs[i];
                    if (file.isDirectory()){
//                        System.out.println(file.getGroup());
//                        通过ftpClient.printWorkingDirectory()
//                        获得当前工作路径和工作名一起加入path就可以绝对定位一个文件
                        System.out.println(ftpClient.printWorkingDirectory());
                        printListName(ftpClient,ftpClient.printWorkingDirectory()+"/"+file.getName());
                    }
                    else if(file.isFile()){
                        System.out.println(path+"/"+file.getName());
                    }
                }
                ftpClient.changeToParentDirectory();
            }
            else
                System.out.println("转换工作路径失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void showName(){
        //ftp服务器地址
        String hostname = "127.0.0.1";
        //ftp服务器端口号默认为21
        Integer port = 21 ;
        //ftp登录账号
        String username = "hadoop";
        //ftp登录密码
        String password = "123456";
        //需要查看的路径
//        若指定路径不存在将返回整个ftp服务器上的文件名
        String remotePath = "./code/jupyter";
        boolean flag = ShowFileNameUtil(hostname,port,username,password,remotePath);
        System.out.println(flag);

    }
}
