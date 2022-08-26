package mt;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import static sun.misc.VM.getState;

/**
 * 基于AQS实现不可重入的独占锁。
 */
public class MutexAQS implements Lock, Serializable {
    private static final long serialVersionUID = 6142703832416865725L;

    private static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = -5633158763632936629L;

        /***
         * 是否锁已经被持有
         * @return
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }


        /***
         * 如果state为0 则尝试获取锁
         * @param arg
         * @return
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        /***
         * 释放锁 设置state为0
         * @param arg
         * @return
         */
        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalMonitorStateException();
            }
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }

        Condition newCondition() {
            return new ConditionObject();
        }
    }

    //创建一个sync对象做相应的操作
    private final Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    /***
     * 解锁
     */
    @Override
    public void unlock() {
        sync.release(1);
    }

    /***
     * 创建一个condition对象
     * @return
     */
    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
