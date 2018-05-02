package net.slipp.todo_list;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackages = {"net.slipp.todo_list"})
public class AppConfig {
    @Bean
    public CountableNotiManager notiManager() {
        return new CountableNotiManager();
    }
}
