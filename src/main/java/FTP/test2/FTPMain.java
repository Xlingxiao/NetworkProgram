package FTP.test2;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class FTPMain {

    private static Logger logger = Logger.getLogger(FTPMain.class);

    public static void main(String[] args) {
        int ftpPort = 21;
        String ftpUserName = "root";
        String ftpPassword = "123456";
        String ftpHost = "127.0.0.1";
        String ftpPath = "/root/temp/";
//        String writeTempFielPath = "";
        try {
            InputStream in = FtpUtil.class.getClassLoader()
                    .getResourceAsStream("env.properties");
            if (in == null) {
                logger.info("配置文件env.properties读取失败");
            } else {
                Properties properties = new Properties();
                properties.load(in);
                ftpUserName = properties.getProperty("ftpUserName");
                ftpPassword = properties.getProperty("ftpPassword");
                ftpHost = properties.getProperty("ftpHost");
                ftpPort = Integer.valueOf(properties.getProperty("ftpPort")).intValue();
                ftpPath = properties.getProperty("ftpPath");
//                writeTempFielPath = properties.getProperty("writeTempFielPath");

                ReadFtpFile read = new ReadFtpFile();
                String result = read.readConfigFileForFTP(ftpUserName, ftpPassword, ftpPath, ftpHost, ftpPort, "test");
                System.out.println("读取配置文件结果为：" + result);

//                WriteFtpFile write = new WriteFtpFile();
//                ftpPath = ftpPath + "/" + "huawei_220.248.192.200_new1.cfg";
//                write.upload(ftpPath, ftpUserName, ftpPassword, ftpHost, ftpPort, result, writeTempFielPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}