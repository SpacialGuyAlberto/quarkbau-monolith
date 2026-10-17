package com.quarkbau.monolith.config;

import com.quarkbau.monolith.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DatabaseSeeder {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            DataSource dataSource
    ) {
        return args -> {
            if (userRepository.count() == 0) {
                System.out.println("No users found. Executing seed-data.sql to populate initial data...");

                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                        false, false, "UTF-8", new ClassPathResource("seed-data.sql")
                );
                
                try {
                    populator.execute(dataSource);
                    System.out.println("Database seeding from seed-data.sql completed successfully.");
                } catch (Exception e) {
                    System.err.println("Error executing seed-data.sql: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Data exists. Skipping seed-data.sql execution.");
            }
        };
    }
}
