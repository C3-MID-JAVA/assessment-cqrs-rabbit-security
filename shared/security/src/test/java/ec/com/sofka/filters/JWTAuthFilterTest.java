package ec.com.sofka.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.sofka.data.AuthErrorResponse;
import ec.com.sofka.services.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTAuthFilterTest {

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private JWTAuthFilter jwtAuthFilter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void filter_noAuthHeader_shouldProceedWithoutAuthentication() {

        MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/test"));
        WebFilterChain chain = mock(WebFilterChain.class);

        when(chain.filter(exchange)).thenReturn(Mono.empty());

        StepVerifier.create(jwtAuthFilter.filter(exchange, chain))
                .verifyComplete();


        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void filter_validToken_shouldAuthenticateAndProceed() {

        String token = "valid.jwt.token";
        String username = "testUser";
        String roles = "USER";

        Claims claims = mock(Claims.class);
        when(claims.get("roles")).thenReturn(roles);

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(jwtService.extractAllClaims(token)).thenReturn(claims);
        when(jwtService.isTokenValid(eq(token), any(UserDetails.class))).thenReturn(true);


        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header("Authorization", "Bearer " + token)
        );
        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());


        StepVerifier.create(jwtAuthFilter.filter(exchange, chain))
                .verifyComplete();


        verify(chain, times(1)).filter(exchange);
    }

    @Test
    void filter_expiredToken_shouldReturnUnauthorized() throws IOException {

        String token = "expired.jwt.token";


        when(jwtService.extractUsername(token)).thenThrow(new ExpiredJwtException(null, null, "Token expired"));


        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header("Authorization", "Bearer " + token)
        );

        WebFilterChain chain = mock(WebFilterChain.class);


        StepVerifier.create(jwtAuthFilter.filter(exchange, chain))
                .expectComplete()
                .verify();


        verify(chain, times(0)).filter(exchange);


        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());


        byte[] responseBody = exchange.getResponse().getBody().blockFirst().asByteBuffer().array();
        AuthErrorResponse response = objectMapper.readValue(responseBody, AuthErrorResponse.class);

        assertEquals("ACCESS_DENIED", response.getError());
        assertEquals("The token has expired", response.getMessage());
    }



    @Test
    void filter_invalidToken_shouldReturnUnauthorized() throws IOException {

        String token = "invalid.jwt.token";


        when(jwtService.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));


        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header("Authorization", "Bearer " + token)
        );

        WebFilterChain chain = mock(WebFilterChain.class);


        StepVerifier.create(jwtAuthFilter.filter(exchange, chain))
                .expectSubscription()
                .verifyComplete();


        verify(chain, times(0)).filter(exchange);


        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());


        String responseBody = exchange.getResponse().getBodyAsString().block();
        AuthErrorResponse response = objectMapper.readValue(responseBody, AuthErrorResponse.class);

        assertEquals("ACCESS_DENIED", response.getError());
        assertEquals("Authentication error", response.getMessage());
    }

}
