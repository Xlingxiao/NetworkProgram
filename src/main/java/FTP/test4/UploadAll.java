package FTP.test4;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

/**
 * @program: FTPTest
 * @description: 上传一个文件夹内所有文件
 * @author: Ling
 * @create: 2018/08/31 17:36
 **/
public class UploadAll {

    private Properties props = new Properties();

    //    获取FTP对象
    public FTPClient getFtpClient() {
//        读取配置文件
        props = new Properties();
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

    //    改变FTP工作路径为指定路径
    public boolean changeWorkPath(FTPClient ftp,String workPath){
//        flag用于判断转变工作空间是否成功
        boolean flag = false;
        try {
            flag = ftp.changeWorkingDirectory(workPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

//    上传单个文件
    public void uploadFile(FTPClient ftp, String localFileName, String targetFilename) {

//        两个参数分别是文件路径、文件输入流
        try {
//            上传服务器有相同的文件名进行覆盖
            File file = new File(localFileName);
            FileInputStream input=new FileInputStream(file);
            ftp.storeFile(targetFilename, input);
            input.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

//    在服务器创建文件夹
    public void createRemoteDir(FTPClient ftpClient, String dirName) throws IOException {
//        使用flag判断文件夹是否存在
        boolean flag = false;
        FTPFile[] listDir = ftpClient.listDirectories();
        for (FTPFile dir : listDir) {
            if (dir.getName().equals(dirName))
                flag = true;
        }
        if (!flag)
            ftpClient.makeDirectory(new String(dirName.getBytes("utf-8"),"iso-8859-1"));
    }

//    递归遍历本地文件夹内所有文件
    public void startUpload(FTPClient ftpClient ,String initLocalPath, String targetPath) throws IOException, InterruptedException {
//        更改文件名编码
        targetPath = new String(targetPath.getBytes("utf-8"),"iso-8859-1");
        changeWorkPath(ftpClient,targetPath);
//        将编码转回来
        targetPath = new String(ftpClient.printWorkingDirectory().getBytes("iso-8859-1"),"utf-8");
        System.out.println(targetPath);
//        遍历本地文件夹
        File filePath = new File(initLocalPath);
        File[] listFile = filePath.listFiles();
        for (File f : listFile) {
//            判断是文件夹递归
            if (f.isDirectory()){
                String fName = f.getName();
//                判断是文件夹之后尝试在服务器创建文件夹
                createRemoteDir(ftpClient,fName);
//                递归调用该函数，初始文件夹变为判断的这个文件夹，目标文件夹改为子文件夹，
                startUpload(ftpClient,f.getPath(),targetPath+"/"+fName);
//            判断是文件-->上传
            }else if(f.isFile()){
                uploadFile(ftpClient,initLocalPath+"/"+f.getName(),f.getName());
                System.out.println("成功 "+initLocalPath+"/"+f.getName());
                Thread.sleep(5000);
            }
        }
//        遍历一个文件夹之后，将工作空间转为上层目录
        ftpClient.changeToParentDirectory();

    }

//    因为本来就是做测试用的，异常全部抛出
    public static void main(String[] args) throws IOException, InterruptedException {
        UploadAll uploadAll = new UploadAll();
        FTPClient ftpClient = uploadAll.getFtpClient();
        String initPath = "E:/tmp/华山";
        String targetPath = "./code/tmp/Up";
        if(uploadAll.changeWorkPath(ftpClient,targetPath)){
            File localDir = new File(initPath);
            String localDirName = localDir.getName();
            uploadAll.createRemoteDir(ftpClient,localDirName);
            System.out.println(ftpClient.printWorkingDirectory());
            uploadAll.startUpload(ftpClient,initPath,"./"+localDirName);
            System.out.println("SUCCESS");
        }
    }
}
