package gateway;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallBack {

    @ResponseBody
    @RequestMapping(value = "/fallbackcontroller")
    public Map fallBackController() {
        Map result = new HashMap(2);
        result.put("code",HttpStatus.TOO_MANY_REQUESTS.value());
        result.put("message","fallback超时熔断,请稍后再试！");
        return  result;
    }
}
