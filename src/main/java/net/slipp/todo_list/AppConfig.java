package net.slipp.todo_list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
@ComponentScan(basePackages = {"net.slipp.todo_list"})
public class AppConfig {
  @Bean
  @Primary
  public CountableNotiManager countableNotiManager() {
    return new CountableNotiManager();
  }
}
