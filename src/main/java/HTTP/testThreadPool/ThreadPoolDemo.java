package HTTP.testThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {
    public static void main(String[] args) {
        ExecutorService testPool = Executors.newFixedThreadPool(10);
        TestPool test = new TestPool();
        for (int i = 0; i < 10; i++) {
            testPool.submit(test);
        }
        testPool.shutdown();
    }
}

class TestPool implements Runnable{
    public void run() {
        int sum =0;
        for (int i = 0; i < 100; i++) {
            sum +=i;
        }
        System.out.println(Thread.currentThread().getName()+"--->"+sum);
    }
}
