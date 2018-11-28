import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author qiaoying
 * @date 2018/11/28 19:57
 */
public class TestMain2 {

    private static boolean flag = true;

    private final Object object = new Object();

    public static void main(String[] args){

        TestMain2 testMain2 = new TestMain2();

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.execute(testMain2.new RunnableA());
        executorService.execute(testMain2.new RunnableB());

        executorService.shutdown();
    }


    private class RunnableA implements Runnable{
        public void run() {
            for (int i = 1; i <= 27; i++){
                synchronized (object){
                    if (!flag){
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(i);
                    flag = false;
                    object.notifyAll();

                }
            }
        }
    }


    private class RunnableB implements Runnable{
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++){
                synchronized (object){
                    if (flag){
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(c);
                    flag = true;
                    object.notifyAll();
                }
            }
        }
    }
}
