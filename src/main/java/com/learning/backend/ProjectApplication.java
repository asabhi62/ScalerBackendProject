package com.learning.backend;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@SpringBootApplication
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Bean
	public DataSource dataSource(
			@Value("${spring.datasource.url}") String url,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password
	) {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		// Optional: tune pool size and other properties
		dataSource.setMaximumPoolSize(10);
		dataSource.setMinimumIdle(2);
		dataSource.setPoolName("ProjectHikariCP");
		return dataSource;
	}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
