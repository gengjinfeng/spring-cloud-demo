package mt;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;

/**
 * 生产消费队列
 */
public class CustomeNoReentrantLockMain {
    final static MutexAQS customeNoReentrantLock = new MutexAQS();
    final static Condition condition = customeNoReentrantLock.newCondition();
    final static Condition condition2 = customeNoReentrantLock.newCondition();
    final static Queue<String> q = new LinkedBlockingQueue<>();
    final static int qSize = 20;

    public static void main(String[] args) {
        List<Thread> threadProducerList = new ArrayList<>();
        List<Thread> threadConsumerList = new ArrayList<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //获取独占锁
                customeNoReentrantLock.lock();
                System.out.println("producer current thread name:" + Thread.currentThread().getName() + " get lock");
                try {
                    //队列如果满了就不往队列中添加元素，并等待释放锁
                    while (qSize == q.size()) {
                        System.out.println("current thread name:" + Thread.currentThread().getName() + " call wait");
                        condition2.await();
                    }
                    //向队列中添加数据
                    q.add("add a element to q");
                    System.out.println("add element end");
                    //唤醒消费线程
                    condition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    customeNoReentrantLock.unlock();
                }
            }
        };

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                //获取独占锁
                customeNoReentrantLock.lock();
                System.out.println("customer current thread name:" + Thread.currentThread().getName() + " get lock");
                try {
                    //队列为空则等待往队列中添加
                    while (0 == q.size()) {
                        System.out.println("current thread name:" + Thread.currentThread().getName() + " call wait");
                        condition.await();
                    }
                    //消费
                    String pop = q.poll();
                    System.out.println("poll element end");
                    //唤醒生产线程
                    condition2.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    customeNoReentrantLock.unlock();
                }
            }
        };

        for (int i = 0; i < 20; i++) {
            threadProducerList.add(new Thread(runnable, "thread0" + i));
            threadConsumerList.add(new Thread(runnable1, "thread0" + i));
        }
        for (int i = 0; i < 20; i++) {
            threadProducerList.get(i).start();
            threadConsumerList.get(i).start();
        }
    }
}
