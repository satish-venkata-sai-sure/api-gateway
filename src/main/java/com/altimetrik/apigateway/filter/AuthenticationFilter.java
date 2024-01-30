package com.altimetrik.apigateway.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.altimetrik.apigateway.exception.AuthorizationHeaderMissingException;
import com.altimetrik.apigateway.exception.InvalidTokenException;
import com.altimetrik.apigateway.util.JwtUtil;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private static final String ADMIN_ROLE = "ROLE_admin";
    private static final String CUSTOMER_ROLE = "ROLE_customer";
    
    @Autowired
    private RouteValidator validator;

    //    @Autowired
//    private RestTemplate template;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new AuthorizationHeaderMissingException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                String path = exchange.getRequest().getURI().getPath();

                try {
                    jwtUtil.validateToken(authHeader);
                    List<String> roles = jwtUtil.extractRoles(authHeader);
                    
                    if (path.startsWith("/admin/") && !roles.contains(ADMIN_ROLE)) {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response.writeWith(Mono.just(response.bufferFactory().wrap("Access denied due to role mismatch".getBytes())));
                       } else if (path.startsWith("/customer/") && !roles.contains(CUSTOMER_ROLE)) {
                        ServerHttpResponse response = exchange.getResponse();
                        response.setStatusCode(HttpStatus.FORBIDDEN);
                        return response.writeWith(Mono.just(response.bufferFactory().wrap("Access denied due to role mismatch".getBytes())));
                                           }
                } catch (Exception e) {
                    System.out.println("invalid access...!");
                    throw new InvalidTokenException("un authorized access to application");
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
