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
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
@Configuration
@EnableReactiveMongoRepositories(basePackages = "ec.com.sofka.database.events",
        reactiveMongoTemplateRef = "eventMongoTemplate")
public class EventMongoConfig {

    @Value("${spring.data.mongodb.events-uri}")
    private String eventMongoUri;

    @Bean(name = "eventsDatabaseFactory")
    public ReactiveMongoDatabaseFactory eventsDatabaseFactory() {
        MongoClient mongoClient = MongoClients.create(eventMongoUri);
        return new SimpleReactiveMongoDatabaseFactory(mongoClient, "events_eda_es");
    }

    @Bean(name = "eventMongoTemplate")
    public ReactiveMongoTemplate eventsMongoTemplate(@Qualifier("eventsDatabaseFactory") ReactiveMongoDatabaseFactory eventsDatabaseFactory) {
        return new ReactiveMongoTemplate(eventsDatabaseFactory);
    }
}
