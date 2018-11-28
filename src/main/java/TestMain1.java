import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qiaoying
 * @date 2018/11/28 19:26
 */
public class TestMain1 {

    private final Lock lock = new ReentrantLock();

    private final Condition conditionA = lock.newCondition();

    private final Condition conditionB = lock.newCondition();

    private static boolean flag = true;

    public static void main(String[] args){
       TestMain1 testMain1 = new TestMain1();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(testMain1.new RunnableA());
        executorService.execute(testMain1.new RunnableB());

        executorService.shutdown();
    }

    private class RunnableA implements Runnable{
        public void run() {
            for (int i = 1; i <= 27; i++){
                lock.lock();
                try {
                   while (!flag){
                       try {
                           conditionA.await();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }

                   System.out.println(i);
                   flag = false;
                   conditionB.signal();

                }finally {
                    lock.unlock();
                }
            }
        }
    }


    private class RunnableB implements Runnable{
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++){
                lock.lock();
                try {
                   while (flag){
                       try {
                           conditionB.await();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
                   System.out.println(c);
                   flag = true;
                   conditionA.signal();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
