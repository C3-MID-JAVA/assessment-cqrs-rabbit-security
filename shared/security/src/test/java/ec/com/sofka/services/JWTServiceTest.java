package ec.com.sofka.services;


import ec.com.sofka.data.ROLE;
import ec.com.sofka.data.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;

import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
    }

    @Test
    void generateToken_validUserDetails_tokenGenerated() {
        UserDetails userDetails = new UserEntity(null, "testUser", "password", ROLE.USER);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_validToken_returnsUsername() {
        UserDetails userDetails = new UserEntity(null, "testUser", "password", ROLE.USER);
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        UserDetails userDetails = new UserEntity(null, "testUser", "password", ROLE.USER);
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenExpired_expiredToken_returnsTrue() {
        UserDetails userDetails = new UserEntity(null, "testUser", "password", ROLE.USER);
        String expiredToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 30))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 15))
                .compact();


        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, () -> jwtService.extractAllClaims(expiredToken));

        assertEquals("Token expired Service", exception.getMessage());

    }



    @Test
    void extractClaim_validToken_returnsSpecificClaim() {
        UserDetails userDetails = new UserEntity(null, "testUser", "password", ROLE.USER);
        String token = jwtService.generateToken(userDetails);

        String extractedClaim = jwtService.extractClaim(token, claims -> claims.getSubject());

        assertEquals("testUser", extractedClaim);
    }
}