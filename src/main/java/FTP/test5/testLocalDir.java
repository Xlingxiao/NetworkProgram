package FTP.test5;/**
 * @创建人
 * @创建时间
 * @描述
 */

import java.io.File;
import java.io.IOException;

/**
 * @program: FTPtest
 * @description: 测试本地文件夹获取文件名和文件路径的区别
 * @author: Ling
 * @create: 2018/09/07 09:00
 **/
public class testLocalDir {
    public static void main(String[] args) throws IOException {

//        Properties props = new Properties();
//        InputStream is = getDistribution.class.getClassLoader().getResourceAsStream("FTP.properties");
//        props.load(is);
//        String ip = props.getProperty("url");
//        int port = Integer.parseInt(props.getProperty("port"));
//        String user = props.getProperty("username");
//        String pw = props.getProperty("password");
//        FTPClient client = new FTPClient();
//        client.connect(ip,port);
//        client.login(user,pw);
//        client.enterLocalPassiveMode();
//        String path = "./code/tmp/";
//        client.changeWorkingDirectory(path);
//
//        FTPFile[] files = client.listDirectories();
//        System.out.println(files.length);
//        for (FTPFile file : files) {
//            System.out.println(file.getName());
//        }
//        client.logout();
//        client.disconnect();
        File dir = new File("E:/tmp/华山");
        File[] list = dir.listFiles();
        for (File f :
                list) {
            System.out.println(f.getName());
        }
        System.out.println(dir.getName());
    }
}
