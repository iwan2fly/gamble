package kr.co.glog.common.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import kr.co.glog.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {

	// HTTP ResponseCdoe
	private HttpStatus httpStatus = HttpStatus.OK;

	// Service Response Code
	private String responseCode = RestResponseCode.OK.getCode();

	// Service Response Message
	private String responseMessage = RestResponseCode.OK.getMessage();

	// Service Debug Message
	private String debugMessage = "";

    // Service Response Data
    private Map<String, Object> object;

    private boolean success;
    private T data;
    private String errorCode;
    private String message;

    public RestResponse putData(String name, Object obj) {
        if (MapUtils.isEmpty(this.object)) {
            this.object = new HashMap<>();
        }
        this.object.put(name, obj);
        return this;
    }

    public static <T> RestResponse<T> success() {
        return (RestResponse<T>) RestResponse.builder()
                .success(true)
                .build();
    }

    public static <T> RestResponse<T> success(T data) {
        return (RestResponse<T>) RestResponse.builder()
                .success(true)
                .data(data)
                .build();
    }

    public static <T> RestResponse<T> fail(T data) {
        return (RestResponse<T>) RestResponse.builder()
                .success(false)
                .data(data)
                .build();
    }

    public static RestResponse fail(ErrorCode errorCode) {
        return RestResponse.builder()
                .success(false)
                .errorCode(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static RestResponse fail(String message, String errorCode) {
        return RestResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .build();
    }

}
