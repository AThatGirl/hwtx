package com.cj.gateway.filter;

import com.cj.gateway.utils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义全局过滤器
 *
 * @author 杰瑞
 * @date 2023/01/13
 */
//顺序,值约小优先级越高
//@Order(-1)
//@Component
public class AuthorizeFilter implements GlobalFilter {

    public static final String HEADER_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取请求参数
        ServerHttpRequest request = exchange.getRequest();
        //获取路径
        String path = request.getURI().getPath();
        System.out.println("访问路径:" + path);
        // 放行一部分静态资源
        if (path.startsWith("/user/")) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(HEADER_TOKEN);
        if (token != null && JwtUtils.checkToken(token)){
            //放行
            return chain.filter(exchange);
        }
        //设置状态码
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        //拦截请求
        return exchange.getResponse().setComplete();
    }
}
