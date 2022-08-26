package consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RestController
public class MainController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private OpenFeignClient openFeignClient;

    @GetMapping("testRibbon")
    public String testRibbon(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-provider");
        return "hi "+serviceInstance.getHost()+":"+serviceInstance.getPort();
    }

    @GetMapping("hi")
    public String hi(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-provider");
        String url = "http://"+serviceInstance.getPort()+":"+serviceInstance.getPort()+"/home?name="+ UUID.randomUUID().toString();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url,String.class);
        return responseEntity.getBody();
    }

    @GetMapping("hi2")
    public String hi2(){
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://eureka-provider/home?name="+ UUID.randomUUID().toString(),String.class);
        return responseEntity.getBody();
    }

    @GetMapping("getList")
    public List<String> getList(@RequestBody List<String> paras){
        List<String> list = openFeignClient.getList(paras);
        list.add(UUID.randomUUID().toString());
        return list;
    }
}
