package com.persiqa.application;

import com.persiqa.core.RelationRegistry;
import com.persiqa.core.StatementFirstRecording;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Wires the shared statement-first write ritual for the application. */
@Configuration
public class KnowledgeConfiguration {
  @Bean
  RelationRegistry relationRegistry() {
    return new RelationRegistry();
  }

  @Bean
  StatementFirstRecording statementFirstRecording(RelationRegistry relationRegistry) {
    return new StatementFirstRecording(relationRegistry);
  }
}
