import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author qiaoying
 * @date 2018/11/28 20:14
 */
public class TestMain3 {

    private final Semaphore semaphore = new Semaphore(1);

    private static boolean flag = true;

    public static void main(String[] args){
       TestMain3 testMain3 = new TestMain3();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(testMain3.new RunnableA());
        executorService.execute(testMain3.new RunnableB());

        executorService.shutdown();

    }

    private class RunnableA implements Runnable{
        public void run() {
            for (int i = 1; i <= 27; i++){
                try {
                    semaphore.acquire();
                    while (!flag){
                        semaphore.release();
                    }
                    System.out.println(i);
                    flag = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    private class RunnableB implements Runnable{
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++){
                try {
                    semaphore.acquire();
                    while (flag){
                        semaphore.release();
                    }
                    System.out.println(c);
                    flag = true;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }
}
