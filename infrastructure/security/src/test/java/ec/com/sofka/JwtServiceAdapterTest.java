package ec.com.sofka;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceAdapterTest {

    private JwtServiceAdapter jwtServiceAdapter;

    @BeforeEach
    void setUp() {
        jwtServiceAdapter = new JwtServiceAdapter();
        ReflectionTestUtils.setField(jwtServiceAdapter, "secretKey", Base64.getEncoder().encodeToString("5575c1586aebf00eb61e46d54d0af1603573738636c3816f20f0066662800515".getBytes()));
        ReflectionTestUtils.setField(jwtServiceAdapter, "jwtExpiration", 86400000L);
    }

    @Test
    void shouldGenerateAndValidateTokenSuccessfully() {
        String token = jwtServiceAdapter.generateToken("testUser", "ADMIN");

        assertNotNull(token, "Token should not be null");
        assertTrue(jwtServiceAdapter.isTokenValid(token), "Token should be valid");

        String extractedUsername = jwtServiceAdapter.extractUsername(token);
        assertEquals("testUser", extractedUsername, "Extracted username should match");

        String extractedRole = jwtServiceAdapter.extractRole(token);
        assertEquals("ADMIN", extractedRole, "Extracted role should match");
    }

    @Test
    void shouldReturnFalseForInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtServiceAdapter.isTokenValid(invalidToken), "Invalid token should not be valid");
    }

    @Test
    void shouldFailIfTokenIsExpired() {
        ReflectionTestUtils.setField(jwtServiceAdapter, "jwtExpiration", -1000L);
        String token = jwtServiceAdapter.generateToken("testUser", "USER");

        boolean isValid = jwtServiceAdapter.isTokenValid(token);

        // Assert
        assertFalse(isValid, "Expired token should not be valid");
    }

    @Test
    void shouldThrowExceptionForMalformedToken() {
        String malformedToken = "malformed.token.value";

        assertFalse(jwtServiceAdapter.isTokenValid(malformedToken), "Malformed token should not be valid");
    }
}
