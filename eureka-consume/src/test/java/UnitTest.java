
import consumer.ConsumerApplication;
import consumer.OpenFeignClient;
import org.junit.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;

@SpringBootTest(classes = ConsumerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class UnitTest {
    @Autowired
    private OpenFeignClient openFeignClient;


    @Resource
    private RestTemplate restTemplate;

    @Resource
    private LoadBalancerClient loadBalancerClient;
    @Test
    public void test() {
        try {
            System.out.println(openFeignClient.home("gengjinfeng"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        ServiceInstance serviceInstance = this.loadBalancerClient.choose("eureka-provider");
        System.out.println(serviceInstance.getHost());
        System.out.println(serviceInstance.getInstanceId());
        System.out.println(serviceInstance.getPort());


        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://eureka-provider/home?name="+ UUID.randomUUID().toString(),String.class);

        System.out.println(responseEntity.getBody());
    }
}