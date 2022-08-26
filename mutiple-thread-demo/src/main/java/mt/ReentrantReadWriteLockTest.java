package mt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * JAVA的并发包提供了读写锁ReentrantReadWriteLock，它表示两个锁，一个是读操作相关的锁，称为共享锁；一个是写相关的锁，称为排他锁，描述如下：
 *
 * 线程进入读锁的前提条件：
 *
 * 没有其他线程的写锁，
 *
 * 没有写请求或者有写请求，但调用线程和持有锁的线程是同一个。
 *
 * 线程进入写锁的前提条件：
 *
 * 没有其他线程的读锁
 *
 * 没有其他线程的写锁
 *
 * 而读写锁有以下三个重要的特性：
 *
 * （1）公平选择性：支持非公平（默认）和公平的锁获取方式，吞吐量还是非公平优于公平。
 *
 * （2）重进入：读锁和写锁都支持线程重进入。
 *
 * （3）锁降级：遵循获取写锁、获取读锁再释放写锁的次序，写锁能够降级成为读锁。
 * 原文链接：https://blog.csdn.net/qq_37740841/article/details/110707178
 */
public class ReentrantReadWriteLockTest {
    static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static Lock readLock = readWriteLock.readLock();
    static Lock writeLock = readWriteLock.writeLock();

    public void read(Lock lock) throws InterruptedException {
        lock.lock();
        Thread.sleep(1000);
        System.out.println("read");
        lock.unlock();
    }

    public void write(Lock lock) throws InterruptedException {
        lock.lock();
        Thread.sleep(1000);
        System.out.println("write");
        lock.unlock();
    }

    public static void main(String[] args) {
        ReentrantReadWriteLockTest t = new ReentrantReadWriteLockTest();
        Runnable read = ()-> {
            try {
                t.read(readLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        Runnable write = ()-> {
            try {
                t.write(writeLock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for(int i = 0;i<20;i++) {
            new Thread(read).start();
        }

        for (int i = 0;i<3;i++) {
            new Thread(write).start();
        }

        /**
         * read 几乎同时输出；write每隔一秒输出；，体现共享锁与排它锁的作用；
         */
    }
}
