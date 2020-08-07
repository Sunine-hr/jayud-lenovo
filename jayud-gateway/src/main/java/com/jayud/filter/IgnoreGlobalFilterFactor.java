package com.jayud.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忽略过滤路由
 *
 * @author larry
 * @date 2020/7/10
 */
@Component
public class IgnoreGlobalFilterFactor extends AbstractGatewayFilterFactory<IgnoreGlobalFilterFactor.Config> {

    public static final String IGNORE_GLOBAL_FILTER = "IgnoreGlobalFilterFactor";

    public IgnoreGlobalFilterFactor() {
        super(Config.class);
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return this::filter;
    }

    private Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //增加忽略的请求头
        exchange.getAttributes().put(IGNORE_GLOBAL_FILTER,true);
        return chain.filter(exchange);
    }

    @Override
    public String name() {
        return "IgnoreGlobalFilterFactor";
    }
}