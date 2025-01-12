package ec.com.sofka.config;

import ec.com.sofka.gateway.TransactionRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( basePackages = "ec.com.sofka",
includeFilters = {
        @ComponentScan.Filter(type= FilterType.REGEX, pattern = "^.+UseCase$"),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {TransactionRepository.class})
})
public class UseCaseConfig {

}
