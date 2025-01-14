package ec.com.sofka.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

//17. Configure the same as you did for autogenerate beans in appservices
@Configuration
@ComponentScan( basePackages = "ec.com.sofka",
        includeFilters = {
                @ComponentScan.Filter(type= FilterType.REGEX, pattern = "^.+ViewUseCase$")
        })
public class UseCaseConfig {
}
