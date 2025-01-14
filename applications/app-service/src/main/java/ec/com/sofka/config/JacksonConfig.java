package ec.com.sofka.config;

import ec.com.sofka.IJSONMapper;
import ec.com.sofka.JSONMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public IJSONMapper jsonMapper(){
        return new JSONMap();
    }
}
