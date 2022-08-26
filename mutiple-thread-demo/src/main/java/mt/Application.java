package mt;

import sun.misc.Unsafe;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Application {


    /**
     * ReentrantLock：独占锁+同步队列
     * CountDownLatch： 共享锁+同步队列
     * Semaphore ：共享锁+同步队列
     * CyclieBarries：ReentrantLock+Condition队列
     * @param args
     */

    public static void main(String[] args) throws ExecutionException, InterruptedException {
                // Unsafe.getUnsafe().compareAndSwapInt(this,0,1,2);



        AtomicInteger atomicInteger = new AtomicInteger(100);

        System.out.println(atomicInteger.incrementAndGet());


        String s = null;
        try {
            //s.getBytes();

            int m = 2/0;
        }
//        catch (Throwable throwable){
//            throwable.printStackTrace();
//            throw new Error("");
//        }
        catch (ArithmeticException arithmeticException){
            arithmeticException.printStackTrace();
        }
        catch (RuntimeException runtimeException){
            runtimeException.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        catch (Error error){
            error.printStackTrace();
        }




        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return null;
            }
        });
        String result = futureTask.get();

    }
}
