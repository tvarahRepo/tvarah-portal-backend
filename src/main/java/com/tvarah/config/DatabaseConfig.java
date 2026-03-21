package com.tvarah.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.tvarah.repository")
public class DatabaseConfig {
    // DataSource and JPA are auto-configured via application.yml.
    // This class enables JPA auditing (createdAt/updatedAt) and transaction management.
}
