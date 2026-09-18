package com.quarkbau.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {
    Neo4jReactiveDataAutoConfiguration.class,
    Neo4jReactiveRepositoriesAutoConfiguration.class
})
@EnableJpaRepositories(
    basePackages = "com.quarkbau.monolith",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = "com\\.quarkbau\\.monolith\\.graph\\.repository\\..*"
    )
)
@EnableNeo4jRepositories(
    basePackages = "com.quarkbau.monolith.graph.repository",
    transactionManagerRef = "neo4jTransactionManager"
)
public class QuarkBauMonolithApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuarkBauMonolithApplication.class, args);
    }
}
