package FTP.test5;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @创建人
 * @创建时间
 * @描述
 */
class getAllPath {

    BlockingQueue<String> queue = new ArrayBlockingQueue(1000);

    long timestamp;

    private static FTPClient getFtpClient() {
        Properties props = new Properties();
        InputStream is = getAllPath.class.getClassLoader().getResourceAsStream("FTP.properties");
        try {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = props.getProperty("url");
        Integer port = Integer.valueOf(props.getProperty("port"));
        String username = props.getProperty( "username");
        String password =  props.getProperty("password");
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
                return null;
            }
//            nat网络环境下需要设置为被动模式
            ftp.enterLocalPassiveMode();
        } catch (SocketException e){
            System.out.println("连接ftp服务器出错");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
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
//                    获得文件时间戳
                    if (file.isDirectory()){
//                        通过ftpClient.printWorkingDirectory()
//                        获得当前工作路径和工作名一起加入path就可以绝对定位一个文件
                        printListName(ftpClient,ftpClient.printWorkingDirectory()+"/"+file.getName());
                    }
                    else if(file.isFile()){
                        long time = file.getTimestamp().getTime().getTime()+28800000;
                        System.out.println(time);
                        if (time<timestamp)
                            continue;
                        Date date = new Date(time);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String fileTime = simpleDateFormat.format(date);

                        System.out.println(
                                new String((path+"/"+file.getName()).
                                        getBytes("iso-8859-1"),"UTF-8")
                                +"\t"+fileTime
                        );
                        System.out.println(file.toFormattedString());
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

    private void setTimeStamp(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date reDate =simpleDateFormat.parse(date);
            timestamp = reDate.getTime();
        } catch (ParseException e) {
            System.out.println("本地时间输出错误，参考格式\t1970-01-01 08:00:00");
            timestamp = 0;
            e.printStackTrace();
        }
        System.out.println(timestamp);
    }

    @Test
    void testShow() {
        getAllPath path = new getAllPath();
        path.setTimeStamp("2018-08-24 08:00:00");
        FTPClient ftpClient = getFtpClient();
        assert ftpClient != null;
        path.printListName(ftpClient,"./code/tmp/url_list");
        String msg;
        System.out.print("\n\n\n\n");
        while (null!=(msg=path.queue.poll())){
            System.out.println(msg);
        }
    }
}
