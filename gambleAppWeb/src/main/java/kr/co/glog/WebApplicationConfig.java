package kr.co.glog;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebApplicationConfig {

    @Profile("dev")
    @Configuration
    @RequiredArgsConstructor
    public static class DevLocalMvcConfiguration implements WebMvcConfigurer {

        private final AppConfig appConfig;

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            // js 파일수정 후 저장 시 변경사항이 즉시적용됨. (js 뿐만아니라 static 하위 모든 파일)
            registry.addResourceHandler("/**", appConfig.getStaticPathPattern())
                    .addResourceLocations("file:src/main/resources/static/");
        }
    }

    @Profile("!dev")
    @Configuration
    @RequiredArgsConstructor
    public static class ProductionMvcConfiguration implements WebMvcConfigurer {

        private final AppConfig appConfig;

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/**", appConfig.getStaticPathPattern())
                    .addResourceLocations("classpath:/static/")
                    .setCacheControl(CacheControl.maxAge(20, TimeUnit.MINUTES)); // 브라우저에서 20분 동안 캐시 유지
        }
    }
}
