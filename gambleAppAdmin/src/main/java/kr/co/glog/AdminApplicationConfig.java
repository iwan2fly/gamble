package kr.co.glog;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class AdminApplicationConfig implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")    // 핸들러 추가
                // .addResourceLocations("/static/");                   // 클래스패스 설정시 끝에 꼭 / 넣어주자.
                .addResourceLocations("/resources/");                   // 클래스패스 설정시 끝에 꼭 / 넣어주자.
    }


}
