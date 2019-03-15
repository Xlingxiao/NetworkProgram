package FTP.test5;/**
 * @创建人
 * @创建时间
 * @描述
 */


import java.io.IOException;

/**
 * @program: FTPtest
 * @description: 将一个整数平均分配给每个线程
 * @author: Ling
 * @create: 2018/09/06 13:25
 **/
public class getDistribution implements Runnable{

    private int count;
    private int threadNumber;

    private getDistribution(int count,int threadNumber){

        this.count = count;
        this.threadNumber = threadNumber;
    }

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
//        String path = "./code/tmp/url_list";
//        client.changeWorkingDirectory(path);
//        FTPFile[] files = client.listFiles();
//        System.out.println(files.length);
//        for (FTPFile file : files) {
//            System.out.println(file.getName());
//        }
//        client.logout();
//        client.disconnect();
        int threadNumber = 2;
        int count = 3;
        getDistribution D = new getDistribution(count,threadNumber);
        if (D.count < threadNumber){
            new Thread(D, String.valueOf(0)).start();

        }else{
            for (int i = 0; i < threadNumber; i++) {
                new Thread(D, String.valueOf(i)).start();
            }
        }
    }

    public void run() {
        if (threadNumber>count){
            System.out.printf("%d--%d\n",1,count);
        }else {
            int extra = count % threadNumber;
            int number = Integer.parseInt(Thread.currentThread().getName());
            int startPoint = number * (count / threadNumber);
            int endPoint = startPoint + (count / threadNumber);
            if(extra>number){
                startPoint += number+1;
                endPoint += number+1;
            }else if(extra <= number) {
                startPoint += extra+1;
                endPoint += extra;
            }
            System.out.printf("%d--%d\n",startPoint,endPoint);
//            for (int i = startPoint; i < endPoint; i++) {
//                System.out.println(Thread.currentThread().getName() + "    " + i);
//            }
        }
    }
}
