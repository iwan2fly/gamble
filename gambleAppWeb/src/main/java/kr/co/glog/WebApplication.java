package kr.co.glog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@Slf4j
@EnableCaching
@SpringBootApplication
@EnableScheduling
public class WebApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebApplication.class);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(WebApplication.class, args);
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        Arrays.sort(beanNames);

        for (String beanName : beanNames) {
            log.debug(beanName);
        }
    }
}