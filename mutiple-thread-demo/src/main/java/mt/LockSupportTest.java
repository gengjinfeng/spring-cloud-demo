package mt;

import java.util.concurrent.locks.LockSupport;


/**
 * LockSupport是用来创建锁和其他同步类的基本线程阻塞原语。
 * LockSupport中的park()和unpark()的作用分别是阻塞线程和解除阻塞线程。
 */
public class LockSupportTest {

    /**
     * LockSupport是一个线程阻塞工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，阻塞之后也有对应的唤醒方法。归根揭底，LockSupport调用的是Unsafe中的native代码。
     *
     * LockSupport类使用了一种名为Permit（许可）的概念来做到阻塞和唤醒线程的功能，每个线程都有一个许可（permit）。
     * permit只有两个值1和0，默认是0。
     * 可以把许可看成是一种（0，1）信号量（Semaphore），但与Semaphore不同的是，许可的累加上限是1。
     * permit默认是0，所以一开始调用park()方法，当前线程就会阻塞，直到别的线程将当前线程的permit设置为1时，park()方法会被唤醒，然后会将permit再次设置为0并返回。
     * @param args
     */


    /**
     *  面试题
     * 为什么可以先唤醒线程后阻塞线程
     * -----因为unpark获取了一个凭证，之后再调用park方法，就可以名正言顺的凭证消费，故不会阻塞。
     * 为什么唤醒两次后阻塞两次，但最终结果还会阻塞线程？
     * -----因为凭证的数量最多为1，连续调用两次unpark和调用一次unpark效果一样，只会增加一个凭证；而调用两次park却需要消费两个凭证，证不够，不能放行。
     * 原文链接：https://blog.csdn.net/weixin_42480780/article/details/115904832
     * @param args
     */

    //LockSupport类可以阻塞当前线程以及唤醒指定被阻塞的线程
    //不需要在锁块中
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "----come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "----被唤醒");
        }, "A");
        a.start();
        Thread b = new Thread(() -> {
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName() + "\t" + "----唤醒动作");
        }, "B");
        b.start();
    }

    //先执行unpark(),依然可以被唤醒
    public static void maina(String[] args) {
        Thread a = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + "\t" + "----come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "----被唤醒");
        }, "A");
        a.start();
        Thread b = new Thread(() -> {
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName() + "\t" + "----唤醒动作");
        }, "B");
        b.start();
    }

}
