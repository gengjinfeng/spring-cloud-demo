package mt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockAndCondition {

//    private Mutex mutex = new Mutex();
//    private ReentrantLock reentrantLock = new ReentrantLock();
//    private CountDownLatch countDownLatch = new CountDownLatch(4);
//
//    ReentrantLock lock = new ReentrantLock();
//    Condition condition = lock.newCondition();
//
//    private Lock lock = reentrantLock.lo


    /**
     * 传统的synchronized和Lock实现等待唤醒通知的约束：
     *
     * 线程需要获得并持有锁，必须在锁块（synchronized或lock中）
     * 必须要先等待后唤醒，线程才能够被唤醒。
     * @param args
     */

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService.submit(()->{
                lock.lock();
                System.out.println(Thread.currentThread().getId()+" - 启动..."+ finalI);
                try {
                    System.out.println(Thread.currentThread().getId()+" - 睡眠..."+ finalI);
                    condition.await();
                    System.out.println(Thread.currentThread().getId()+" - 被唤醒..."+ finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            });

            executorService.submit(()->{
                lock.lock();
                System.out.println(Thread.currentThread().getId()+" - 唤醒线程 启动..."+ finalI);
                try {
                    Thread.sleep(5000); //必须先
                    System.out.println(Thread.currentThread().getId()+" - 唤醒线程 唤醒..."+ finalI);
                    condition.signal();
                    System.out.println(Thread.currentThread().getId()+" - 唤醒线程 唤醒完毕..."+ finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    //如果此处注释，则不会真正的唤醒
                    lock.unlock();
                }
            });
        }



    }

}
