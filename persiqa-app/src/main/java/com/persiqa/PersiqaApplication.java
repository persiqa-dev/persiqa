package com.persiqa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/** Spring Boot entry point for the Persiqa multiuser web application. */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.persiqa.persistence.repository")
public class PersiqaApplication {
  public static void main(String[] args) {
    SpringApplication.run(PersiqaApplication.class, args);
  }
}
