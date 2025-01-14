package ec.com.sofka.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "ec.com.sofka.database.bank",
        reactiveMongoTemplateRef = "bankMongoTemplate"
)
public class BankMongoConfig {
    @Value("${spring.data.mongodb.bank-uri}")
    private String bankMongoUri;

    @Primary
    @Bean(name = "bankDatabaseFactory")
    public ReactiveMongoDatabaseFactory bankDatabaseFactory() {
        MongoClient mongoClient = MongoClients.create(bankMongoUri);
        return new SimpleReactiveMongoDatabaseFactory(mongoClient, "bank_account_v2");
    }

    @Primary
    @Bean(name = "bankMongoTemplate")
    public ReactiveMongoTemplate bankMongoTemplate(@Qualifier("bankDatabaseFactory") ReactiveMongoDatabaseFactory bankDatabaseFactory) {
        return new ReactiveMongoTemplate(bankDatabaseFactory);
    }
}
