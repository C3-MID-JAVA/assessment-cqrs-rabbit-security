package ec.com.sofka;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ec.com.sofka.repository")
public class TestMongoConfig {
        @Bean
        public MongoClient reactiveMongoClient() {
                return MongoClients.create("mongodb://root:root@localhost:27017/bank_account");
        }

        @Bean
        public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient, MappingMongoConverter converter) {
                return new ReactiveMongoTemplate(mongoClient, "bank_account");
        }
}