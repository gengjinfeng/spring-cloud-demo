package mt;

import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport比Object的wait/notify有两大优势：
 * LockSupport不需要在同步代码块里 。所以线程间也不需要维护一个共享的同步对象了，实现了线程间的解耦。
 * unpark函数可以先于park调用，所以不需要担心线程间的执行的先后顺序。
 *
 * Object类中的wait、notify、notifyAll用于线程等待和唤醒的方法，都必须在sychronized内部执行（必须用到关键字sychronized），且成对出现使用
 * 先wait,再notify才可以。
 */
public class WaitAndNotifyTest {

    public static void main1(String[] args) throws InterruptedException {
        final Object obj = new Object();
        Thread A = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            try {
                synchronized (obj) { //wait和notify/notifyAll方法只能在同步代码块里用
                    obj.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(sum);
        });
        A.start();
        //睡眠一秒钟，保证线程A已经计算完成，阻塞在wait方法. 若不停留时间，先执行notify线上会一直阻塞挂起
        Thread.sleep(1000);
        synchronized (obj) {
            obj.notify();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread A = new Thread(() -> {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += i;
            }
            LockSupport.park();
            System.out.println(sum);
        });
        A.start();
        //睡眠一秒钟，保证线程A已经计算完成，阻塞在wait方法
//        Thread.sleep(1000);
        LockSupport.unpark(A);
    }

}
