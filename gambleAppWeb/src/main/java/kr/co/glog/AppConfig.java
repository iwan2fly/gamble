package kr.co.glog;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Component
public class AppConfig {

    /**
     * js 파일 수정 시 캐싱 방지를 위하여 static resource 경로 사이에 random uuid 를 넣음
     * 컨트롤러에서 model attribute 로 넘겨 mustache 에서 resource 경로에 사용
     */
    private final String staticPath = String.format("/%s", UUID.randomUUID());
    private final String staticPathPattern = String.format("%s/**", getStaticPath());
}
