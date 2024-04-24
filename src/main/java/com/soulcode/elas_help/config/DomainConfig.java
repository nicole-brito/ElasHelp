package com.soulcode.elas_help.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("com.soulcode.elas_help.domain")
@EnableJpaRepositories("com.soulcode.elas_help.repos")
@EnableTransactionManagement
public class DomainConfig {
}
