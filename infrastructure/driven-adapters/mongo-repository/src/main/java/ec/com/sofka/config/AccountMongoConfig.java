package ec.com.sofka.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "ec.com.sofka.database.account",
       reactiveMongoTemplateRef = "accountMongoTemplate")
public class AccountMongoConfig {
    @Primary
    @Bean(name = "accountsDatabaseFactory")
    public ReactiveMongoDatabaseFactory accountsDatabaseFactory(
            @Value("${spring.data.mongodb.accounts-uri}") String uri) {
        System.out.println("Creating Accounts Database Factory with URI: " + uri);
        MongoClient mongoClient = MongoClients.create(uri);
        return new SimpleReactiveMongoDatabaseFactory(mongoClient,"accounts_manager");
    }


    @Primary
    @Bean(name = "accountMongoTemplate")
    public ReactiveMongoTemplate accountsMongoTemplate(@Qualifier("accountsDatabaseFactory") ReactiveMongoDatabaseFactory accountsDatabaseFactory) {
        return new ReactiveMongoTemplate(accountsDatabaseFactory);
    }
}
