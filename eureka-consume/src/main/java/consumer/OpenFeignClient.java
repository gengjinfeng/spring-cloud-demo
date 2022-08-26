package consumer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "eureka-provider")
public interface OpenFeignClient {
    @RequestMapping("home")
    public String home(String name);

    @PostMapping("getList")
    public List<String> getList(@RequestBody List<String> list);
}
