package FTP.test4;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Download {
    /**
     * Description: 从FTP服务器下载文件
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public static boolean downFile(String url, int port,String username, String password, String remotePath,String fileName,String localPath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(url, port);
            ftp.login(username, password);//登录
//            获取链接状态码
            reply = ftp.getReplyCode();
//            验证链接状态码
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.setControlEncoding("UTF-8");
//            nat网络环境下需要设置为被动模式
            ftp.enterLocalPassiveMode();
            //转移到FTP服务器目录
            ftp.changeWorkingDirectory(remotePath);
//            得到工作路径下的所有文件
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff:fs){
                if(ff.getName().equals(fileName)){
//                    判断是文件夹或者文件可以用
//                    isFile()  isDirectory()
                    if(ff.isDirectory()){
                        System.out.println(ff.getName()+"：是一个文件夹，不能直接下载");
                        return success;
                    }
                    File localFile = new File(localPath+"/"+ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
//                    也可以选择读取文件的内容
//                    InputStream is = ftp.retrieveFileStream(fileName);
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
//                    String msg = "";
//                    while ((msg=br.readLine())!= null) {
//                        System.out.println(msg);
//                    }
//                    is.close();
                }
            }
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

    @Test
    public void testUpLoadFromString(){
        //ftp服务器地址
        String hostname = "xx.xx.xx.xx";
        //ftp服务器端口号默认为21
        Integer port = 21 ;
        //ftp登录账号
        String username = "hadoop";
        //ftp登录密码
        String password = "123456";
//            本地路径
        String localPath = "D:/Game";
        boolean flag = downFile(hostname, port, username, password, "./code/tmp/", "url_list",localPath);
        System.out.println(flag);

    }
}
