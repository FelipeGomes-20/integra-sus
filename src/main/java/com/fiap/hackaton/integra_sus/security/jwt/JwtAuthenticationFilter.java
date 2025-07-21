package com.fiap.hackaton.integra_sus.security.jwt;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AuthenticationWebFilter {

    public JwtAuthenticationFilter(
            ReactiveAuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            ReactiveUserDetailsService userDetailsService
    ) {
        super(authenticationManager);
        this.setServerAuthenticationConverter(new JwtServerAuthenticationConverter(jwtUtil, userDetailsService));
        this.setSecurityContextRepository(new NoOpSecurityContextRepository());
    }

    static class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

        private final JwtUtil jwtUtil;
        private final ReactiveUserDetailsService userDetailsService;

        public JwtServerAuthenticationConverter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
            this.jwtUtil = jwtUtil;
            this.userDetailsService = userDetailsService;
        }

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.empty();
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.isTokenValid(token)) {
                return Mono.empty();
            }

            String username = jwtUtil.extractUsername(token);

            return userDetailsService.findByUsername(username)
                    .map(userDetails ->
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                    );
        }
    }

    static class NoOpSecurityContextRepository implements ServerSecurityContextRepository {

        @Override
        public Mono<Void> save(ServerWebExchange exchange, org.springframework.security.core.context.SecurityContext context) {
            return Mono.empty();
        }

        @Override
        public Mono<org.springframework.security.core.context.SecurityContext> load(ServerWebExchange exchange) {
            return Mono.empty();
        }
    }
}