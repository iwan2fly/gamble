package kr.co.glog.common.model;

import kr.co.glog.common.utils.EnumToMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@EnumToMap
@Getter
@RequiredArgsConstructor
public enum RestResponseCode {

    OK( "OK", "정상 저리되었습니다." ),
    ILLEGAL_PARAMETER("ILLEGAL_PARAMETER", "파라미터 구성에 오류가 있습니다." ),
    LOGIN_REQUIRED("LOGIN_REQUIRED", "로그인이 필요한 서비스입니다." ),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "정의되지 않은 내부 오류입니다." ),
    UNEXPECTED( "UNEXPECTED", "예상하지 못한 오류가 발생했습니다." )
    ;

    private final String code;         // 응답코드
    private final String message;       // 응답메시지



}
