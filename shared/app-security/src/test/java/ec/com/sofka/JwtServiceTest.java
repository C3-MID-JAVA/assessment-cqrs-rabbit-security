package ec.com.sofka;

import ec.com.sofka.data.UserEntity;
import ec.com.sofka.utils.enums.RoleEnum;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(null);
    }

    @Test
    void generateToken_validUserDetails_tokenGenerated() {
        UserDetails userDetails = new UserEntity(null, "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_EXECUTIVE);

        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_validToken_returnsUsername() {
        UserDetails userDetails = new UserEntity(null, "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_EXECUTIVE);
        String token = jwtService.generateToken(userDetails);

        String extractedUsername = jwtService.extractUsername(token);

        assertEquals("test@gmail.com", extractedUsername);
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        UserDetails userDetails = new UserEntity(null, "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_EXECUTIVE);
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenExpired_expiredToken_returnsTrue() {
        UserDetails userDetails = new UserEntity(null, "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_EXECUTIVE);
        String expiredToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 30))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 15))
                .compact();


        ExpiredJwtException exception = assertThrows(ExpiredJwtException.class, () -> jwtService.extractAllClaims(expiredToken));

        assertEquals(ExpiredJwtException.class, exception.getClass());

    }

    @Test
    void extractClaim_validToken_returnsSpecificClaim() {
        UserDetails userDetails = new UserEntity(null, "Jhon", "Doe", "test@gmail.com", "Test123.", RoleEnum.ROLE_EXECUTIVE);
        String token = jwtService.generateToken(userDetails);

        String extractedClaim = jwtService.extractClaim(token, claims -> claims.getSubject());

        assertEquals("test@gmail.com", extractedClaim);
    }

}