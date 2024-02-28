package kr.co.glog;

import kr.co.glog.app.admin.interceptor.GeneralLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


	@Override
	public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(new GeneralLog())
                .addPathPatterns("/**")
				.excludePathPatterns("/healthCheck.html")	// 상태 체크 페이지 제외
                .excludePathPatterns("/login") 		//	로그인 페이지는 예외
				.excludePathPatterns("/error") 		//	에러 페이지는 예외
        		.excludePathPatterns("/lib/**")
        		.excludePathPatterns("/images/**")
        		.excludePathPatterns("/app/**")
				.excludePathPatterns("/local/**")
        		;
           //     
    }
}
