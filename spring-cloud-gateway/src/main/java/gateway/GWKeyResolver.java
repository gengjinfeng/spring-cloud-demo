package gateway;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class GWKeyResolver {

    /**
     * ip限流策略
     * @return
     */
    @Bean("ipKeyResolver")
    public KeyResolver ipKeyResolver() {
        //return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());

        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                String hostName = Objects.requireNonNull(exchange.getRequest()
                        .getRemoteAddress()).getHostName();
                System.out.println("hostName:" + hostName);
                return Mono.just(hostName);
            }
        };
    }

    /**
     * 请求参数限流策略
     * @return
     */
    @Bean("userKeyResolver")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("user"));
    }

    /**
     * 请求路径（即接口）限流
     * @return
     */
    @Bean("apiKeyResolver")
    @Primary
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }

}
