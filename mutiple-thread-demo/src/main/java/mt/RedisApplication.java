package mt;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class RedisApplication {
    public static void main(String[] args) {

        RedissonClient redisson = Redisson.create();
        System.out.println(redisson.getAtomicLong("111"));

//        Config config = new Config();
//        config.useSingleServer().setAddress("127.0.0.1:6379");
//        RedissonClient redisson = Redisson.create(config);
    }
}
