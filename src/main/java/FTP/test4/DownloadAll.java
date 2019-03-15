package FTP.test4;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.SocketException;
import java.util.Properties;


/**
 * @创建人
 * @创建时间
 * @描述
 */
public class DownloadAll {

//    获取FTPClient对象
    public FTPClient getFtpClient() {
//        读取配置文件
        Properties props = new Properties();
        InputStream is = UploadAll.class.getClassLoader().getResourceAsStream("FTP.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("url");
        Integer port = Integer.valueOf(props.getProperty("port"));
        String username = props.getProperty( "username");
        String password =  props.getProperty("password");
//        根据配置文件创建FTP对象
        FTPClient ftp = new FTPClient();
        try {
            int reply;
//            如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
            ftp.connect(url, port);
            ftp.login(username, password);//登录
//            获取链接状态码
            reply = ftp.getReplyCode();
//            验证链接状态码
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return null;
            }
            ftp.setControlKeepAliveReplyTimeout(10*1000);
            ftp.setDataTimeout(10*1000);
//            nat网络环境下需要设置为被动模式
            ftp.enterLocalPassiveMode();
        } catch (SocketException e){
            System.out.println("连接ftp服务器出错");
            return null;
        } catch (IOException e) {
            System.out.println("获取配置文件出错");
            return null;
        }
        return ftp;
    }

//    递归下载
    private static void saveAllContent(FTPClient ftp, String remotePath, String localPath){
        boolean flag ;
        try {
//            转移到FTP服务器目录
//            FTP协议默认只支持iso-8859-1的编码格式，
//            这里我们转换中文文件名为字节形式
//            将字节形式转为iso-8859-1的编码
            remotePath = new String(remotePath.getBytes("UTF-8"),"iso-8859-1");
            flag = ftp.changeWorkingDirectory(remotePath);
            if (flag){
//                得到工作路径下的所有文件
                FTPFile[] fs = ftp.listFiles();
                for(int i = 0 ; i<5;i++){
//                    判断文件路径下没有那么多文件的话就退出
                    if (fs.length<=i)
                        break;
                    FTPFile ff = fs[i];
                    if(ff.isDirectory()){
//                        下面三行用来创建本地文件夹
                        File file = new File(localPath+"/"+ff.getName());
                        if (!file.exists())
                            file.mkdir();
                        System.out.println(ff.getName());
                        saveAllContent(ftp,ff.getName(),localPath+"/"+ff.getName());
                    }
                    else if(ff.isFile()){
                        File localFile = new File(localPath+"/"+ff.getName());
                        OutputStream is = new FileOutputStream(localFile);
                        ftp.retrieveFile(ff.getName(), is);
                        is.close();
                    }
                }
                ftp.changeToParentDirectory();
            }
            else
                System.out.println("切换工作路径失败");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void TestDownAll(){
        //目标路径
        String remotePath ="./code/tmp/Up/华山";
        //本地路径
        String localPath = "E:/tmp";

        FTPClient ftpClient = getFtpClient();

        if (ftpClient!=null){
            saveAllContent(ftpClient,remotePath,localPath);
        }
        System.out.println("处理结束");
    }
}
