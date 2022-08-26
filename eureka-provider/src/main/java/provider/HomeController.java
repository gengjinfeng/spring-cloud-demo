package provider;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {

    private RedissonClient redisson;
    private RRateLimiter rateLimiter;

    private RSemaphore semaphore = null;
    {
//        Config config = new Config();
//        config.useSingleServer().setAddress("127.0.0.1:6379");
//        redisson = Redisson.create(config);
        redisson = Redisson.create();

        rateLimiter = redisson.getRateLimiter("myRateLimiter");
        // 初始化
        // 最大流速 = 每1秒钟产生10个令牌
        rateLimiter.trySetRate(RateType.OVERALL, 6, 1, RateIntervalUnit.MINUTES);

        semaphore = redisson.getSemaphore("semaphore");
        semaphore.trySetPermits(3);
    }
    @RequestMapping("home")
    public String home(@RequestParam("name") String name){
        System.out.println(Thread.currentThread().getName()+"准备获取令牌。。。"+new Date().toString());
        if(rateLimiter.tryAcquire()) {
            System.out.println(Thread.currentThread().getName() + "成功获取令牌，进来了。。。" + new Date().toString());
            return "hi," + name + " home page," + Thread.currentThread().getName();
        }else {
            return "请稍后再试！"+Thread.currentThread().getName()+new Date().toString();
        }
    }

    @GetMapping("semaphore")
    public String semaphore() {

        try {
            Thread.sleep(2000);
            semaphore.acquire();
            return Thread.currentThread().getName() + " 通过了";
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
        return Thread.currentThread().getName() + " END";
    }

    @PostMapping("getList")
    public List<String> getList(@RequestBody List<String> list){
        return list;
    }

    @RequestMapping("/test/fallback")
    public Object fallacak() throws InterruptedException {
        Thread.sleep(7000);
        //log.info("熔断处理！！！");
        return "熔断处理！！！,Service Error！！！";
    }

    @RequestMapping(value = "/test/fallback2",consumes="application/json",produces = "application/json")
    @ResponseBody
    public Object fallacak2(@RequestBody String name) {
        return name+" Service UP！！！";
    }
}
