package gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关验证签名：https://blog.csdn.net/qq_34272760/article/details/124986622
 * 码云地址：https://gitee.com/zhurongsheng/springcloud-gateway-rsa
 */

/**
 * 网关日志
 */
@Order(-1)
@Configuration
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper=new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        System.out.println("异常："+ex);

        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // 设置返回JSON
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                //返回响应结果
//                ApiResult<String> result=new ApiResult<>();
//                result.setCodeToFail(ex.getMessage());
                Map result = new HashMap<>(2);
                result.put("code",response.getStatusCode().value());
                result.put("message",ex.getMessage());

                System.out.println("网关处理请求发送异常："+ex.getMessage()+" "+ex);
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            }
            catch (JsonProcessingException e) {
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
