package kr.co.glog.app.web.exception;

import kr.co.glog.common.exception.ApplicationRuntimeException;
import kr.co.glog.common.exception.ErrorCode;
import kr.co.glog.common.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    /**
     * HttpStatus 500: FAILURE
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception e) {
        log.error("[Exception] error = {}", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(RestResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR));
    }

    /**
     * HttpStatus 400: FAILURE
     * 시스템은 문제가 없지만, 비즈니스 로직에서 에러가 발생
     * 얼른 찾아서 수정하자
     */
    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity onBaseException(ApplicationRuntimeException e) {
        log.error("[ApplicationRuntimeException] error = {}", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestResponse.fail(ErrorCode.COMMON_BUSINESS_LOGIC_ERROR));
    }

    /**
     * 예상치 않은 Exception 중에서 모니터링 skip 이 가능한 Exception 을 처리할 때
     * 얘는 그냥 스킵이다
     * ex) ClientAbortException
     */
    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity skipException(Exception e) {
        log.warn("[SkipException] error = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(RestResponse.fail(ErrorCode.COMMON_SYSTEM_ERROR));
    }

    /**
     * HttpStatus 400: request parameter error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("[MethodArgumentNotValidException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestResponse.fail(ErrorCode.COMMON_INVALID_PARAMETER));
    }

    /**
     * HttpStatus 400: JSON parse error
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("[HttpMessageNotReadableException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestResponse.fail(ErrorCode.COMMON_INVALID_PARAMETER));
    }

    /**
     * HttpStatus 400: enum type 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("[MethodArgumentTypeMismatchException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestResponse.fail(ErrorCode.COMMON_INVALID_PARAMETER));
    }

    /**
     * HttpStatus 400: request parameter bind error
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity bindException(BindException e) {
        log.warn("[BindException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(RestResponse.fail(ErrorCode.COMMON_INVALID_PARAMETER));
    }

    /**
     * HttpStatus 401: 인증 오류
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity authenticationException(AuthenticationException e) {
        log.warn("[AuthenticationException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestResponse.fail(ErrorCode.COMMON_UNAUTHORIZED));
    }

    /**
     * HttpStatus 403: 권한 없음
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity accessDeniedException(AccessDeniedException e) {
        log.warn("[AuthenticationException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RestResponse.fail(ErrorCode.COMMON_FORBIDDEN));
    }

    /**
     * HttpStatus 405: 존재하지 않는 url mapping
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("[HttpRequestMethodNotSupportedException] errorMsg = {}", NestedExceptionUtils.getMostSpecificCause(e).getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(RestResponse.fail(ErrorCode.COMMON_METHOD_NOT_ALLOWED));
    }
}